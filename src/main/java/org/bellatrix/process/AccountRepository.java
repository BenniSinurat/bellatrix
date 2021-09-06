package org.bellatrix.process;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.bellatrix.data.AccountPermissions;
import org.bellatrix.data.Accounts;
import org.bellatrix.data.Currencies;
import org.bellatrix.data.Groups;
import org.bellatrix.data.Transfers;
import org.bellatrix.services.LoadAccountsByGroupsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class AccountRepository {

	private JdbcTemplate jdbcTemplate;

	public void createAccount(String name, String description, boolean system, Integer currencyID) {
		jdbcTemplate.update("insert into accounts (name, description, system_account, currency_id) values (?, ?, ?, ?)", name,
				description, system, currencyID);
	}

	public void updateAccount(Integer id,Integer currencyID, String name, String description, boolean system) {
		this.jdbcTemplate.update("update  accounts set name = ?, description = ?, system_account = ?, currency_id = ? where id = ?",
				new Object[] { name, description, system, currencyID, id });
	}

	public void addAccountPermission(Integer id, Integer groupID, BigDecimal credit_limit,
			BigDecimal upper_credit_limit, BigDecimal lower_credit_limit) {
		jdbcTemplate.update(
				"insert into account_permissions (account_id, group_id, credit_limit, upper_credit_limit, lower_credit_limit) values (?, ?, ?, ?, ?)",
				id, groupID, credit_limit, upper_credit_limit, lower_credit_limit);
	}

	public void updateAccountPermission(Integer id, BigDecimal credit_limit, BigDecimal upper_credit_limit,
			BigDecimal lower_credit_limit) {
		this.jdbcTemplate.update(
				"update  account_permissions set credit_limit = ?, upper_credit_limit = ?, lower_credit_limit= ? where id = ?",
				new Object[] { credit_limit, upper_credit_limit, lower_credit_limit, id });
	}

	public void deleteAccountPermission(Integer id) {
		this.jdbcTemplate.update("delete from account_permissions where id = ?", new Object[] { id });
	}

	public void deleteAccountPermission(Integer id, Integer groupID) {
		this.jdbcTemplate.update("delete from account_permissions where account_id = ? and group_id = ?", new Object[] { id, groupID });
	}

	public List<Accounts> loadAllAccounts(Integer currentPage, Integer pageSize) {
		try {
			List<Accounts> accounts = this.jdbcTemplate.query("select * from accounts LIMIT ?, ?", new Object[] { currentPage, pageSize },
					new RowMapper<Accounts>() {
						public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accounts accounts = new Accounts();
							accounts.setId(rs.getInt("id"));
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("currency_id"));
							accounts.setCurrency(currency);
							accounts.setName(rs.getString("name"));
							accounts.setDescription(rs.getString("description"));
							accounts.setSystemAccount(rs.getBoolean("system_account"));
							accounts.setCreatedDate(rs.getTimestamp("created_date"));
							accounts.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return accounts;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Accounts> loadAccountsByGroups(LoadAccountsByGroupsRequest req) {
		try {
			List<Accounts> accounts = this.jdbcTemplate.query(
					"select c.id, c.name, c.description, c.system_account, c.currency_id, a.credit_limit, a.upper_credit_limit, a.lower_credit_limit, c.created_date from account_permissions a inner join groups b on a.group_id = b.id inner join accounts c on a.account_id = c.id where b.id = ?",
					new Object[] { req.getGroupID() }, new RowMapper<Accounts>() {
						public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accounts accounts = new Accounts();
							accounts.setId(rs.getInt("id"));
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("currency_id"));
							accounts.setCurrency(currency);
							accounts.setName(rs.getString("name"));
							accounts.setDescription(rs.getString("description"));
							accounts.setSystemAccount(rs.getBoolean("system_account"));
							accounts.setCreditLimit(rs.getBigDecimal("credit_limit"));
							accounts.setUpperCreditLimit(rs.getBigDecimal("upper_credit_limit"));
							accounts.setLowerCreditLimit(rs.getBigDecimal("lower_credit_limit"));
							accounts.setCreatedDate(rs.getTimestamp("created_date"));
							accounts.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return accounts;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<AccountPermissions> loadGroupPermissionByAccounts(Integer accountID) {
		try {
			List<AccountPermissions> accounts = this.jdbcTemplate.query(
					"select p.id, p.account_id, p.group_id, g.name, p.credit_limit, p.upper_credit_limit, p.lower_credit_limit from account_permissions p inner join groups g on p.group_id = g.id where p.account_id = ?;",
					new Object[] { accountID }, new RowMapper<AccountPermissions>() {
						public AccountPermissions mapRow(ResultSet rs, int rowNum) throws SQLException {
							AccountPermissions accountPermissions = new AccountPermissions();
							accountPermissions.setId(rs.getInt("id"));
							Accounts accounts = new Accounts();
							Groups group = new Groups();
							group.setId(rs.getInt("group_id"));
							group.setName(rs.getString("name"));
							accounts.setGroup(group);
							accounts.setId(rs.getInt("account_id"));
							accounts.setCreditLimit(rs.getBigDecimal("credit_limit"));
							accounts.setUpperCreditLimit(rs.getBigDecimal("upper_credit_limit"));
							accounts.setLowerCreditLimit(rs.getBigDecimal("lower_credit_limit"));
							accountPermissions.setAccount(accounts);
							return accountPermissions;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<AccountPermissions> loadGroupPermissionByID(Integer id) {
		try {
			List<AccountPermissions> accounts = this.jdbcTemplate.query(
					"select p.id, p.account_id, p.group_id, g.name, p.credit_limit, p.upper_credit_limit, p.lower_credit_limit from account_permissions p inner join groups g on p.group_id = g.id where p.id = ?;",
					new Object[] { id }, new RowMapper<AccountPermissions>() {
						public AccountPermissions mapRow(ResultSet rs, int rowNum) throws SQLException {
							AccountPermissions accountPermissions = new AccountPermissions();
							accountPermissions.setId(rs.getInt("id"));
							Accounts accounts = new Accounts();
							Groups group = new Groups();
							group.setId(rs.getInt("group_id"));
							group.setName(rs.getString("name"));
							accounts.setGroup(group);
							accounts.setId(rs.getInt("account_id"));
							accounts.setCreditLimit(rs.getBigDecimal("credit_limit"));
							accounts.setUpperCreditLimit(rs.getBigDecimal("upper_credit_limit"));
							accounts.setLowerCreditLimit(rs.getBigDecimal("lower_credit_limit"));
							accountPermissions.setAccount(accounts);
							return accountPermissions;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Accounts loadAccountsByID(Integer accountID) {
		try {
			Accounts accounts = this.jdbcTemplate.queryForObject(
					"select a.id, a.name, a.description, a.system_account, a.currency_id, a.created_date, b.name as currency_name from accounts a inner join currency b on a.currency_id = b.id where a.id = ?",
					new Object[] { accountID }, new RowMapper<Accounts>() {
						public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accounts accounts = new Accounts();
							accounts.setId(rs.getInt("id"));
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("currency_id"));
							currency.setName(rs.getString("currency_name"));
							accounts.setCurrency(currency);
							accounts.setName(rs.getString("name"));
							accounts.setDescription(rs.getString("description"));
							accounts.setSystemAccount(rs.getBoolean("system_account"));
							accounts.setCreatedDate(rs.getTimestamp("created_date"));
							accounts.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return accounts;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Accounts loadAccountsByID(Integer accountID, Integer groupID) {
		try {
			Accounts accounts = this.jdbcTemplate.queryForObject(
					"select c.id, c.name, c.description, c.system_account, a.credit_limit, a.upper_credit_limit, a.lower_credit_limit, c.currency_id, c.created_date, b.name as currency_name from account_permissions a inner join accounts c on a.account_id = c.id inner join currency b on c.currency_id = b.id where c.id = ? and a.group_id = ?",
					new Object[] { accountID, groupID }, new RowMapper<Accounts>() {
						public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accounts accounts = new Accounts();
							accounts.setId(rs.getInt("id"));
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("currency_id"));
							currency.setName(rs.getString("currency_name"));
							accounts.setCurrency(currency);
							accounts.setName(rs.getString("name"));
							accounts.setDescription(rs.getString("description"));
							accounts.setSystemAccount(rs.getBoolean("system_account"));
							accounts.setCreditLimit(rs.getBigDecimal("credit_limit"));
							accounts.setUpperCreditLimit(rs.getBigDecimal("upper_credit_limit"));
							accounts.setLowerCreditLimit(rs.getBigDecimal("lower_credit_limit"));
							accounts.setCreatedDate(rs.getTimestamp("created_date"));
							accounts.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return accounts;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Accounts loadAccountByID(Integer accountID) {
		try {
			Accounts accounts = this.jdbcTemplate.queryForObject(
					"select c.id, c.name, c.description, c.system_account, c.currency_id, c.created_date, b.name as currency_name from accounts c inner join currency b on c.currency_id = b.id where c.id = ?",
					new Object[] { accountID }, new RowMapper<Accounts>() {
						public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accounts accounts = new Accounts();
							accounts.setId(rs.getInt("id"));
							Currencies currency = new Currencies();
							currency.setId(rs.getInt("currency_id"));
							currency.setName(rs.getString("currency_name"));
							accounts.setCurrency(currency);
							accounts.setName(rs.getString("name"));
							accounts.setDescription(rs.getString("description"));
							accounts.setSystemAccount(rs.getBoolean("system_account"));
							accounts.setCreatedDate(rs.getTimestamp("created_date"));
							accounts.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return accounts;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Accounts> loadAccounts(Integer currentPage, Integer pageSize) {
		try {
			List<Accounts> accounts = this.jdbcTemplate.query(
					"select c.id, c.name, c.description, c.system_account, a.credit_limit, a.upper_credit_limit, a.lower_credit_limit, c.created_date from account_permissions a inner join groups b on a.group_id = b.id inner join accounts c on a.account_id = c.id LIMIT ?,?;",
					new Object[] { currentPage, pageSize }, new RowMapper<Accounts>() {
						public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accounts accounts = new Accounts();
							accounts.setId(rs.getInt("id"));
							accounts.setName(rs.getString("name"));
							accounts.setDescription(rs.getString("description"));
							accounts.setSystemAccount(rs.getBoolean("system_account"));
							accounts.setCreditLimit(rs.getBigDecimal("credit_limit"));
							accounts.setUpperCreditLimit(rs.getBigDecimal("upper_credit_limit"));
							accounts.setLowerCreditLimit(rs.getBigDecimal("lower_credit_limit"));
							accounts.setCreatedDate(rs.getTimestamp("created_date"));
							accounts.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return accounts;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Accounts> listGroupByAccountID(Integer accountID) {
		try {
			List<Accounts> accounts = this.jdbcTemplate.query(
					"select a.id, a.group_id, b.name, a.credit_limit, a.upper_credit_limit, a.lower_credit_limit from account_permissions a join groups b on a.group_id = b.id where account_id = ?",
					new Object[] { accountID }, new RowMapper<Accounts>() {
						public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accounts accounts = new Accounts();
							accounts.setId(rs.getInt("id"));
							accounts.setName(rs.getString("name"));
							accounts.setCreditLimit(rs.getBigDecimal("credit_limit"));
							accounts.setUpperCreditLimit(rs.getBigDecimal("upper_credit_limit"));
							accounts.setLowerCreditLimit(rs.getBigDecimal("lower_credit_limit"));
							return accounts;
						}
					});
			return accounts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public BigDecimal loadBalanceInquiry(String username, Integer accountID) {
		try {
			BigDecimal balance = jdbcTemplate.queryForObject(
					"select sum(journal) as balance from (select sum(-amount) as journal from transfers join members on members.id = transfers.from_member_id where members.username = ? and transfers.from_account_id = ? and transfers.transaction_state = 'PROCESSED' and transfers.charged_back = false union all select sum(amount) as journal from transfers join members on members.id = transfers.to_member_id where members.username = ? and transfers.to_account_id = ? and transfers.transaction_state = 'PROCESSED'  and transfers.charged_back = false) t1",
					new Object[] { username, accountID, username, accountID }, BigDecimal.class);
			if (balance == null) {
				return BigDecimal.ZERO;
			}
			return balance;
		} catch (EmptyResultDataAccessException e) {
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal loadReservedAmount(String username, Integer accountID) {
		try {
			BigDecimal balance = jdbcTemplate.queryForObject(
					"select sum(-amount) as journal from transfers join members on members.id = transfers.from_member_id where members.username = ? and transfers.from_account_id = ? and transfers.transaction_state = 'PENDING';",
					new Object[] { username, accountID }, BigDecimal.class);
			if (balance == null) {
				return BigDecimal.ZERO;
			}
			return balance;
		} catch (EmptyResultDataAccessException e) {
			return BigDecimal.ZERO;
		}
	}

	public BigDecimal loadUpperCreditLimitBalance(String username, Integer accountID) {
		try {
			/**BigDecimal balance = jdbcTemplate.queryForObject(
					"select sum(amount) as journal from transfers join members on members.id = transfers.to_member_id where members.username = ? and transfers.to_account_id = ? and transfers.charged_back = false and YEAR(transaction_date) = YEAR(NOW()) AND MONTH(transaction_date)=MONTH(NOW());",
					new Object[] { username, accountID }, BigDecimal.class);*/
			BigDecimal balance = jdbcTemplate.queryForObject(
					"select sum(amount) as journal from transfers join members on members.id = transfers.from_member_id where members.username = ? and transfers.from_account_id = ? and transfers.charged_back = false and YEAR(transaction_date) = YEAR(NOW()) AND MONTH(transaction_date)=MONTH(NOW());",
					new Object[] { username, accountID }, BigDecimal.class);
			if (balance == null) {
				return BigDecimal.ZERO;
			}
			return balance;
		} catch (EmptyResultDataAccessException e) {
			return BigDecimal.ZERO;
		}
	}

	public List<Transfers> loadTransferFromUsername(String username, Integer accountID, Integer currentPage,
			Integer pageSize, String fromDate, String toDate, String orderBy, String orderType) {
		try {
			List<Transfers> transfer = this.jdbcTemplate.query(
					"select -a.amount as amount, a.id, a.transfer_type_id, t.name, a.from_account_id, a.to_account_id, a.from_member_id, a.to_member_id, b.username as from_username, (select username from members where id=a.to_member_id) as to_username, b.name as from_name, (select name from members where id=a.to_member_id) as to_name, a.trace_number, a.transaction_number, a.description, a.transaction_state, a.transaction_date, a.modified_date, a.parent_id, a.charged_back, a.custom_field, a.reverse_by, (select username from members where id=a.reverse_by) as reverse_username, (select name from members where id=a.reverse_by) as reverse_name, a.reference_number, a.remark from transfers a join members b on a.from_member_id = b.id join transfer_types t on a.transfer_type_id = t.id where b.username=? and a.from_account_id=? and a.transaction_date between ? and ? union all select c.amount, c.id, c.transfer_type_id, r.name, c.from_account_id, c.to_account_id, c.from_member_id, c.to_member_id, (select username from members where id=c.from_member_id) as from_username, b.username as to_username, (select name from members where id=c.from_member_id) as from_name, b.name as to_name, c.trace_number, c.transaction_number, c.description, c.transaction_state, c.transaction_date, c.modified_date, c.parent_id, c.charged_back, c.custom_field, c.reverse_by, (select username from members where id=c.reverse_by) as reverse_username, (select name from members where id=c.reverse_by) as reverse_name, c.reference_number, c.remark from transfers c join members b on c.to_member_id = b.id join transfer_types r on c.transfer_type_id=r.id where b.username=? and c.to_account_id=? and c.transaction_date between ? and ? order by "
							+ orderBy + " " + orderType + " limit ?,?",
					new Object[] { username, accountID, fromDate, toDate, username, accountID, fromDate, toDate,
							currentPage, pageSize },
					new RowMapper<Transfers>() {
						public Transfers mapRow(ResultSet rs, int rowNum) throws SQLException {
							Transfers transfer = new Transfers();
							transfer.setId(rs.getInt("id"));
							transfer.setTransferTypeID(rs.getInt("transfer_type_id"));
							transfer.setFromAccountID(rs.getInt("from_account_id"));
							transfer.setToAccountID(rs.getInt("to_account_id"));
							transfer.setName(rs.getString("name"));
							transfer.setFromMemberID(rs.getInt("from_member_id"));
							transfer.setToMemberID(rs.getInt("to_member_id"));
							transfer.setFromUsername(rs.getString("from_username"));
							transfer.setToUsername(rs.getString("to_username"));
							transfer.setFromName(rs.getString("from_name"));
							transfer.setToName(rs.getString("to_name"));
							transfer.setAmount(rs.getBigDecimal("amount"));
							transfer.setTraceNumber(rs.getString("trace_number"));
							transfer.setTransactionNumber(rs.getString("transaction_number"));
							transfer.setParentID(rs.getString("parent_id"));
							transfer.setTransactionDate(rs.getTimestamp("transaction_date"));
							transfer.setFormattedTransactionDate(Utils.formatDate(rs.getTimestamp("transaction_date")));
							transfer.setDescription(rs.getString("description"));
							transfer.setChargedBack(rs.getBoolean("charged_back"));
							transfer.setModifiedDate(rs.getTimestamp("modified_date"));
							transfer.setTransactionState(rs.getString("transaction_state"));
							transfer.setReverseByID(rs.getInt("reverse_by"));
							transfer.setReverseByName(rs.getString("reverse_name"));
							transfer.setReverseByUsername(rs.getString("reverse_username"));
							transfer.setCustomField(rs.getBoolean("custom_field"));
							transfer.setReferenceNumber(rs.getString("reference_number"));
							transfer.setRemark(rs.getString("remark"));
							return transfer;
						}
					});

			return transfer;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalTransaction(Integer memberID, Integer accountID, String startDate, String endDate) {
		int count = this.jdbcTemplate.queryForObject(
				"select sum(total) from (select  count(*) as total from transfers where transfers.from_member_id = ? and transfers.from_account_id = ? and transfers.transaction_date between ? and ? union all select  count(*) as total from transfers where transfers.to_member_id = ? and transfers.to_account_id = ? and transfers.transaction_date between ? and ?) as sub;",
				Integer.class, memberID, accountID, startDate, endDate, memberID, accountID, startDate, endDate);
		return count;
	}

	public Integer countTotalRecords() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from transfers;", Integer.class);
		return count;
	}

	public Integer countTotalAccount() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) as total from accounts", Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
