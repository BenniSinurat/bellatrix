package org.bellatrix.process;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class LogRepository {

	private JdbcTemplate jdbcTemplate;

	public void logRequest(String traceNumber, String payload, String messageID, String type) {
		jdbcTemplate.update(
				"insert into log (message_id, type, trace_number, request_payload, request_date) values (?,?,?,?,NOW())",
				messageID, type, traceNumber, payload);
	}

	public void logResponse(String traceNumber, String payload, String messageID, String trxNo, String status) {
		this.jdbcTemplate.update(
				"update log set transaction_number = ?, response_payload = ?, status = ?, response_date = NOW()  where message_id = ? and trace_number = ?",
				trxNo, payload, status, messageID, traceNumber);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
