package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.bellatrix.data.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class MessageRepository {

	private JdbcTemplate jdbcTemplate;

	public Integer countUnreadMessageByUsername(Integer memberID) {
		int attemptsCount = this.jdbcTemplate.queryForObject(
				"select count(id) as unread from messages where messages.to_member_id = ? and messages.read = false;",
				Integer.class, memberID);
		return attemptsCount;
	}

	public List<NotificationMessage> loadMessageByMemberID(Integer memberID, Integer currentPage, Integer pageSize) {
		try {
			List<NotificationMessage> message = this.jdbcTemplate.query(
					"select a.id, a.subject, a.body, a.read, a.date, (select username from members where id = a.from_member_id ) as from_username , (select name from members where id = a.from_member_id ) as from_name from messages a where a.to_member_id = ? order by date desc limit ?,?;",
					new Object[] { memberID, currentPage, pageSize }, new RowMapper<NotificationMessage>() {
						public NotificationMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
							NotificationMessage message = new NotificationMessage();
							message.setId(rs.getInt("id"));
							message.setFromUsername(rs.getString("from_username"));
							message.setFromName(rs.getString("from_name"));
							message.setSubject(rs.getString("subject"));
							message.setBody(rs.getString("body"));
							message.setRead(rs.getBoolean("read"));
							message.setDate(rs.getTimestamp("date"));
							return message;
						}
					});
			return message;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<NotificationMessage> loadMessageByID(Integer id) {
		try {
			List<NotificationMessage> message = this.jdbcTemplate.query(
					"select a.id, a.subject, a.body, a.read, a.date, (select username from members where id = a.from_member_id ) as from_username , (select name from members where id = a.from_member_id ) as from_name from messages a where a.id = ?;",
					new Object[] { id }, new RowMapper<NotificationMessage>() {
						public NotificationMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
							NotificationMessage message = new NotificationMessage();
							message.setId(rs.getInt("id"));
							message.setFromUsername(rs.getString("from_username"));
							message.setFromName(rs.getString("from_name"));
							message.setSubject(rs.getString("subject"));
							message.setBody(rs.getString("body"));
							message.setRead(rs.getBoolean("read"));
							message.setDate(rs.getTimestamp("date"));
							return message;
						}
					});
			return message;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void flagReadByID(Integer memberID) {
		this.jdbcTemplate.update("update messages set messages.read = true where id = ?", memberID);
	}

	public void sendMessage(Integer fromMemberID, Integer toMemberID, String subject, String body) {
		this.jdbcTemplate.update("insert into messages (from_member_id, to_member_id, subject, body) values(?,?,?,?);",
				fromMemberID, toMemberID, subject, body);
	}

	public void deleteMessage(Integer id) {
		this.jdbcTemplate.update("delete from messages where id = ?;", id);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
