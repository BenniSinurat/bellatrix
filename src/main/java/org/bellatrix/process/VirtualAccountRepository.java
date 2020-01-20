package org.bellatrix.process;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.bellatrix.data.BillingStatus;
import org.bellatrix.data.Members;
import org.bellatrix.data.RegisterVADoc;
import org.bellatrix.data.VARecordView;
import org.bellatrix.data.VAStatusRecordView;
import org.bellatrix.data.VirtualAccounts;
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
				statement.setDate(5, Date.valueOf(expired.toLocalDate()));
				return statement;
			}
		}, holder);

		return holder.getKey().intValue();
	}

	public void registerBillingVA(String eventID, RegisterVADoc rva) {
		this.jdbcTemplate.update(
				"insert into billing_va (event_id, va_no, name, msisdn, reference_no, email, description, billing_state, member_id, amount, minimum_payment, expired_date, fullpayment, persistent, bank_id, callback_url, ticket_id) values (?, ?, ?, ?, ?, ?, ?, 'ACTIVE', ?, ?, ?, ?, ?, ?, ?, ?, ?);",
				eventID, rva.getId(), rva.getName(), rva.getMsisdn(), rva.getReferenceNumber(), rva.getEmail(),
				rva.getDescription(), rva.getMember().getId(), rva.getAmount(), rva.getMinimumPayment(), rva.getExpiredAt(), rva.isFullPayment(),
				rva.isPersistent(), rva.getBankID(), rva.getCallbackURL(), rva.getTicketID());
	}

	public List<VAStatusRecordView> loadVAByStatus(String username, Integer memberID, String startDate, String endDate,
			Integer currentPage, Integer pageSize) {
		try {
			List<VAStatusRecordView> va = this.jdbcTemplate.query(
					"select bv.id, bv.event_id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.bank_id, bv.description, bv.created_date, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.member_id=? and billing_state='ACTIVE' and bv.created_date between ? and ? order by bv.created_date desc limit ?,?;",
					new Object[] { memberID, startDate, endDate, currentPage, pageSize },
					new RowMapper<VAStatusRecordView>() {
						public VAStatusRecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VAStatusRecordView vav = new VAStatusRecordView();
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
							vav.setCreatedDate(rs.getDate("created_date"));
							vav.setBankID(rs.getInt("bank_id"));
							vav.setBankCode(rs.getString("bank_code"));
							vav.setBankName(rs.getString("bank_name"));
							vav.setParentUsername(username);
							return vav;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<VAStatusRecordView> loadVAReport(String username, Integer memberID) {
		try {
			List<VAStatusRecordView> va = this.jdbcTemplate.query(
					"select bv.id, bv.event_id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.bank_id, bv.description, bv.created_date, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.member_id=? and billing_state='ACTIVE' and bv.created_date order by bv.created_date desc;",
					new Object[] { memberID },
					new RowMapper<VAStatusRecordView>() {
						public VAStatusRecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VAStatusRecordView vav = new VAStatusRecordView();
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
							vav.setCreatedDate(rs.getDate("created_date"));
							vav.setBankID(rs.getInt("bank_id"));
							vav.setBankCode(rs.getString("bank_code"));
							vav.setBankName(rs.getString("bank_name"));
							vav.setParentUsername(username);
							return vav;
						}
					});
			return va;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BillingStatus> getVAPaymentStatus(Integer id) {
		try {
			List<BillingStatus> va = this.jdbcTemplate.query(
					"select reference_number, transaction_number, transaction_state, transaction_date from transfers where billing_id =?",
					new Object[] { id }, new RowMapper<BillingStatus>() {
						public BillingStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
							BillingStatus vav = new BillingStatus();
							vav.setReferenceNumber(rs.getString("reference_number"));
							vav.setTransactionDate(rs.getTimestamp("transaction_date"));
							vav.setStatus(rs.getString("transaction_state"));
							vav.setTransactionNumber(rs.getString("transaction_number"));
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
				logger.info("EXPIRED DATE: " + exd + "/ID: " + rva.getId() + "/Member ID: " + rva.getMember().getId()
						+ "/Reference No: " + rva.getReferenceNumber() + "/Name: " + rva.getName() + "/Email: "
						+ rva.getEmail() + "/Fullpayment: " + rva.isFullPayment() + "/Persistent: " + rva.isPersistent()
						+ "/Bank ID: " + rva.getBankID());
				id = this.jdbcTemplate.queryForObject(
						"select id from billing_va where va_no = ? and member_id = ? and reference_no = ? and name = ? and email = ? and billing_state = 'ACTIVE' and fullpayment = ? and persistent = ? and bank_id = ? and DATE(expired_date) = DATE(?) AND HOUR(expired_date) = HOUR(?) AND MINUTE(expired_date) = MINUTE(?)",
						String.class, rva.getId(), rva.getMember().getId(), rva.getReferenceNumber(), rva.getName(),
						rva.getEmail(), rva.isFullPayment(), rva.isPersistent(), rva.getBankID(), exd, exd, exd);
			} else {
				logger.info("ID: " + rva.getId() + "/Member ID: " + rva.getMember().getId() + "/Reference No: "
						+ rva.getReferenceNumber() + "/Name: " + rva.getName() + "/Email: " + rva.getEmail()
						+ "/Fullpayment: " + rva.isFullPayment() + "/Persistent: " + rva.isPersistent() + "/Bank ID: "
						+ rva.getBankID());
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
					"select bv.id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.bank_id, bv.minimum_payment, bv.description, bv.created_date, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.member_id=? and billing_state='ACTIVE' order by bv.id desc limit ?,?;",
					new Object[] { member.getId(), currentPage, pageSize }, new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView vav = new VARecordView();
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
							vav.setCreatedDate(rs.getDate("created_date"));
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
	
	public List<VARecordView> loadVAByMemberID(Members member) {
		try {
			List<VARecordView> vrv = this.jdbcTemplate.query(
					"select bv.id, bv.va_no, bv.name, bv.reference_no, bv.email, bv.billing_state, bv.amount, bv.expired_date, bv.fullpayment, bv.persistent, bv.callback_url, bv.bank_id, bv.minimum_payment, bv.description, bv.created_date, bb.bank_code, bb.name as bank_name from billing_va bv join bank_va bb on bb.id=bv.bank_id where bv.member_id=? and billing_state='ACTIVE' order by bv.id desc;",
					new Object[] { member.getId() }, new RowMapper<VARecordView>() {
						public VARecordView mapRow(ResultSet rs, int rowNum) throws SQLException {
							VARecordView vav = new VARecordView();
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
							vav.setCreatedDate(rs.getDate("created_date"));
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

	public void deleteVA(String paymentCode) {
		this.jdbcTemplate.update("update billing_va set billing_state='DELETED' where va_no=?", paymentCode);
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
	}
	
	public void updateStatusBilling(String paymentCode) {
		this.jdbcTemplate.update(
				"update billing_va set billing_state='EXPIRED' where va_no = ? and billing_state='ACTIVE'",
				paymentCode);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}