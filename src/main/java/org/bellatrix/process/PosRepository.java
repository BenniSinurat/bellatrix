package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.bellatrix.data.Members;
import org.bellatrix.data.Terminal;
import org.bellatrix.services.DeletePOSRequest;
import org.bellatrix.services.RegisterPOSRequest;
import org.bellatrix.services.UpdatePOSRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class PosRepository {

	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(PosRepository.class);

	public Terminal getPosTerminalDetail(Integer id, Members member) {
		try {
			Terminal terminal = this.jdbcTemplate.queryForObject(
					"select * from pos_terminal where id = ? and member_id = ?", new Object[] { id, member.getId() },
					new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int rowNum) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setToMember(member);
							terminal.setAddress(rs.getString("outlet_address"));
							terminal.setCity(rs.getString("outlet_city"));
							terminal.setPostalCode(rs.getString("postal_code"));
							terminal.setPic(rs.getString("outlet_pic"));
							terminal.setMsisdn(rs.getString("outlet_msisdn"));
							terminal.setEmail(rs.getString("outlet_email"));
							terminal.setName(rs.getString("name"));
							terminal.setOpenPayment(rs.getBoolean("open_payment"));
							terminal.setTransferTypeID(rs.getInt("transfer_type_id"));
							terminal.setFixedAmount(rs.getBoolean("fixed_amount"));
							if (terminal.isFixedAmount()) {
								terminal.setAmount(rs.getBigDecimal("amount"));
							} else {
								terminal.setAmount(null);
							}
							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Terminal> getPosTerminalDetail(Members member, Integer currentPage, Integer pageSize) {
		try {
			List<Terminal> terminal = this.jdbcTemplate
					.query("select * from pos_terminal where member_id = ? order by id desc limit " + currentPage + ","
							+ pageSize + ";", new Object[] { member.getId() }, new RowMapper<Terminal>() {
								public Terminal mapRow(ResultSet rs, int rowNum) throws SQLException {
									Terminal terminal = new Terminal();
									terminal.setId(rs.getInt("id"));
									terminal.setToMember(member);
									terminal.setAddress(rs.getString("outlet_address"));
									terminal.setCity(rs.getString("outlet_city"));
									terminal.setPostalCode(rs.getString("postal_code"));
									terminal.setPic(rs.getString("outlet_pic"));
									terminal.setMsisdn(rs.getString("outlet_msisdn"));
									terminal.setEmail(rs.getString("outlet_email"));
									terminal.setName(rs.getString("name"));
									terminal.setOpenPayment(rs.getBoolean("open_payment"));
									terminal.setTransferTypeID(rs.getInt("transfer_type_id"));
									terminal.setFixedAmount(rs.getBoolean("fixed_amount"));
									if (terminal.isFixedAmount()) {
										terminal.setAmount(rs.getBigDecimal("amount"));
									} else {
										terminal.setAmount(null);
									}
									return terminal;
								}
							});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void registerPOS(RegisterPOSRequest req, Integer memberID) {
		this.jdbcTemplate.update(
				"insert into pos_terminal (member_id, transfer_type_id, name, outlet_pic, outlet_address, outlet_city, postal_code, outlet_email, outlet_msisdn, open_payment, fixed_amount, amount) values(?,?,?,?,?,?,?,?,?,?,?,?);",
				memberID, req.getTransferTypeID(), req.getName(), req.getPic(), req.getAddress(), req.getCity(),
				req.getPostalCode(), req.getEmail(), req.getMsisdn(), req.getOpenPayment(), req.getFixedAmount(),
				req.getAmount());
	}

	public void updatePOS(UpdatePOSRequest req, Integer memberID) {
		this.jdbcTemplate.update(
				"update pos_terminal set pos_terminal.transfer_type_id = ?, pos_terminal.name = ?, pos_terminal.outlet_pic = ?, pos_terminal.outlet_address = ?, pos_terminal.outlet_city = ?, pos_terminal.postal_code = ?, pos_terminal.outlet_email = ?, pos_terminal.outlet_msisdn = ?, pos_terminal.open_payment = ?, pos_terminal.fixed_amount = ?, pos_terminal.amount = ? where id = ? and member_id = ?",
				req.getTransferTypeID(), req.getName(), req.getPic(), req.getAddress(), req.getCity(),
				req.getPostalCode(), req.getEmail(), req.getMsisdn(), req.getOpenPayment(), req.getFixedAmount(),
				req.getAmount(), req.getTerminalID(), memberID);
	}
	
	public void deletePOS(DeletePOSRequest req, Integer memberID) {
		this.jdbcTemplate.update(
				"delete from  pos_terminal where id = ? and member_id = ?",
				req.getTerminalID(), memberID);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}