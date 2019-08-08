package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.bellatrix.data.Groups;
import org.bellatrix.data.Members;
import org.bellatrix.data.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Component
@Repository
public class GroupRepository {

	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void createGroups(Groups groups) {
		jdbcTemplate.update("insert into groups (name, description, pin_length, max_pin_tries) values (?, ?, ?, ?)",
				groups.getName(), groups.getDescription(), groups.getPinLength(), groups.getMaxPinTries());
	}

	public void deleteGroups(String id) {
		jdbcTemplate.update("delete from groups where id = " + id);
	}

	public List<Groups> loadAllGroups(Integer currentPage, Integer pageSize) {
		try {
			List<Groups> groups = this.jdbcTemplate.query("select * from groups LIMIT ?,?",
					new Object[] { currentPage, pageSize }, new RowMapper<Groups>() {
						public Groups mapRow(ResultSet rs, int rowNum) throws SQLException {
							Groups groups = new Groups();
							groups.setId(rs.getInt("id"));
							groups.setNotificationID(rs.getInt("notification_id"));
							groups.setName(rs.getString("name"));
							groups.setDescription(rs.getString("description"));
							groups.setMaxPinTries(rs.getInt("max_pin_tries"));
							groups.setPinLength(rs.getInt("pin_length"));
							groups.setCreatedDate(rs.getTimestamp("created_date"));
							groups.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return groups;
						}
					});
			return groups;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Groups loadGroupsByID(Integer req) {
		try {
			Groups groups = this.jdbcTemplate.queryForObject("select * from groups where id = ?", new Object[] { req },
					new RowMapper<Groups>() {
						public Groups mapRow(ResultSet rs, int rowNum) throws SQLException {
							Groups groups = new Groups();
							groups.setId(rs.getInt("id"));
							groups.setNotificationID(rs.getInt("notification_id"));
							groups.setName(rs.getString("name"));
							groups.setDescription(rs.getString("description"));
							groups.setCreatedDate(rs.getTimestamp("created_date"));
							groups.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							groups.setMaxPinTries(rs.getInt("max_pin_tries"));
							groups.setPinLength(rs.getInt("pin_length"));
							return groups;
						}
					});
			return groups;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Groups> loadGroupByIds(List<Integer> req) {
		String sql = "select *  from groups where id in (:groupID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("groupID", req);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		List<Groups> groups = template.query(sql, paramMap, new RowMapper<Groups>() {
			public Groups mapRow(ResultSet rs, int rowNum) throws SQLException {
				Groups groups = new Groups();
				groups.setId(rs.getInt("id"));
				groups.setNotificationID(rs.getInt("notification_id"));
				groups.setName(rs.getString("name"));
				groups.setDescription(rs.getString("description"));
				groups.setCreatedDate(rs.getTimestamp("created_date"));
				groups.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
				groups.setMaxPinTries(rs.getInt("max_pin_tries"));
				groups.setPinLength(rs.getInt("pin_length"));
				return groups;
			}
		});
		return groups;
	}

	public Groups loadGroupsByMemberUsername(String req) {
		try {
			Groups groups = this.jdbcTemplate.queryForObject(
					"select a.id, a.notification_id, a.name, a.description, a.pin_length, a.max_pin_tries, a.created_date from groups a join members b on b.group_id = a.id where b.username = ?",
					new Object[] { req }, new RowMapper<Groups>() {
						public Groups mapRow(ResultSet rs, int rowNum) throws SQLException {
							Groups groups = new Groups();
							groups.setId(rs.getInt("id"));
							groups.setNotificationID(rs.getInt("notification_id"));
							groups.setName(rs.getString("name"));
							groups.setDescription(rs.getString("description"));
							groups.setCreatedDate(rs.getTimestamp("created_date"));
							groups.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							groups.setMaxPinTries(rs.getInt("max_pin_tries"));
							groups.setPinLength(rs.getInt("pin_length"));
							return groups;
						}
					});
			return groups;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Groups loadGroupsByMemberID(Integer req) {
		try {
			Groups groups = this.jdbcTemplate.queryForObject(
					"select a.id, a.notification_id, a.name, a.description, a.pin_length, a.max_pin_tries, a.created_date from groups a join members b on b.group_id = a.id where b.id = ?",
					new Object[] { req }, new RowMapper<Groups>() {
						public Groups mapRow(ResultSet rs, int rowNum) throws SQLException {
							Groups groups = new Groups();
							groups.setId(rs.getInt("id"));
							groups.setNotificationID(rs.getInt("notification_id"));
							groups.setName(rs.getString("name"));
							groups.setDescription(rs.getString("description"));
							groups.setCreatedDate(rs.getTimestamp("created_date"));
							groups.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							groups.setMaxPinTries(rs.getInt("max_pin_tries"));
							groups.setPinLength(rs.getInt("pin_length"));
							return groups;
						}
					});
			return groups;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Notifications loadDefaultNotificationByID(Integer req) {
		try {
			Notifications notif = this.jdbcTemplate.queryForObject("select * from notifications where id = ?",
					new Object[] { req }, new RowMapper<Notifications>() {
						public Notifications mapRow(ResultSet rs, int rowNum) throws SQLException {
							Notifications notif = new Notifications();
							notif.setId(rs.getInt("id"));
							notif.setName(rs.getString("name"));
							notif.setModuleURL(rs.getString("module_url"));
							return notif;
						}
					});
			return notif;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Notifications loadDefaultNotificationByGroupID(Integer req) {
		try {
			Notifications notif = this.jdbcTemplate.queryForObject(
					"select a.id, a.name, a.module_url from notifications a inner join groups b on a.id = b.notification_id where b.id = ?",
					new Object[] { req }, new RowMapper<Notifications>() {
						public Notifications mapRow(ResultSet rs, int rowNum) throws SQLException {
							Notifications notif = new Notifications();
							notif.setId(rs.getInt("id"));
							notif.setName(rs.getString("name"));
							notif.setModuleURL(rs.getString("module_url"));
							return notif;
						}
					});
			return notif;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalGroups() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from groups", Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
