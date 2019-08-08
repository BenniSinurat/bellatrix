package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.bellatrix.data.AccessStatus;
import org.bellatrix.data.AccessType;
import org.bellatrix.data.Accesses;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.CreateCredentialRequest;
import org.bellatrix.services.CredentialResponse;
import org.bellatrix.services.ValidateCredentialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class AccessRepository {

	private JdbcTemplate jdbcTemplate;

	public void createCredential(CreateCredentialRequest req) {
		jdbcTemplate.update(
				"insert into access (member_id, access_type_id, expired, expired_date, blocked, credential) values (?,?,?,?,?,MD5(?))",
				req.getMemberID(), req.getAccessTypeID(), req.isExpired(), req.getExpiredDate(), req.isBlocked(),
				req.getCredential());
	}

	public Accesses validateCredential(ValidateCredentialRequest req) {
		try {
			Accesses access = this.jdbcTemplate.queryForObject(
					"select a.id, a.access_type_id, a.member_id from access a join members b on  a.member_id = b.id where b.username = ? and a.access_type_id = ? and a.credential = MD5(?)",
					new Object[] { req.getUsername(), req.getAccessTypeID(), req.getCredential() },
					new RowMapper<Accesses>() {
						public Accesses mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accesses access = new Accesses();
							access.setId(rs.getInt("id"));
							access.setAccessTypeID(rs.getInt("access_type_id"));
							access.setMemberID(rs.getInt("member_id"));
							return access;
						}
					});
			return access;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public CredentialResponse getSecretFromMemberID(Integer memberID, Integer accessTypeID)
			throws TransactionException {
		try {
			CredentialResponse credential = this.jdbcTemplate.queryForObject(
					"select a.credential, a.validate, b.id, b.name, b.internal_name from access a inner join access_types b on a.access_type_id = b.id where member_id = ? and access_type_id = ?;",
					new Object[] { memberID, accessTypeID }, new RowMapper<CredentialResponse>() {
						public CredentialResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
							CredentialResponse credential = new CredentialResponse();
							credential.setId(rs.getInt("id"));
							credential.setCredential(rs.getString("credential"));
							credential.setInternalName(rs.getString("internal_name"));
							credential.setName(rs.getString("name"));
							credential.setValidate(rs.getBoolean("validate"));
							return credential;
						}
					});
			return credential;
		} catch (EmptyResultDataAccessException e) {
			throw new TransactionException(String.valueOf(Status.INVALID));
		}

	}

	public Integer validateBruteForce(Integer memberID, Integer accessTypeID, Integer value) {
		try {
			int attemptsCount = this.jdbcTemplate.queryForObject(
					"select id from access_attempts where member_id = ? and access_type_id = ? and TIMESTAMPDIFF(SECOND,access_attempts.date,NOW()) < ? order by id desc limit 1; ",
					Integer.class, memberID, accessTypeID, value);
			return attemptsCount;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	public Timestamp validateBruteForce(Integer memberID, Integer accessTypeID) {
		try {
			Timestamp lastAttempts = this.jdbcTemplate.queryForObject(
					"select date from access_attempts where member_id = ? and access_type_id = ? order by id desc limit 1; ",
					Timestamp.class, memberID, accessTypeID);
			return lastAttempts;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countFailedAccessAttempts(Integer memberID, Integer accessTypeID) {
		int attemptsCount = this.jdbcTemplate.queryForObject(
				"select count(id) from access_attempts where member_id = ? and access_type_id = ?", Integer.class,
				memberID, accessTypeID);
		return attemptsCount;
	}

	public void flagAccessAttempts(Integer memberID, Integer accessTypeID) {
		this.jdbcTemplate.update("insert into access_attempts (member_id, access_type_id) values (?, ?)", memberID,
				accessTypeID);
	}

	public void blockCredential(Integer memberID, Integer accessTypeID) {
		this.jdbcTemplate.update("update access set blocked = true where member_id = ? and access_type_id = ?",
				memberID, accessTypeID);
	}

	public AccessStatus accessStatus(String username, Integer accessTypeID) {
		try {
			AccessStatus access = this.jdbcTemplate.queryForObject(
					"select a.blocked, a.expired, a.expired_date, a.access_type_id from access a join members b on a.member_id = b.id where b.username = ? and a.access_type_id = ?",
					new Object[] { username, accessTypeID }, new RowMapper<AccessStatus>() {
						public AccessStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
							AccessStatus access = new AccessStatus();
							access.setBlocked(rs.getBoolean("blocked"));
							access.setExpired(rs.getBoolean("expired"));
							access.setExpiredDate(rs.getDate("expired_date"));
							access.setAccessTypeID(rs.getInt("access_type_id"));
							return access;
						}
					});
			return access;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	public Accesses loadCredentialByUsername(String username, Integer accessID) {
		try {
			Accesses access = this.jdbcTemplate.queryForObject(
					"select a.id, a.access_type_id, a.member_id, a.blocked, a.expired, a.expired_date, a.credential from access a join members b on  a.member_id = b.id where b.username = ? and a.access_type_id = ?",
					new Object[] { username, accessID }, new RowMapper<Accesses>() {
						public Accesses mapRow(ResultSet rs, int rowNum) throws SQLException {
							Accesses access = new Accesses();
							access.setId(rs.getInt("id"));
							access.setAccessTypeID(rs.getInt("access_type_id"));
							access.setMemberID(rs.getInt("member_id"));
							access.setBlocked(rs.getBoolean("blocked"));
							access.setExpired(rs.getBoolean("expired"));
							access.setExpiredDate(rs.getDate("expired_date"));
							access.setPin(rs.getString("credential"));
							return access;
						}
					});
			return access;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void clearAccessAttemptsRecord(Integer memberID, Integer accessTypeID) {
		this.jdbcTemplate.update("delete from access_attempts where member_id = ? and access_type_id = ?", memberID,
				accessTypeID);
	}

	public void changeCredential(Integer memberID, Integer accessTypeID, String newCredential) {
		this.jdbcTemplate.update("update access set credential = MD5(?) where member_id = ? and access_type_id = ?",
				newCredential, memberID, accessTypeID);
	}

	public void unblockCredential(Integer memberID, Integer accessTypeID) {
		this.jdbcTemplate.update("update access set blocked = false where member_id = ? and access_type_id = ?",
				memberID, accessTypeID);
	}

	public List<AccessType> getAllCredentialType(Integer currentPage, Integer pageSize) {
		try {
			List<AccessType> accessType = this.jdbcTemplate.query("select * from access_types LIMIT ?, ?",
					new Object[] { currentPage, pageSize }, new RowMapper<AccessType>() {
						public AccessType mapRow(ResultSet rs, int rowNum) throws SQLException {
							AccessType accessType = new AccessType();
							accessType.setId(rs.getInt("id"));
							accessType.setName(rs.getString("name"));
							accessType.setDescription(rs.getString("description"));
							return accessType;
						}
					});
			return accessType;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updateCredentialType(Integer id, String name, String internalName, String description) {
		this.jdbcTemplate.update("update access_types set name = ?, description= ? where id = ?",
				new Object[] { name, description, id });
	}

	public void createCredentialType(String name, String internalName, String description) {
		this.jdbcTemplate.update("insert into access_types (name, internal_name, description) values (?, ?, ?)", name,
				internalName, description);
	}
	
	public Integer countTotalAccount() {
		int count = this.jdbcTemplate.queryForObject("select  count(*) as total from access_types", Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
