package org.bellatrix.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.bellatrix.data.AccountTransfer;
import org.bellatrix.data.BankTransfers;
import org.bellatrix.data.ScheduleTransfer;
import org.bellatrix.data.Status;
import org.bellatrix.services.CreateScheduleTransferRequest;
import org.bellatrix.services.RegisterAccountTransferRequest;
import org.bellatrix.services.UpdateScheduleTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.Statement;

@Component
@Repository
public class InterBankRepository {

	private JdbcTemplate jdbcTemplate;

	public List<Integer> loadBankTransferPermissionByGroup(Integer groupID) {
		List<Integer> bankIDs = this.jdbcTemplate.queryForList(
				"select bank_transfer_id from bank_transfer_permissions where group_id = ?", new Object[] { groupID },
				Integer.class);
		return bankIDs;
	}

	public BankTransfers loadBanksFromID(Integer id, Integer groupID) {
		try {
			BankTransfers banks = this.jdbcTemplate.queryForObject(
					"select a.id, a.bank_code, a.bank_name, a.enabled, a.swift_code, a.transfer_method, a. charging_code, a.pool_account_no, a.to_member_id, a.gateway_url, a.modified_date, b.transfer_type_id from  bank_transfers a inner join bank_transfer_permissions b on a.id = b.bank_transfer_id where a.id = ? and b.group_id = ? and a.enabled = true;",
					new Object[] { id, groupID }, new RowMapper<BankTransfers>() {
						public BankTransfers mapRow(ResultSet rs, int rowNum) throws SQLException {
							BankTransfers banks = new BankTransfers();
							banks.setId(rs.getInt("id"));
							banks.setBankCode(rs.getString("bank_code"));
							banks.setBankName(rs.getString("bank_name"));
							banks.setChargingCode(rs.getString("charging_code"));
							banks.setEnabled(rs.getBoolean("enabled"));
							banks.setGatewayURL(rs.getString("gateway_url"));
							banks.setModifiedDate(rs.getTimestamp("modified_date"));
							banks.setPoolAccountNo(rs.getString("pool_account_no"));
							banks.setSwiftCode(rs.getString("swift_code"));
							banks.setTransferMethod(rs.getString("transfer_method"));
							banks.setTransferTypeID(rs.getInt("transfer_type_id"));
							banks.setToMemberID(rs.getInt("to_member_id"));
							return banks;
						}
					});
			return banks;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<BankTransfers> loadBanksFromIDs(List<Integer> id, Integer groupID, Integer currentPage,
			Integer pageSize) {
		String sql = "select a.id, a.bank_code, a.bank_name, a.enabled, a.swift_code, a.transfer_method, a. charging_code, a.pool_account_no, b.transfer_type_id, a.to_member_id, a.gateway_url, a.modified_date from  bank_transfers a inner join bank_transfer_permissions b on a.id = b.bank_transfer_id where a.id in (:memberID) and b.group_id = "
				+ groupID + " and a.enabled = true LIMIT " + currentPage + "," + pageSize;
		Map<String, List<Integer>> paramMap = Collections.singletonMap("memberID", id);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		List<BankTransfers> banks = template.query(sql, paramMap, new RowMapper<BankTransfers>() {
			public BankTransfers mapRow(ResultSet rs, int rowNum) throws SQLException {
				BankTransfers banks = new BankTransfers();
				banks.setId(rs.getInt("id"));
				banks.setBankCode(rs.getString("bank_code"));
				banks.setBankName(rs.getString("bank_name"));
				banks.setChargingCode(rs.getString("charging_code"));
				banks.setEnabled(rs.getBoolean("enabled"));
				banks.setGatewayURL(rs.getString("gateway_url"));
				banks.setModifiedDate(rs.getTimestamp("modified_date"));
				banks.setPoolAccountNo(rs.getString("pool_account_no"));
				banks.setSwiftCode(rs.getString("swift_code"));
				banks.setTransferMethod(rs.getString("transfer_method"));
				banks.setTransferTypeID(rs.getInt("transfer_type_id"));
				banks.setToMemberID(rs.getInt("to_member_id"));
				return banks;
			}
		});
		return banks;
	}

	public List<AccountTransfer> loadBankAccountListByMember(Integer memberID, Integer currentPage, Integer pageSize) {
		try {
			List<AccountTransfer> accTransfer = this.jdbcTemplate.query(
					"select * from bank_account inner join bank_transfers on bank_account.bank_transfers_id = bank_transfers.id where member_id = ? limit ?,?",
					new Object[] { memberID, currentPage, pageSize }, new RowMapper<AccountTransfer>() {
						public AccountTransfer mapRow(ResultSet rs, int rowNum) throws SQLException {
							AccountTransfer accTransfer = new AccountTransfer();
							accTransfer.setAccountName(rs.getString("account_name"));
							accTransfer.setAccountNo(rs.getString("account_no"));
							accTransfer.setCreatedDate(rs.getTimestamp("created_date"));
							accTransfer.setDescription(rs.getString("description"));
							accTransfer.setId(rs.getInt("id"));
							accTransfer.setBankCode(rs.getString("bank_code"));
							accTransfer.setBankName(rs.getString("bank_name"));
							accTransfer.setBankID(rs.getInt("bank_transfers_id"));
							return accTransfer;
						}
					});
			return accTransfer;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<AccountTransfer> loadBankAccountListByNo(Integer memberID, String accountNo) {
		try {
			List<AccountTransfer> accTransfer = this.jdbcTemplate.query(
					"select * from bank_account inner join bank_transfers on bank_account.bank_transfers_id = bank_transfers.id where bank_account.member_id = ? and bank_account.account_no = ?",
					new Object[] { memberID, accountNo }, new RowMapper<AccountTransfer>() {
						public AccountTransfer mapRow(ResultSet rs, int rowNum) throws SQLException {
							AccountTransfer accTransfer = new AccountTransfer();
							accTransfer.setAccountName(rs.getString("account_name"));
							accTransfer.setAccountNo(rs.getString("account_no"));
							accTransfer.setCreatedDate(rs.getTimestamp("created_date"));
							accTransfer.setDescription(rs.getString("description"));
							accTransfer.setId(rs.getInt("id"));
							accTransfer.setBankCode(rs.getString("bank_code"));
							accTransfer.setBankName(rs.getString("bank_name"));
							accTransfer.setBankID(rs.getInt("bank_transfers_id"));
							return accTransfer;
						}
					});
			return accTransfer;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public BankTransfers validateBankAccountNoByMember(Integer memberID, String accountNo) {
		try {
			BankTransfers banks = this.jdbcTemplate.queryForObject(
					"select a.id, b.transfer_type_id, (select username from members where id = b.to_member_id) as to_member, b.bank_name from bank_account a inner join bank_transfers b on a.bank_transfers_id = b.id where a.member_id = ? and account_no = ?;",
					new Object[] { memberID, accountNo }, new RowMapper<BankTransfers>() {
						public BankTransfers mapRow(ResultSet rs, int rowNum) throws SQLException {
							BankTransfers banks = new BankTransfers();
							banks.setId(rs.getInt("id"));
							banks.setBankName(rs.getString("bank_name"));
							banks.setTransferTypeID(rs.getInt("transfer_type_id"));
							banks.setToUsername(rs.getString("to_member"));
							return banks;
						}
					});
			return banks;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public String validateMemberBankAccountNo(Integer bankID, Integer memberID, String accountNo) {
		try {
			String name = this.jdbcTemplate.queryForObject(
					"select account_name from bank_account where bank_transfers_id = ? and member_id = ? and account_no = ?",
					new Object[] { bankID, memberID, accountNo }, String.class);
			return name;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	public void insertBankAccount(Integer fromMemberID, RegisterAccountTransferRequest req) {
		this.jdbcTemplate.update(
				"insert into bank_account (bank_transfers_id, member_id, account_no, account_name, description) values(?,?,?,?,?);",
				req.getBankID(), fromMemberID, req.getAccountNo(), req.getAccountName(), req.getDescription());
	}

	public void removeBankAccount(Integer id, Integer memberID) throws Exception {
		int affected = this.jdbcTemplate.update("delete from bank_account where id = ? and member_id = ?",
				new Object[] { id, memberID });
		if (affected == 0) {
			throw new Exception(String.valueOf(Status.ACCOUNT_NOT_FOUND));
		}
	}

	public Integer countTotalbankAccounts(Integer memberID) {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from bank_account where member_id = ?",
				Integer.class, memberID);
		return count;
	}

	@Transactional
	public void createScheduleTransfer(CreateScheduleTransferRequest req) {
		final CreateScheduleTransferRequest transfers = req;
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into schedule_transfer (from_member_id, transfer_type_id, bank_id, account_no, account_name, schedule_date, enabled) values (?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, transfers.getFromMemberID());
				statement.setInt(2, transfers.getTransferTypeID());
				statement.setInt(3, transfers.getBankID());
				statement.setString(4, transfers.getAccountNo());
				statement.setString(5, transfers.getAccountName());
				if (transfers.getScheduleDate() != null) {
					statement.setTimestamp(6, Timestamp.valueOf(transfers.getScheduleDate()));
				} else {
					statement.setTimestamp(6, null);
				}
				statement.setBoolean(7, transfers.isEnabled());
				return statement;
			}
		});
	}

	@Transactional
	public void updateScheduleTransfer(UpdateScheduleTransfer req) {
		final UpdateScheduleTransfer transfer = req;
		jdbcTemplate.update(
				"update schedule_transfer set from_member_id = ?, transfer_type_id = ?, bank_id = ?, account_no = ?, account_name = ?, schedule_date = ?, enabled = ? where id = ?",
				transfer.getFromMemberID(), transfer.getTransferTypeID(), transfer.getBankID(), transfer.getAccountNo(),
				transfer.getAccountName(), Timestamp.valueOf(transfer.getScheduleDate()), transfer.isEnabled(), transfer.getId());
	}

	public List<ScheduleTransfer> findScheduleTransfer(String byField, Object id) {
		try {
			List<ScheduleTransfer> transfers = this.jdbcTemplate.query(
					"select * from schedule_transfer where " + byField + " = ?", new Object[] { id },
					new RowMapper<ScheduleTransfer>() {
						public ScheduleTransfer mapRow(ResultSet rs, int rowNum) throws SQLException {
							ScheduleTransfer transfers = new ScheduleTransfer();
							transfers.setAccountName(rs.getString("account_name"));
							transfers.setAccountNo(rs.getString("account_no"));
							transfers.setBankID(rs.getInt("bank_id"));
							transfers.setFromMemberID(rs.getInt("from_member_id"));
							transfers.setId(rs.getInt("id"));
							transfers.setTransferTypeID(rs.getInt("transfer_type_id"));
							transfers.setEnabled(rs.getBoolean("enabled"));
							transfers.setScheduleDate(rs.getString("schedule_date"));
							return transfers;
						}
					});
			return transfers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public ScheduleTransfer findOneScheduleTransfer(String byField, Object id) {
		try {
			ScheduleTransfer transfers = this.jdbcTemplate.queryForObject(
					"select *  from schedule_transfer where " + byField + " = ?", new Object[] { id },
					new RowMapper<ScheduleTransfer>() {
						public ScheduleTransfer mapRow(ResultSet rs, int rowNum) throws SQLException {
							ScheduleTransfer transfers = new ScheduleTransfer();
							transfers.setAccountName(rs.getString("account_name"));
							transfers.setAccountNo(rs.getString("account_no"));
							transfers.setBankID(rs.getInt("bank_id"));
							transfers.setFromMemberID(rs.getInt("from_member_id"));
							transfers.setId(rs.getInt("id"));
							transfers.setTransferTypeID(rs.getInt("transfer_type_id"));
							transfers.setEnabled(rs.getBoolean("enabled"));
							transfers.setScheduleDate(rs.getString("schedule_date"));
							return transfers;
						}
					});
			return transfers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void deleteScheduleTransfer(Integer id) throws Exception {
		int affected = this.jdbcTemplate.update("delete from schedule_transfer where id = ?", new Object[] { id });
		if (affected == 0) {
			throw new Exception(String.valueOf(Status.SCHEDULED_TRANSFER_NOT_FOUND));
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
