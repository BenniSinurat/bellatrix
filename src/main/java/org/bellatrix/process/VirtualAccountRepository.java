package org.bellatrix.process;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.bellatrix.data.Members;
import org.bellatrix.data.RegisterVADoc;
import org.bellatrix.data.VAEvent;
import org.bellatrix.data.VARecordView;
import org.bellatrix.data.VirtualAccounts;
import org.bellatrix.services.CreateEventStatusRequest;
import org.bellatrix.services.LoadBillingStatusByMemberRequest;
import org.bellatrix.services.LoadVAByEventRequest;
import org.bellatrix.services.LoadVAStatusByMemberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;

@Component
@Repository
public class VirtualAccountRepository {

	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(VirtualAccountRepository.class);

	public Integer getBinID(Integer memberID, Integer bankID) {
		try {
			int binID = this.jdbcTemplate.queryForObject(
					"select bin_number_id from member_va where member_id = ? and bank_va_id = ? and enabled = true order by id limit 1",
					Integer.class, memberID, bankID);
			return binID;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void registerMemberVA(Integer memberID, Integer bankID, Integer binID) {
		this.jdbcTemplate.update("insert into member_va (member_id, bank_va_id, bin_number_id) values (?, ?, ?)",
				memberID, bankID, binID);
	}

	public VirtualAccounts loadVAInfo(Integer bankID, Integer binID) {
		try {
			VirtualAccounts va = this.jdbcTemplate.queryForObject(
					"select a.bank_code, a.name, a.transfer_type_id, a.available_digit, b.bin, b.account_no from bank_va a inner join bin_va b on a.id = b.bank_va_id where a.id = ? and b.id = ?",
					new Object[] { bankID, binID }, new RowMapper<VirtualAccounts>() {
						public VirtualAccounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							VirtualAccounts va = new VirtualAccounts();
							va.setAccountNumber(rs.getString("account_no"));
							va.setBankCode(rs.getString("bank_code"));
							va.setBankName(rs.getString("name"));
							va.setBinNumber(rs.getString("bin"));
							va.setReferenceCodeLength(rs.getString("available_digit"));
							va.setTransferTypeID(rs.getInt("transfer_type_id"));
							return va;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public BankVA loadBankVA(Integer memberID, Integer bankVaID, Integer binID) {
		try {
			BankVA va = this.jdbcTemplate.queryForObject(
					"select a.id, a.bank_code, a.name from bank_va a inner join member_va b on b.bank_va_id = a.id where b.member_id = ? and b.bank_va_id = ? and b.bin_number_id = ?",
					new Object[] { memberID, bankVaID, binID }, new RowMapper<BankVA>() {
						public BankVA mapRow(ResultSet rs, int rowNum) throws SQLException {
							BankVA va = new BankVA();
							va.setId(rs.getInt("id"));
							va.setBankCode(rs.getString("bank_code"));
							va.setBankName(rs.getString("name"));
							return va;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BankVA> loadBankVA(Integer memberID) {
		try {
			List<BankVA> va = this.jdbcTemplate.query(
					"select a.id, a.bank_code, a.name from bank_va a inner join member_va b on b.bank_va_id = a.id where b.member_id = "
							+ memberID,
					new RowMapper<BankVA>() {
						public BankVA mapRow(ResultSet rs, int rowNum) throws SQLException {
							BankVA va = new BankVA();
							va.setId(rs.getInt("id"));
							va.setBankCode(rs.getString("bank_code"));
							va.setBankName(rs.getString("name"));
							return va;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BankVA> listBankVA() {
		try {
			List<BankVA> va = this.jdbcTemplate.query("select a.id, a.bank_code, a.name from bank_va a",
					new RowMapper<BankVA>() {
						public BankVA mapRow(ResultSet rs, int rowNum) throws SQLException {
							BankVA va = new BankVA();
							va.setId(rs.getInt("id"));
							va.setBankCode(rs.getString("bank_code"));
							va.setBankName(rs.getString("name"));
							return va;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer registerEventVA(Integer memberID, String ticketID, String name, String description,
			LocalDateTime expired) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into event_va (member_id, ticket_id, name, description, expired_date) values (?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, memberID);
				statement.setString(2, ticketID);
				statement.setString(3, name);
				statement.setString(4, description);
				statement.setTimestamp(5, Timestamp.valueOf(expired));
				return statement;
			}
		}, holder);

		return holder.getKey().intValue();
	}

	public void registerBillingVA(String eventID, RegisterVADoc rva) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into billing_va (event_id, va_no, name, msisdn, reference_no, email, description, billing_state, member_id, amount, minimum_payment, expired_date, fullpayment, persistent, bank_id, callback_url, ticket_id, subscribed, billing_cycle, membership_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, eventID);
				statement.setString(2, rva.getId());
				statement.setString(3, rva.getName());
				statement.setString(4, rva.getMsisdn());
				statement.setString(5, rva.getReferenceNumber());
				statement.setString(6, rva.getEmail());
				statement.setString(7, rva.getDescription());
				statement.setString(8, "ACTIVE");
				statement.setInt(9, rva.getMember().getId());
				statement.setBigDecimal(10, rva.getAmount());
				statement.setBigDecimal(11, rva.getMinimumPayment());
				if (rva.getExpiredAt() != null) {
					statement.setTimestamp(12, Timestamp.valueOf(rva.getExpiredAt()));
				} else {
					statement.setTimestamp(12, null);
				}
				statement.setBoolean(13, rva.isFullPayment());
				statement.setBoolean(14, rva.isPersistent());
				statement.setInt(15, rva.getBankID());
				statement.setString(16, rva.getCallbackURL());
				statement.setString(17, rva.getTicketID());
				if (rva.isSubscribed()) {
					statement.setBoolean(18, rva.isSubscribed());
					statement.setInt(19, rva.getBillingCycle());
					statement.setInt(20, rva.getMembership().getId());
				} else {
					statement.setBoolean(18, rva.isSubscribed());
					statement.setNull(19, Types.INTEGER);
					statement.setNull(20, Types.INTEGER);
				}
				return statement;
			}
		}, holder);

		Integer billingID = holder.getKey().intValue();
		if (rva.isSubscribed()) {
			this.jdbcTemplate.update("insert into billing_status (billing_id, amount, status) values (?, ?, 'UNPAID')",
					new Object[] { billingID, rva.getAmount() });
		}
	}

	public List<VARecordView> loadVAByStatus(String username, Integer memberID, String startDate, String endDate,
			Integer currentPage, Integer pageSize, boolean subscribed) {
		try {
			List<VARecordView> va = this.jdbcTemplate.query(
					"select bv.ticket_id, bv.id, bv.event_id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.bank_id, bv.description, bv.created_date, bv.subscribed, bv.membership_id, bv.billing_cycle, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.subscribed=? and bv.member_id=? and billing_state='ACTIVE' and bv.created_date between ? and ? order by bv.created_date desc limit ?, ?; ",
					new Object[] { subscribed, memberID, startDate, endDate, currentPage, pageSize },
					new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView vav = new VARecordView();
							vav.setTicketID(rs.getString("ticket_id"));
							vav.setId(rs.getString("id"));
							vav.setName(rs.getString("name"));
							vav.setReferenceNumber(rs.getString("reference_no"));
							vav.setPaymentCode(rs.getString("va_no"));
							vav.setStatus(rs.getString("billing_state"));
							vav.setAmount(rs.getBigDecimal("amount"));
							vav.setExpiredAt(rs.getTimestamp("expired_date"));
							vav.setFullPayment(rs.getBoolean("fullpayment"));
							vav.setPersistent(rs.getBoolean("persistent"));
							vav.setDescription(rs.getString("description"));
							vav.setCreatedDate(rs.getTimestamp("created_date"));
							vav.setBankID(rs.getInt("bank_id"));
							vav.setBankCode(rs.getString("bank_code"));
							vav.setBankName(rs.getString("bank_name"));
							vav.setParentUsername(username);
							vav.setSubscribed(subscribed);
							if (rs.getBoolean("subscribed")) {
								vav.setBillingCycle(rs.getInt("billing_cycle"));
								vav.setMembershipID(rs.getInt("membership_id"));
							}
							return vav;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public String getVAID(RegisterVADoc rva) {
		try {
			java.util.Date expiredDate;
			String exd, id = "";
			if (rva.getExpiredAt() != null) {
				expiredDate = Date.from(rva.getExpiredAt().atZone(ZoneId.systemDefault()).toInstant());
				exd = Utils.formatDate(expiredDate);
				id = this.jdbcTemplate.queryForObject(
						"select id from billing_va where va_no = ? and member_id = ? and reference_no = ? and name = ? and email = ? and billing_state = 'ACTIVE' and fullpayment = ? and persistent = ? and bank_id = ? and DATE(expired_date) = DATE(?) and HOUR(expired_date) = HOUR(?) and MINUTE(expired_date) = MINUTE(?)",
						String.class, rva.getId(), rva.getMember().getId(), rva.getReferenceNumber(), rva.getName(),
						rva.getEmail(), rva.isFullPayment(), rva.isPersistent(), rva.getBankID(), exd, exd, exd);
			} else {
				id = this.jdbcTemplate.queryForObject(
						"select id from billing_va where va_no = ? and member_id = ? and reference_no = ? and name = ? and email = ? and billing_state = 'ACTIVE' and fullpayment = ? and persistent = ? and bank_id = ? and expired_date IS NULL",
						String.class, rva.getId(), rva.getMember().getId(), rva.getReferenceNumber(), rva.getName(),
						rva.getEmail(), rva.isFullPayment(), rva.isPersistent(), rva.getBankID());
			}
			return id;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<VARecordView> loadVAByMember(Members member, Integer currentPage, Integer pageSize) {
		try {
			List<VARecordView> vrv = this.jdbcTemplate.query(
					"select bv.ticket_id, bv.id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.bank_id, bv.minimum_payment, bv.description, bv.created_date, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.member_id=? and billing_state='ACTIVE' order by bv.id desc limit ?,?;",
					new Object[] { member.getId(), currentPage, pageSize }, new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView vav = new VARecordView();
							vav.setTicketID(rs.getString("ticket_id"));
							vav.setId(rs.getString("id"));
							vav.setName(rs.getString("name"));
							vav.setReferenceNumber(rs.getString("reference_no"));
							vav.setPaymentCode(rs.getString("va_no"));
							vav.setStatus(rs.getString("billing_state"));
							vav.setAmount(rs.getBigDecimal("amount"));
							vav.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
							vav.setExpiredAt(rs.getTimestamp("expired_date"));
							vav.setFullPayment(rs.getBoolean("fullpayment"));
							vav.setPersistent(rs.getBoolean("persistent"));
							vav.setCallbackURL(rs.getString("callback_url"));
							vav.setDescription(rs.getString("description"));
							vav.setCreatedDate(rs.getTimestamp("created_date"));
							vav.setBankID(rs.getInt("bank_id"));
							vav.setBankCode(rs.getString("bank_code"));
							vav.setBankName(rs.getString("bank_name"));
							vav.setParentUsername(member.getUsername());
							return vav;
						}
					});
			return vrv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<VARecordView> loadVAByDate(Members members, String startDate, String endDate, boolean subscribed) {
		try {
			List<VARecordView> va = this.jdbcTemplate.query(
					"select bv.ticket_id, bv.id, bv.event_id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.bank_id, bv.description, bv.created_date, bv.subscribed, bv.membership_id, bv.billing_cycle, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.subscribed=? and bv.member_id=? and billing_state='ACTIVE' and bv.created_date between ? and ? order by bv.created_date desc;",
					new Object[] { subscribed, members.getId(), startDate, endDate }, new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView vav = new VARecordView();
							vav.setTicketID(rs.getString("ticket_id"));
							vav.setId(rs.getString("id"));
							vav.setName(rs.getString("name"));
							vav.setReferenceNumber(rs.getString("reference_no"));
							vav.setPaymentCode(rs.getString("va_no"));
							vav.setStatus(rs.getString("billing_state"));
							vav.setAmount(rs.getBigDecimal("amount"));
							vav.setExpiredAt(rs.getTimestamp("expired_date"));
							vav.setFullPayment(rs.getBoolean("fullpayment"));
							vav.setPersistent(rs.getBoolean("persistent"));
							vav.setDescription(rs.getString("description"));
							vav.setCreatedDate(rs.getTimestamp("created_date"));
							vav.setBankID(rs.getInt("bank_id"));
							vav.setBankCode(rs.getString("bank_code"));
							vav.setBankName(rs.getString("bank_name"));
							vav.setParentUsername(members.getUsername());
							vav.setSubscribed(subscribed);
							if (rs.getBoolean("subscribed")) {
								vav.setBillingCycle(rs.getInt("billing_cycle"));
								vav.setMembershipID(rs.getInt("membership_id"));
							}
							return vav;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<VARecordView> loadVAByEventID(String username, LoadVAByEventRequest req) {
		try {
			if (req.getBillingStatus() == null | req.getBillingStatus().equalsIgnoreCase("")) {
				List<VARecordView> vrv = this.jdbcTemplate.query(
						"SELECT es.event_id, es.name, es.msisdn, es.email, es.reference_number, es.amount, es.status, es.transaction_number, es.trace_number, es.description, es.created_date, e.name as event_name FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.event_id = ? AND es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC LIMIT ?,?;",
						new Object[] { req.getEventID(), req.getFromDate(), req.getToDate(), req.getCurrentPage(),
								req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_number"));
								vav.setPaymentCode(rs.getString("reference_number"));
								vav.setStatus(rs.getString("status"));
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setTransactionDate(rs.getTimestamp("created_date"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTime("created_date")));
								vav.setCreatedDate(rs.getTimestamp("created_date"));

								VAEvent vae = new VAEvent();
								vae.setTicketID(rs.getString("event_id"));
								vae.setEventName(rs.getString("name"));

								vav.setParentUsername(username);
								vav.setVaEvent(vae);
								return vav;
							}
						});
				return vrv;
			} else {
				List<VARecordView> vrv = this.jdbcTemplate.query(
						"SELECT es.event_id, es.name, es.msisdn, es.email, es.reference_number, es.amount, es.status, es.transaction_number, es.trace_number, es.description, es.created_date, e.name as event_name FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.status = ? AND es.event_id = ? AND es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC LIMIT ?,?;",
						new Object[] { req.getBillingStatus(), req.getEventID(), req.getFromDate(), req.getToDate(),
								req.getCurrentPage(), req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_number"));
								vav.setPaymentCode(rs.getString("reference_number"));
								vav.setStatus(rs.getString("status"));
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setTransactionDate(rs.getTimestamp("created_date"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTime("created_date")));
								vav.setCreatedDate(rs.getTimestamp("created_date"));

								VAEvent vae = new VAEvent();
								vae.setTicketID(rs.getString("event_id"));
								vae.setEventName(rs.getString("name"));

								vav.setParentUsername(username);
								vav.setVaEvent(vae);
								return vav;
							}
						});
				return vrv;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countVAByEventID(LoadVAByEventRequest req) {
		int count = 0;
		if (req.getBillingStatus().equalsIgnoreCase(null) || req.getBillingStatus().equalsIgnoreCase("")) {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(es.id) FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.event_id = ? AND es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC;",
					Integer.class, req.getEventID(), req.getFromDate(), req.getToDate());
		} else {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(es.id) FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.status = ? AND es.event_id = ? AND es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC;",
					Integer.class, req.getBillingStatus(), req.getEventID(), req.getFromDate(), req.getToDate());
		}
		return count;
	}
	
	public List<VARecordView> loadVAByEvent(String username, LoadVAByEventRequest req) {
		try {
			if (req.getBillingStatus() == null | req.getBillingStatus().equalsIgnoreCase("")) {
				List<VARecordView> vrv = this.jdbcTemplate.query(
						"SELECT es.event_id, es.name, es.msisdn, es.email, es.reference_number, es.amount, es.status, es.transaction_number, es.trace_number, es.description, es.created_date, e.name as event_name FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC LIMIT ?,?;",
						new Object[] { req.getFromDate(), req.getToDate(), req.getCurrentPage(),
								req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_number"));
								vav.setPaymentCode(rs.getString("reference_number"));
								if (rs.getString("status").equalsIgnoreCase("PROCESSED")) {
									vav.setStatus("PAID");
								} else {
									vav.setStatus(rs.getString("status"));
								}
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setTransactionDate(rs.getTimestamp("created_date"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTime("created_date")));
								vav.setCreatedDate(rs.getTimestamp("created_date"));

								VAEvent vae = new VAEvent();
								vae.setTicketID(rs.getString("event_id"));
								vae.setEventName(rs.getString("name"));

								vav.setParentUsername(username);
								vav.setVaEvent(vae);
								return vav;
							}
						});
				return vrv;
			} else {
				List<VARecordView> vrv = this.jdbcTemplate.query(
						"SELECT es.event_id, es.name, es.msisdn, es.email, es.reference_number, es.amount, es.status, es.transaction_number, es.trace_number, es.description, es.created_date, e.name as event_name FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.status = ? AND es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC LIMIT ?,?;",
						new Object[] { req.getBillingStatus(), req.getFromDate(), req.getToDate(),
								req.getCurrentPage(), req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_number"));
								vav.setPaymentCode(rs.getString("reference_number"));
								if (rs.getString("status").equalsIgnoreCase("PROCESSED")) {
									vav.setStatus("PAID");
								} else {
									vav.setStatus(rs.getString("status"));
								}
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setTransactionDate(rs.getTimestamp("created_date"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTime("created_date")));
								vav.setCreatedDate(rs.getTimestamp("created_date"));

								VAEvent vae = new VAEvent();
								vae.setTicketID(rs.getString("event_id"));
								vae.setEventName(rs.getString("name"));

								vav.setParentUsername(username);
								vav.setVaEvent(vae);
								return vav;
							}
						});
				return vrv;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Integer countVAByEvent(LoadVAByEventRequest req) {
		int count = 0;
		if (req.getBillingStatus().equalsIgnoreCase(null) || req.getBillingStatus().equalsIgnoreCase("")) {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(es.id) FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC;",
					Integer.class, req.getFromDate(), req.getToDate());
		} else {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(es.id) FROM event_status es JOIN event_va e ON e.ticket_id = es.event_id WHERE es.status = ? AND es.created_date BETWEEN ? AND ? ORDER BY es.created_date DESC;",
					Integer.class, req.getBillingStatus(), req.getFromDate(), req.getToDate());
		}
		return count;
	}

	public List<VARecordView> loadVAByMemberID(Members member) {
		try {
			List<VARecordView> vrv = this.jdbcTemplate.query(
					"select bv.ticket_id, bv.id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.bank_id, bv.minimum_payment, bv.description, bv.created_date, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.member_id=? and billing_state='ACTIVE' order by bv.id desc;",
					new Object[] { member.getId() }, new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView vav = new VARecordView();
							vav.setTicketID(rs.getString("ticket_id"));
							vav.setId(rs.getString("id"));
							vav.setName(rs.getString("name"));
							vav.setReferenceNumber(rs.getString("reference_no"));
							vav.setPaymentCode(rs.getString("va_no"));
							vav.setStatus(rs.getString("billing_state"));
							vav.setAmount(rs.getBigDecimal("amount"));
							vav.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
							vav.setExpiredAt(rs.getTimestamp("expired_date"));
							vav.setFullPayment(rs.getBoolean("fullpayment"));
							vav.setPersistent(rs.getBoolean("persistent"));
							vav.setCallbackURL(rs.getString("callback_url"));
							vav.setDescription(rs.getString("description"));
							vav.setCreatedDate(rs.getTimestamp("created_date"));
							vav.setBankID(rs.getInt("bank_id"));
							vav.setBankCode(rs.getString("bank_code"));
							vav.setBankName(rs.getString("bank_name"));
							vav.setParentUsername(member.getUsername());
							return vav;
						}
					});
			return vrv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<VARecordView> loadVAAllPaid(Members member) {
		try {
			List<VARecordView> paidVA = this.jdbcTemplate.query(
					"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, bv.billing_state, t.amount, bv.minimum_payment, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.description, bv.created_date, bv.bank_id, bv.reference_no, t.transaction_number, t.transaction_date, t.transaction_state FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE t.to_member_id = ? AND t.billing_id IS NOT NULL ORDER BY t.billing_id;",
					new Object[] { member.getId() }, new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView vav = new VARecordView();
							vav.setTicketID(rs.getString("ticket_id"));
							vav.setTransactionDate(rs.getTimestamp("transaction_date"));
							vav.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
							vav.setStatus(rs.getString("transaction_state"));
							vav.setId(rs.getString("id"));
							vav.setName(rs.getString("name"));
							vav.setReferenceNumber(rs.getString("reference_no"));
							vav.setPaymentCode(rs.getString("va_no"));

							if (rs.getString("transaction_state").equalsIgnoreCase("PROCESSED")) {
								vav.setStatus("PAID");
							} else {
								vav.setStatus(rs.getString("transaction_state"));
							}
							vav.setAmount(rs.getBigDecimal("amount"));
							vav.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
							vav.setExpiredAt(rs.getTimestamp("expired_date"));
							vav.setFullPayment(rs.getBoolean("fullpayment"));
							vav.setPersistent(rs.getBoolean("persistent"));
							vav.setCallbackURL(rs.getString("callback_url"));
							vav.setDescription(rs.getString("description"));
							vav.setCreatedDate(rs.getTimestamp("created_date"));
							vav.setBankID(rs.getInt("bank_id"));
							vav.setParentUsername(member.getUsername());
							return vav;
						}
					});
			return paidVA;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<VARecordView> loadVAPaid(LoadBillingStatusByMemberRequest req, Members member) {
		try {
			if (req.getEventID() == null || req.getEventID().equalsIgnoreCase("")) {
				List<VARecordView> paidVA = this.jdbcTemplate.query(
						"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, bv.billing_state, t.amount, bv.minimum_payment, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.description, bv.created_date, bv.bank_id, bv.reference_no, bv.subscribed, bv.membership_id, bv.billing_cycle, t.transaction_number, t.transaction_date, t.transaction_state FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE bv.subscribed = ? AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC LIMIT ?,?",
						new Object[] { req.isSubscribed(), member.getId(), req.getFromDate(), req.getToDate(),
								req.getCurrentPage(), req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setTicketID(rs.getString("ticket_id"));
								vav.setTransactionDate(rs.getTimestamp("transaction_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								vav.setStatus(rs.getString("transaction_state"));
								vav.setId(rs.getString("id"));
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_no"));
								vav.setPaymentCode(rs.getString("va_no"));

								if (rs.getString("transaction_state").equalsIgnoreCase("PROCESSED")) {
									vav.setStatus("PAID");
								} else {
									vav.setStatus(rs.getString("transaction_state"));
								}
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								vav.setExpiredAt(rs.getTimestamp("expired_date"));
								vav.setFullPayment(rs.getBoolean("fullpayment"));
								vav.setPersistent(rs.getBoolean("persistent"));
								vav.setCallbackURL(rs.getString("callback_url"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setBankID(rs.getInt("bank_id"));
								vav.setParentUsername(member.getUsername());
								vav.setSubscribed(req.isSubscribed());
								if (rs.getBoolean("subscribed")) {
									vav.setBillingCycle(rs.getInt("billing_cycle"));
									vav.setMembershipID(rs.getInt("membership_id"));
								}
								return vav;
							}
						});
				return paidVA;
			} else {
				List<VARecordView> paidVA = this.jdbcTemplate.query(
						"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, bv.billing_state, t.amount, bv.minimum_payment, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.description, bv.created_date, bv.bank_id, bv.reference_no, bv.subscribed, bv.membership_id, bv.billing_cycle, t.transaction_number, t.transaction_date, t.transaction_state FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE bv.event_id= ? AND bv.subscribed = ? AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC LIMIT ?,?",
						new Object[] { req.getEventID(), req.isSubscribed(), member.getId(), req.getFromDate(),
								req.getToDate(), req.getCurrentPage(), req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setTicketID(rs.getString("ticket_id"));
								vav.setTransactionDate(rs.getTimestamp("transaction_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								vav.setStatus(rs.getString("transaction_state"));
								vav.setId(rs.getString("id"));
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_no"));
								vav.setPaymentCode(rs.getString("va_no"));

								if (rs.getString("transaction_state").equalsIgnoreCase("PROCESSED")) {
									vav.setStatus("PAID");
								} else {
									vav.setStatus(rs.getString("transaction_state"));
								}
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								vav.setExpiredAt(rs.getTimestamp("expired_date"));
								vav.setFullPayment(rs.getBoolean("fullpayment"));
								vav.setPersistent(rs.getBoolean("persistent"));
								vav.setCallbackURL(rs.getString("callback_url"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setBankID(rs.getInt("bank_id"));
								vav.setParentUsername(member.getUsername());
								vav.setSubscribed(req.isSubscribed());
								if (rs.getBoolean("subscribed")) {
									vav.setBillingCycle(rs.getInt("billing_cycle"));
									vav.setMembershipID(rs.getInt("membership_id"));
								}
								return vav;
							}
						});
				return paidVA;
			}

		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<VARecordView> loadVAPaidByDate(LoadBillingStatusByMemberRequest req, Members member) {
		try {
			if (req.getEventID() == null || req.getEventID().equalsIgnoreCase("")) {
				List<VARecordView> paidVA = this.jdbcTemplate.query(
						"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, bv.billing_state, t.amount, bv.minimum_payment, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.description, bv.created_date, bv.bank_id, bv.reference_no, bv.subscribed, bv.membership_id, bv.billing_cycle, t.transaction_number, t.transaction_date, t.transaction_state FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE bv.subscribed = ? AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC;",
						new Object[] { req.isSubscribed(), member.getId(), req.getFromDate(), req.getToDate() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setTicketID(rs.getString("ticket_id"));
								vav.setTransactionDate(rs.getTimestamp("transaction_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								vav.setStatus(rs.getString("transaction_state"));
								vav.setId(rs.getString("id"));
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_no"));
								vav.setPaymentCode(rs.getString("va_no"));

								if (rs.getString("transaction_state").equalsIgnoreCase("PROCESSED")) {
									vav.setStatus("PAID");
								} else {
									vav.setStatus(rs.getString("transaction_state"));
								}
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								vav.setExpiredAt(rs.getTimestamp("expired_date"));
								vav.setFullPayment(rs.getBoolean("fullpayment"));
								vav.setPersistent(rs.getBoolean("persistent"));
								vav.setCallbackURL(rs.getString("callback_url"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setBankID(rs.getInt("bank_id"));
								vav.setParentUsername(member.getUsername());
								vav.setSubscribed(req.isSubscribed());
								if (rs.getBoolean("subscribed")) {
									vav.setBillingCycle(rs.getInt("billing_cycle"));
									vav.setMembershipID(rs.getInt("membership_id"));
								}
								return vav;
							}
						});
				return paidVA;
			} else {
				List<VARecordView> paidVA = this.jdbcTemplate.query(
						"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, bv.billing_state, t.amount, bv.minimum_payment, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.description, bv.created_date, bv.bank_id, bv.reference_no, bv.subscribed, bv.membership_id, bv.billing_cycle, t.transaction_number, t.transaction_date, t.transaction_state FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE bv.event_id = ? AND  bv.subscribed = ? AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC;",
						new Object[] { req.getEventID(), req.isSubscribed(), member.getId(), req.getFromDate(),
								req.getToDate() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView vav = new VARecordView();
								vav.setTicketID(rs.getString("ticket_id"));
								vav.setTransactionDate(rs.getTimestamp("transaction_date"));
								vav.setTransactionNumber(rs.getString("transaction_number"));
								vav.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								vav.setStatus(rs.getString("transaction_state"));
								vav.setId(rs.getString("id"));
								vav.setName(rs.getString("name"));
								vav.setReferenceNumber(rs.getString("reference_no"));
								vav.setPaymentCode(rs.getString("va_no"));

								if (rs.getString("transaction_state").equalsIgnoreCase("PROCESSED")) {
									vav.setStatus("PAID");
								} else {
									vav.setStatus(rs.getString("transaction_state"));
								}
								vav.setAmount(rs.getBigDecimal("amount"));
								vav.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								vav.setExpiredAt(rs.getTimestamp("expired_date"));
								vav.setFullPayment(rs.getBoolean("fullpayment"));
								vav.setPersistent(rs.getBoolean("persistent"));
								vav.setCallbackURL(rs.getString("callback_url"));
								vav.setDescription(rs.getString("description"));
								vav.setCreatedDate(rs.getTimestamp("created_date"));
								vav.setBankID(rs.getInt("bank_id"));
								vav.setParentUsername(member.getUsername());
								vav.setSubscribed(req.isSubscribed());
								if (rs.getBoolean("subscribed")) {
									vav.setBillingCycle(rs.getInt("billing_cycle"));
									vav.setMembershipID(rs.getInt("membership_id"));
								}
								return vav;
							}
						});
				return paidVA;
			}

		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<VARecordView> subscribedVA(RegisterVADoc va) {
		try {
			List<VARecordView> vaDetail = this.jdbcTemplate.query(
					"select amount, status from billing_status where billing_id = (select id from billing_va where va_no = ?) and month(created_date) between month(current_timestamp) and month(now() + interval 1 month);",
					new Object[] { va.getId() }, new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView sva = new VARecordView();
							sva.setAmount(rs.getBigDecimal("amount"));
							sva.setStatus(rs.getString("status"));
							return sva;
						}
					});
			return vaDetail;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateSubscribedVA(String trxState, Integer billingID, String traceNumber, String trxNumber) {
		this.jdbcTemplate.update(
				"update billing_status set status = ?, trace_number = ?, transaction_number = ? where status='UNPAID' and billing_id = ?",
				trxState, traceNumber, trxNumber, billingID);
	}

	public void registerEventStatus(CreateEventStatusRequest req, Members member) {
		this.jdbcTemplate.update(
				"insert into event_status (event_id, name, msisdn, email, reference_number, member_id, amount, status, channel_id, transaction_number, trace_number, description) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				req.getEventID(), req.getName(), req.getMsisdn(), req.getEmail(), req.getReferenceNumber(),
				member.getId(), req.getAmount(), req.getStatus(), req.getChannelID(), req.getTransactionNumber(),
				req.getTraceNumber(), req.getDescription());
	}

	public Integer countVAPaid(Integer memberID, String fromDate, String toDate) {
		int count = this.jdbcTemplate.queryForObject(
				"SELECT count(t.billing_id) FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ?",
				Integer.class, memberID, fromDate, toDate);
		return count;
	}

	public Integer countVAUnPaid(Integer memberID) {
		int count = this.jdbcTemplate.queryForObject(
				"select count(bv.id) from billing_va bv where bv.member_id=? and billing_state='ACTIVE' order by bv.id desc;",
				Integer.class, memberID);
		return count;
	}

	public void deleteVA(String ticketID) {
		this.jdbcTemplate.update("delete from billing_va where ticket_id=?", ticketID);
	}

	public Integer countVAReport(Integer memberID) {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from billing_va where member_id=?",
				Integer.class, memberID);
		return count;
	}

	public void updateStatusBillingVA(String traceNumber, String transactionNumber) {
		this.jdbcTemplate.update(
				"update transfers set transaction_state='PROCESSED' where transaction_state='PENDING' and (transaction_number=? or parent_id=?)",
				transactionNumber, transactionNumber);
		int billingID = this.jdbcTemplate.queryForObject(
				"select billing_id from transfers where transaction_number = ?", Integer.class, transactionNumber);
		if (billingID != 0) {
			this.jdbcTemplate.update(
					"update billing_status set status='PAID' where status='PENDING' and billing_id = ?", billingID);
		}
	}

	public void updateStatusBilling(String paymentCode) {
		this.jdbcTemplate.update(
				"update billing_va set billing_state='EXPIRED' where va_no = ? and billing_state='ACTIVE'",
				paymentCode);
	}

	public void updateBilling(RegisterVADoc vadoc) {
		this.jdbcTemplate.update(
				"update billing_va set amount=?, callback_url=?, name=?, persistent=?, expired_date=?, fullpayment=?, minimum_payment=?, email=?, msisdn=?, description=?, billing_state=? where ticket_id=?",
				vadoc.getAmount(), vadoc.getCallbackURL(), vadoc.getName(), vadoc.isPersistent(), vadoc.getExpiredAt(),
				vadoc.isFullPayment(), vadoc.getMinimumPayment(), vadoc.getEmail(), vadoc.getMsisdn(),
				vadoc.getDescription(), vadoc.getStatus(), vadoc.getTicketID());
	}

	public List<VARecordView> loadVANonSubscribed(LoadVAStatusByMemberRequest req, Members member) {
		try {
			if (req.getBillingStatus() == null | req.getBillingStatus().equalsIgnoreCase("")) {
				List<VARecordView> vaDetail = this.jdbcTemplate.query(
						"SELECT bv.ticket_id, bv.va_no, bv.name, t.amount, bv.minimum_payment, bv.fullpayment, bv.description, bv.created_date, bv.bank_id, bv.reference_no, t.transaction_number, t.transaction_date, t.transaction_state FROM transfers t \n"
								+ "JOIN billing_va bv ON bv.id = t.billing_id WHERE bv.event_id IS NULL AND bv.subscribed = false AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC LIMIT ?,?;",
						new Object[] { member.getId(), req.getFromDate(), req.getToDate(), req.getCurrentPage(),
								req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView sva = new VARecordView();
								sva.setTicketID(rs.getString("ticket_id"));
								sva.setPaymentCode(rs.getString("va_no"));
								sva.setName(rs.getString("name"));
								sva.setAmount(rs.getBigDecimal("amount"));
								sva.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								sva.setFullPayment(rs.getBoolean("fullpayment"));
								sva.setDescription(rs.getString("description"));
								sva.setBankID(rs.getInt("bank_id"));
								sva.setCreatedDate(rs.getTimestamp("created_date"));
								sva.setReferenceNumber(rs.getString("reference_no"));
								sva.setTransactionNumber(rs.getString("transaction_number"));
								sva.setTransactionDate(rs.getTimestamp("transaction_date"));
								sva.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								sva.setStatus(rs.getString("transaction_state"));
								if (rs.getString("transaction_state").equalsIgnoreCase("PROCESSED")) {
									sva.setStatus("PAID");
								} else {
									sva.setStatus(rs.getString("transaction_state"));
								}
								return sva;
							}
						});
				return vaDetail;
			} else {
				List<VARecordView> vaDetail = this.jdbcTemplate.query(
						"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, t.amount, bv.minimum_payment, bv.fullpayment, bv.description, bv.created_date, bv.bank_id, bv.reference_no, t.transaction_number, t.transaction_date, t.transaction_state FROM transfers t \n"
								+ "JOIN billing_va bv ON bv.id = t.billing_id WHERE t.transaction_state = ? AND bv.event_id IS NULL AND bv.subscribed = false AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC LIMIT ?,?;",
						new Object[] { req.getBillingStatus(), member.getId(), req.getFromDate(), req.getToDate(),
								req.getCurrentPage(), req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView sva = new VARecordView();
								sva.setTicketID(rs.getString("ticket_id"));
								sva.setPaymentCode(rs.getString("va_no"));
								sva.setName(rs.getString("name"));
								sva.setAmount(rs.getBigDecimal("amount"));
								sva.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								sva.setFullPayment(rs.getBoolean("fullpayment"));
								sva.setDescription(rs.getString("description"));
								sva.setBankID(rs.getInt("bank_id"));
								sva.setCreatedDate(rs.getTimestamp("created_date"));
								sva.setReferenceNumber(rs.getString("reference_no"));
								sva.setTransactionNumber(rs.getString("transaction_number"));
								sva.setTransactionDate(rs.getTimestamp("transaction_date"));
								sva.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								if (rs.getString("transaction_state").equalsIgnoreCase("PROCESSED")) {
									sva.setStatus("PAID");
								} else {
									sva.setStatus(rs.getString("transaction_state"));
								}
								return sva;
							}
						});
				return vaDetail;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countVANonSubscribed(LoadVAStatusByMemberRequest req, Members members) {
		int count = 0;
		if (req.getBillingStatus() == null || req.getBillingStatus().equalsIgnoreCase("")) {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(bv.id) FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE bv.event_id IS NULL AND bv.subscribed = false AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC;",
					Integer.class, members.getId(), req.getFromDate(), req.getToDate());
		} else {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(bv.id) FROM transfers t JOIN billing_va bv ON bv.id = t.billing_id WHERE t.transaction_state = ? AND bv.event_id IS NULL AND bv.subscribed = false AND t.to_member_id = ? AND t.billing_id IS NOT NULL AND t.transaction_date BETWEEN ? AND ? ORDER BY t.transaction_date DESC;",
					Integer.class, req.getBillingStatus(), members.getId(), req.getFromDate(), req.getToDate());
		}
		return count;
	}

	public List<VARecordView> loadVASubscribed(LoadVAStatusByMemberRequest req, Members member) {
		try {
			if (req.getBillingStatus() == null | req.getBillingStatus().equalsIgnoreCase("")) {
				List<VARecordView> vaDetail = this.jdbcTemplate.query(
						"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, bs.amount, bv.minimum_payment, bv.fullpayment, bv.description, bv.created_date, bv.bank_id, bv.reference_no, bs.transaction_number, bs.created_date as transaction_date, bs.status FROM billing_status bs\n"
								+ "JOIN billing_va bv ON bv.id = bs.billing_id \n"
								+ "WHERE bv.event_id IS NULL AND bv.subscribed = true AND bv.member_id = ? AND bs.created_date BETWEEN ? AND ? ORDER BY bs.created_date DESC LIMIT ?,?;",
						new Object[] { member.getId(), req.getFromDate(), req.getToDate(), req.getCurrentPage(),
								req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView sva = new VARecordView();
								sva.setTicketID(rs.getString("ticket_id"));
								sva.setPaymentCode(rs.getString("va_no"));
								sva.setName(rs.getString("name"));
								sva.setAmount(rs.getBigDecimal("amount"));
								sva.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								sva.setFullPayment(rs.getBoolean("fullpayment"));
								sva.setDescription(rs.getString("description"));
								sva.setBankID(rs.getInt("bank_id"));
								sva.setCreatedDate(rs.getTimestamp("created_date"));
								sva.setReferenceNumber(rs.getString("reference_no"));
								sva.setTransactionNumber(rs.getString("transaction_number"));
								sva.setTransactionDate(rs.getTimestamp("transaction_date"));
								sva.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								sva.setStatus(rs.getString("status"));
								if (rs.getString("status").equalsIgnoreCase("PROCESSED")) {
									sva.setStatus("PAID");
								} else {
									sva.setStatus(rs.getString("transaction_state"));
								}
								return sva;
							}
						});
				return vaDetail;
			} else {
				List<VARecordView> vaDetail = this.jdbcTemplate.query(
						"SELECT bv.id, bv.ticket_id, bv.va_no, bv.name, bs.amount, bv.minimum_payment, bv.fullpayment, bv.description, bv.created_date, bv.bank_id, bv.reference_no, bs.transaction_number, bs.created_date as transaction_date, bs.status FROM billing_status bs\n"
								+ "JOIN billing_va bv ON bv.id = bs.billing_id \n"
								+ "WHERE bs.status = ? AND bv.event_id IS NULL AND bv.subscribed = true AND bv.member_id = ? AND bs.created_date BETWEEN ? AND ? ORDER BY bs.created_date DESC LIMIT ?,?;",
						new Object[] { req.getBillingStatus(), member.getId(), req.getFromDate(), req.getToDate(),
								req.getCurrentPage(), req.getPageSize() },
						new RowMapper<VARecordView>() {
							public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
								VARecordView sva = new VARecordView();
								sva.setTicketID(rs.getString("ticket_id"));
								sva.setPaymentCode(rs.getString("va_no"));
								sva.setName(rs.getString("name"));
								sva.setAmount(rs.getBigDecimal("amount"));
								sva.setMinimumPayment(rs.getBigDecimal("minimum_payment"));
								sva.setFullPayment(rs.getBoolean("fullpayment"));
								sva.setDescription(rs.getString("description"));
								sva.setBankID(rs.getInt("bank_id"));
								sva.setCreatedDate(rs.getTimestamp("created_date"));
								sva.setReferenceNumber(rs.getString("reference_no"));
								sva.setTransactionNumber(rs.getString("transaction_number"));
								sva.setTransactionDate(rs.getTimestamp("transaction_date"));
								sva.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
								if (rs.getString("status").equalsIgnoreCase("PROCESSED")) {
									sva.setStatus("PAID");
								} else {
									sva.setStatus(rs.getString("status"));
								}
								return sva;
							}
						});
				return vaDetail;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countVASubscribed(LoadVAStatusByMemberRequest req, Members members) {
		int count = 0;
		if (req.getBillingStatus() == null || req.getBillingStatus().equalsIgnoreCase("")) {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(bv.id) FROM billing_status bs JOIN billing_va bv ON bv.id = bs.billing_id WHERE bv.event_id IS NULL AND bv.subscribed = true AND bv.member_id = ? AND bs.created_date BETWEEN ? AND ? ORDER BY bs.created_date DESC;",
					Integer.class, members.getId(), req.getFromDate(), req.getToDate());
		} else {
			count = this.jdbcTemplate.queryForObject(
					"SELECT count(bv.id) FROM billing_status bs JOIN billing_va bv ON bv.id = bs.billing_id WHERE bs.status = ? AND bv.event_id IS NULL AND bv.subscribed = true AND bv.member_id = ? AND bs.created_date BETWEEN ? AND ? ORDER BY bs.created_date DESC;",
					Integer.class, req.getBillingStatus(), members.getId(), req.getFromDate(), req.getToDate());
		}
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}