package org.bellatrix.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.bellatrix.data.Billers;
import org.bellatrix.data.PaymentChannel;
import org.bellatrix.services.BillerRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.Statement;

@Component
@Repository
public class BillPaymentRepository {

	private JdbcTemplate jdbcTemplate;

	public List<Billers> loadBillerFromMember(Integer memberID) {
		try {
			List<Billers> billers = this.jdbcTemplate.query(
					"select b.id, b.module_url, b.name, b.description, b.open_payment, b.external, b.asynchronous from billers b inner join biller_permission a on a.biller_id = b.id where a.member_id = ? and a.enabled = true;",
					new Object[] { memberID }, new RowMapper<Billers>() {
						public Billers mapRow(ResultSet rs, int rowNum) throws SQLException {
							Billers billers = new Billers();
							billers.setId(rs.getInt("id"));
							billers.setBillerName(rs.getString("name"));
							billers.setDescription(rs.getString("description"));
							billers.setModuleURL(rs.getString("module_url"));
							billers.setOpenPayment(rs.getBoolean("open_payment"));
							billers.setAsyncPayment(rs.getBoolean("asynchronous"));
							billers.setExternalSystem(rs.getBoolean("external"));
							return billers;
						}
					});
			return billers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Billers loadBillerFromID(Integer memberID, Integer billerID) {
		try {
			Billers billers = this.jdbcTemplate.queryForObject(
					"select b.id, b.module_url, b.name, b.description, b.open_payment, b.external, b.asynchronous from billers b inner join biller_permission a on a.biller_id = b.id where a.member_id = ? and a.biller_id = ? and a.enabled = true;",
					new Object[] { memberID, billerID }, new RowMapper<Billers>() {
						public Billers mapRow(ResultSet rs, int rowNum) throws SQLException {
							Billers billers = new Billers();
							billers.setId(rs.getInt("id"));
							billers.setBillerName(rs.getString("name"));
							billers.setDescription(rs.getString("description"));
							billers.setModuleURL(rs.getString("module_url"));
							billers.setOpenPayment(rs.getBoolean("open_payment"));
							billers.setAsyncPayment(rs.getBoolean("asynchronous"));
							billers.setExternalSystem(rs.getBoolean("external"));
							return billers;
						}
					});
			return billers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Billers> getBillerList() {
		try {
			List<Billers> billers = this.jdbcTemplate.query("select * from billers where enabled = true;",
					new RowMapper<Billers>() {
						public Billers mapRow(ResultSet rs, int rowNum) throws SQLException {
							Billers billers = new Billers();
							billers.setId(rs.getInt("id"));
							billers.setBillerName(rs.getString("name"));
							billers.setDescription(rs.getString("description"));
							billers.setModuleURL(rs.getString("module_url"));
							billers.setOpenPayment(rs.getBoolean("open_payment"));
							billers.setAsyncPayment(rs.getBoolean("asynchronous"));
							billers.setExternalSystem(rs.getBoolean("external"));
							return billers;
						}
					});
			return billers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer getBillerIDFromMember(Integer memberID) {
		try {
			Integer id = this.jdbcTemplate.queryForObject("select id from billers where member_id = ?;",
					new Object[] { memberID }, Integer.class);
			return id;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Transactional
	public void registerBiller(BillerRegisterRequest req, Integer memberID) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into billers (name, description, module_url, open_payment, asynchronous, external, member_id) values (?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, req.getBillerName());
				statement.setString(2, req.getDescription());
				statement.setString(3, req.getModuleURL());
				statement.setBoolean(4, req.getOpenPayment());
				statement.setBoolean(5, req.getAsyncPayment());
				statement.setBoolean(6, req.getExternalSystem());
				statement.setInt(7, memberID);
				return statement;
			}
		}, holder);

		Integer billID = holder.getKey().intValue();
		this.jdbcTemplate.update("insert into biller_permission (member_id, biller_id) values (?, ?)",
				new Object[] { memberID, billID });
	}

	public List<PaymentChannel> loadChannels() {
		try {
			List<PaymentChannel> channel = this.jdbcTemplate.query("select * from payment_channel",
					new RowMapper<PaymentChannel>() {
						public PaymentChannel mapRow(ResultSet rs, int rowNum) throws SQLException {
							PaymentChannel channel = new PaymentChannel();
							channel.setDescription(rs.getString("description"));
							channel.setId(rs.getInt("id"));
							channel.setName(rs.getString("name"));
							return channel;
						}
					});
			return channel;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
