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

	public Terminal getPosTerminalDetail(Integer id, Integer memberID) {
		try {
			Terminal terminal = this.jdbcTemplate.queryForObject(
					"select * from pos_terminal where id = ? and member_id = ?", new Object[] { id, memberID },
					new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int rowNum) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setAddress(rs.getString("outlet_address"));
							terminal.setCity(rs.getString("outlet_city"));
							terminal.setPostalCode(rs.getString("postal_code"));
							terminal.setPic(rs.getString("outlet_pic"));
							terminal.setMsisdn(rs.getString("outlet_msisdn"));
							terminal.setEmail(rs.getString("outlet_email"));
							terminal.setName(rs.getString("name"));
							terminal.setTransferTypeID(rs.getInt("transfer_type_id"));
							terminal.setNnsID(rs.getString("nns_id"));
							terminal.setMerchantCategoryCode(rs.getString("merchant_category_code"));
							
							Members member = new Members();
							member.setId(rs.getInt("member_id"));
							
							Members toMember = new Members();
							toMember.setId(rs.getInt("to_member_id"));

							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Terminal> getPosTerminalDetail(Integer memberID, Integer currentPage, Integer pageSize) {
		try {
			List<Terminal> terminal = this.jdbcTemplate
					.query("select * from pos_terminal where member_id = ? order by id desc limit " + currentPage + ","
							+ pageSize + ";", new Object[] { memberID }, new RowMapper<Terminal>() {
								public Terminal mapRow(ResultSet rs, int rowNum) throws SQLException {
									Terminal terminal = new Terminal();
									terminal.setId(rs.getInt("id"));
									terminal.setAddress(rs.getString("outlet_address"));
									terminal.setCity(rs.getString("outlet_city"));
									terminal.setPostalCode(rs.getString("postal_code"));
									terminal.setPic(rs.getString("outlet_pic"));
									terminal.setMsisdn(rs.getString("outlet_msisdn"));
									terminal.setEmail(rs.getString("outlet_email"));
									terminal.setName(rs.getString("name"));
									terminal.setTransferTypeID(rs.getInt("transfer_type_id"));
									terminal.setNnsID(rs.getString("nns_id"));
									terminal.setMerchantCategoryCode(rs.getString("merchant_category_code"));
									
									Members toMember = new Members();
									toMember.setId(rs.getInt("to_member_id"));
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
				"insert into pos_terminal (member_id, transfer_type_id, name, outlet_pic, outlet_address, outlet_city, postal_code, outlet_email, outlet_msisdn, nns_id, merchant_category_code) values(?,?,?,?,?,?,?,?,?,?,?);",
				memberID, req.getTransferTypeID(), req.getName(), req.getPic(), req.getAddress(), req.getCity(),
				req.getPostalCode(), req.getEmail(), req.getMsisdn(), req.getNnsID(), req.getMerchantCategoryCode());
	}

	public void updatePOS(UpdatePOSRequest req, Integer memberID) {
		this.jdbcTemplate.update(
				"update pos_terminal set pos_terminal.transfer_type_id = ?, pos_terminal.name = ?, pos_terminal.outlet_pic = ?, pos_terminal.outlet_address = ?, pos_terminal.outlet_city = ?, pos_terminal.postal_code = ?, pos_terminal.outlet_email = ?, pos_terminal.outlet_msisdn = ?, pos_terminal.merchant_category_code = ? where id = ? and member_id = ?",
				req.getTransferTypeID(), req.getName(), req.getPic(), req.getAddress(), req.getCity(),
				req.getPostalCode(), req.getEmail(), req.getMsisdn(), req.getMerchantCategoryCode(),
				req.getTerminalID(), memberID);
	}

	public void deletePOS(DeletePOSRequest req, Integer memberID) {
		this.jdbcTemplate.update("delete from  pos_terminal where id = ? and member_id = ?", req.getTerminalID(),
				memberID);
	}

	public Terminal getPosTerminalByNNSID(String nnsID) {
		try {
			Terminal terminal = this.jdbcTemplate.queryForObject(
					"select * from pos_terminal p join members m on m.id = p.member_id where p.nns_id = ?",
					new Object[] { nnsID }, new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int rowNum) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setAddress(rs.getString("outlet_address"));
							terminal.setCity(rs.getString("outlet_city"));
							terminal.setPostalCode(rs.getString("postal_code"));
							terminal.setPic(rs.getString("outlet_pic"));
							terminal.setMsisdn(rs.getString("outlet_msisdn"));
							terminal.setEmail(rs.getString("outlet_email"));
							terminal.setName(rs.getString("name"));
							terminal.setTransferTypeID(rs.getInt("transfer_type_id"));
							terminal.setNnsID(rs.getString("nns_id"));
							terminal.setMerchantCategoryCode(rs.getString("merchant_category_code"));

							Members members = new Members();
							members.setId(rs.getInt("member_id"));
							members.setGroupID(rs.getInt("group_id"));
							members.setUsername(rs.getString("username"));
							members.setName(rs.getString("name"));
							members.setMsisdn(rs.getString("msisdn"));
							members.setEmail(rs.getString("email"));
							members.setAddress(rs.getString("address"));
							members.setDateOfBirth(rs.getDate("date_of_birth"));
							members.setPlaceOfBirth(rs.getString("place_of_birth"));
							members.setIdCardNo(rs.getString("id_card_no"));
							members.setMotherMaidenName(rs.getString("mother_maiden_name"));
							members.setNationality(rs.getString("nationality"));
							members.setWork(rs.getString("work"));
							members.setSex(rs.getString("sex"));
							members.setCreatedDate(rs.getTimestamp("created_date"));
							members.setUid(rs.getString("uid"));
							members.setFcmID(rs.getString("fcm_id"));
							terminal.setToMember(members);
							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Terminal getPosTerminalDetail(String nnsID) {
		try {
			Terminal terminal = this.jdbcTemplate.queryForObject("select * from pos_terminal where nns_id = ?",
					new Object[] { nnsID }, new RowMapper<Terminal>() {
						public Terminal mapRow(ResultSet rs, int rowNum) throws SQLException {
							Terminal terminal = new Terminal();
							terminal.setId(rs.getInt("id"));
							terminal.setAddress(rs.getString("outlet_address"));
							terminal.setCity(rs.getString("outlet_city"));
							terminal.setPostalCode(rs.getString("postal_code"));
							terminal.setPic(rs.getString("outlet_pic"));
							terminal.setMsisdn(rs.getString("outlet_msisdn"));
							terminal.setEmail(rs.getString("outlet_email"));
							terminal.setName(rs.getString("name"));
							terminal.setTransferTypeID(rs.getInt("transfer_type_id"));
							terminal.setNnsID(rs.getString("nns_id"));
							terminal.setMerchantCategoryCode(rs.getString("merchant_category_code"));
							
							Members member = new Members();
							member.setId(rs.getInt("member_id"));
							terminal.setMember(member);
							
							Members toMember = new Members();
							toMember.setId(rs.getInt("member_id"));
							terminal.setToMember(toMember);
							return terminal;
						}
					});
			return terminal;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}