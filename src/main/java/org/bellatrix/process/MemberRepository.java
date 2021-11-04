package org.bellatrix.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.bellatrix.data.ExternalMemberFields;
import org.bellatrix.data.Groups;
import org.bellatrix.data.MemberKYC;
import org.bellatrix.data.MemberView;
import org.bellatrix.data.Members;
import org.bellatrix.data.MerchantBusinessScale;
import org.bellatrix.data.MerchantCategory;
import org.bellatrix.data.MerchantOwner;
import org.bellatrix.data.MerchantSubCategory;
import org.bellatrix.data.Merchants;
import org.bellatrix.data.MultiBillers;
import org.bellatrix.services.LoadMembersByExternalIDRequest;
import org.bellatrix.services.MemberKYCRequest;
import org.bellatrix.services.RegisterMemberRequest;
import org.bellatrix.services.RegisterMerchantRequest;
import org.bellatrix.services.UpdateMemberRequest;
import org.bellatrix.services.UpdateMerchantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.Statement;

@Component
@Repository
public class MemberRepository {

	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(MemberRepository.class);

	public List<Members> findMembers(String byField, Object id) {
		try {
			List<Members> members = this.jdbcTemplate.query("select * from members where " + byField + " = ?",
					new Object[] { id }, new RowMapper<Members>() {
						public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
							Members members = new Members();
							members.setId(rs.getInt("id"));
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
							return members;
						}
					});
			return members;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Members> loadMembersByIds(List<Integer> req) {
		String sql = "select *  from members where id in (:memberID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("memberID", req);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		List<Members> members = template.query(sql, paramMap, new RowMapper<Members>() {
			public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
				Members members = new Members();
				members.setId(rs.getInt("id"));
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
				return members;
			}
		});
		return members;
	}

	public List<Members> loadMembersByGroupID(Integer groupID, Integer currentPage, Integer pageSize) {
		try {
			List<Members> members = this.jdbcTemplate
					.query("select *  from members where group_id = ? order by id desc limit " + currentPage + ", "
							+ pageSize + ";", new Object[] { groupID }, new RowMapper<Members>() {
								public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
									Members members = new Members();
									members.setId(rs.getInt("id"));
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
									members.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
									members.setEmailVerify(rs.getBoolean("email_verify"));
									members.setUid(rs.getString("uid"));
									members.setFcmID(rs.getString("fcm_id"));
									return members;
								}
							});
			return members;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Members> findAllMembers(Integer offset, Integer limit) {
		try {
			List<Members> members = this.jdbcTemplate.query(
					"select *  from members order by id desc limit " + offset + ", " + limit, new Object[] {},
					new RowMapper<Members>() {
						public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
							Members members = new Members();
							members.setId(rs.getInt("id"));
							members.setGroupID(rs.getInt("group_id"));
							members.setUsername(rs.getString("username"));
							members.setName(rs.getString("name"));
							members.setMsisdn(rs.getString("msisdn"));
							members.setEmail(rs.getString("email"));
							members.setEmailVerify(rs.getBoolean("email_verify"));
							members.setAddress(rs.getString("address"));
							members.setDateOfBirth(rs.getDate("date_of_birth"));
							members.setPlaceOfBirth(rs.getString("place_of_birth"));
							members.setIdCardNo(rs.getString("id_card_no"));
							members.setMotherMaidenName(rs.getString("mother_maiden_name"));
							members.setNationality(rs.getString("nationality"));
							members.setWork(rs.getString("work"));
							members.setSex(rs.getString("sex"));
							members.setCreatedDate(rs.getTimestamp("created_date"));
							members.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							members.setUid(rs.getString("uid"));
							members.setFcmID(rs.getString("fcm_id"));
							return members;
						}
					});
			return members;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Members findOneMembers(String byField, Object id) {
		try {
			Members members = this.jdbcTemplate.queryForObject("select a.id, a.group_id, a.username, a.name, a.msisdn, a.email, a.email_verify, a.address, a.date_of_birth, a.place_of_birth, a.id_card_no, a.mother_maiden_name, a.nationality, a.work, a.sex, a.created_date, a.uid, a.fcm_id, (select status from member_kyc_request where member_id = a.id order by id desc limit 1) as kyc_status from members a where a." + byField + " = ?",
					new Object[] { id }, new RowMapper<Members>() {
						public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
							Members members = new Members();
							members.setId(rs.getInt("id"));
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
							members.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							members.setEmailVerify(rs.getBoolean("email_verify"));
							members.setUid(rs.getString("uid"));
							members.setFcmID(rs.getString("fcm_id"));
							members.setKycStatus(rs.getString("kyc_status"));
							return members;
						}
					});
			return members;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Members> findMembersByExternalID(LoadMembersByExternalIDRequest req) {
		try {
			List<Members> members = this.jdbcTemplate.query(
					"select a.id, a.group_id, a.username, a.name, a.msisdn, a.email, a.email_verify, a.address, a.date_of_birth, a.place_of_birth, a. id_card_no, a.mother_maiden_name, a.nationality, a.work, a.sex, a.created_date, a.uid, a.fcm_id, (select status from member_kyc_request where member_id = a.id order by id desc limit 1) as kyc_status from members a join external_members b on a.id=b.member_id where b.parent_id=? and b.external_id=? ORDER BY id DESC",
					new Object[] { req.getPartnerID(), req.getExternalID() }, new RowMapper<Members>() {
						public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
							Members members = new Members();
							members.setId(rs.getInt("id"));
							members.setGroupID(rs.getInt("group_id"));
							members.setUsername(rs.getString("username"));
							members.setName(rs.getString("name"));
							members.setMsisdn(rs.getString("msisdn"));
							members.setEmail(rs.getString("email"));
							members.setEmailVerify(rs.getBoolean("email_verify"));
							members.setAddress(rs.getString("address"));
							members.setDateOfBirth(rs.getDate("date_of_birth"));
							members.setPlaceOfBirth(rs.getString("place_of_birth"));
							members.setIdCardNo(rs.getString("id_card_no"));
							members.setMotherMaidenName(rs.getString("mother_maiden_name"));
							members.setNationality(rs.getString("nationality"));
							members.setWork(rs.getString("work"));
							members.setSex(rs.getString("sex"));
							members.setUid(rs.getString("uid"));
							members.setFcmID(rs.getString("fcm_id"));
							members.setKycStatus(rs.getString("kyc_status"));
							members.setCreatedDate(rs.getTimestamp("created_date"));
							members.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							return members;
						}
					});
			return members;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Members> loadMembersByExternalID(LoadMembersByExternalIDRequest req) {
		try {
			List<Members> members = this.jdbcTemplate.query(
					"select a.id, a.group_id, a.username, a.name, a.msisdn, a.email, a.email_verify, a.address, a.date_of_birth, a.place_of_birth, a. id_card_no, a.mother_maiden_name, a.nationality, a.work, a.sex, a.created_date, a.uid, a.fcm_id, (select status from member_kyc_request where member_id = a.id order by id desc limit 1) as kyc_status, b.parent_id, b.external_id, b.description from members a join external_members b on a.id=b.member_id where b.parent_id=? ORDER BY id DESC LIMIT ?,?",
					new Object[] { req.getPartnerID(), req.getCurrentPage(), req.getPageSize() },
					new RowMapper<Members>() {
						public Members mapRow(ResultSet rs, int rowNum) throws SQLException {
							Members members = new Members();
							members.setId(rs.getInt("id"));
							members.setGroupID(rs.getInt("group_id"));
							members.setUsername(rs.getString("username"));
							members.setName(rs.getString("name"));
							members.setMsisdn(rs.getString("msisdn"));
							members.setEmail(rs.getString("email"));
							members.setEmailVerify(rs.getBoolean("email_verify"));
							members.setAddress(rs.getString("address"));
							members.setDateOfBirth(rs.getDate("date_of_birth"));
							members.setPlaceOfBirth(rs.getString("place_of_birth"));
							members.setIdCardNo(rs.getString("id_card_no"));
							members.setMotherMaidenName(rs.getString("mother_maiden_name"));
							members.setNationality(rs.getString("nationality"));
							members.setWork(rs.getString("work"));
							members.setSex(rs.getString("sex"));
							members.setUid(rs.getString("uid"));
							members.setFcmID(rs.getString("fcm_id"));
							members.setKycStatus(rs.getString("kyc_status"));
							members.setCreatedDate(rs.getTimestamp("created_date"));
							members.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							List<ExternalMemberFields> lext = new LinkedList<ExternalMemberFields>();
							ExternalMemberFields ext = new ExternalMemberFields();
							ext.setDescription(rs.getString("description"));
							ext.setExternalID(rs.getString("external_id"));
							ext.setParentID(rs.getInt("parent_id"));
							ext.setMemberID(rs.getInt("id"));
							ext.setId(rs.getInt("id"));
							ext.setCreatedDate(rs.getTimestamp("created_date"));
							lext.add(ext);
							members.setExternalMembers(lext);
							return members;
						}
					});
			return members;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<ExternalMemberFields> loadExternalMemberFields(Integer memberID) {
		try {
			List<ExternalMemberFields> extmembers = this.jdbcTemplate.query(
					"select * from external_members where member_id = ?", new Object[] { memberID },
					new RowMapper<ExternalMemberFields>() {
						public ExternalMemberFields mapRow(ResultSet rs, int rowNum) throws SQLException {
							ExternalMemberFields extmembers = new ExternalMemberFields();
							extmembers.setParentID(rs.getInt("parent_id"));
							extmembers.setExternalID(rs.getString("external_id"));
							extmembers.setDescription(rs.getString("description"));
							extmembers.setMemberID(rs.getInt("member_id"));
							extmembers.setId(rs.getInt("id"));
							extmembers.setCreatedDate(rs.getTimestamp("created_date"));
							return extmembers;
						}
					});
			return extmembers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MultiBillers> loadBillerFromMemberID(Integer memberID) {
		try {
			List<MultiBillers> billers = this.jdbcTemplate.query("select * from multi_billers where member_id = ?",
					new Object[] { memberID }, new RowMapper<MultiBillers>() {
						public MultiBillers mapRow(ResultSet rs, int rowNum) throws SQLException {
							MultiBillers billers = new MultiBillers();
							billers.setId(rs.getInt("id"));
							billers.setBillerID(rs.getInt("biller_id"));
							billers.setMemberID(rs.getInt("member_id"));
							billers.setInquiryURL(rs.getString("inquiry_url"));
							billers.setPaymentURL(rs.getString("payment_url"));
							billers.setReversalURL(rs.getString("reversal_url"));
							billers.setStatusURL(rs.getString("status_url"));
							billers.setTransferTypeID(rs.getInt("transfer_type_id"));
							return billers;
						}
					});
			return billers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public ExternalMemberFields loadExternalMemberFields(String externalID) {
		try {
			ExternalMemberFields extmembers = this.jdbcTemplate.queryForObject(
					"select a.member_id, a.parent_id, a.description, b.username from external_members a join members b on a.member_id = b.id where external_id = ? ",
					new Object[] { externalID }, new RowMapper<ExternalMemberFields>() {
						public ExternalMemberFields mapRow(ResultSet rs, int rowNum) throws SQLException {
							ExternalMemberFields extmembers = new ExternalMemberFields();
							extmembers.setMemberID(rs.getInt("member_id"));
							extmembers.setUsername(rs.getString("username"));
							extmembers.setParentID(rs.getInt("parent_id"));
							extmembers.setExternalID(externalID);
							extmembers.setDescription(rs.getString("description"));
							extmembers.setId(rs.getInt("id"));
							extmembers.setCreatedDate(rs.getTimestamp("created_date"));
							return extmembers;
						}
					});
			return extmembers;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Transactional
	public void createMembers(RegisterMemberRequest req) {
		final RegisterMemberRequest members = req;

		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into members (group_id, name, username, email, msisdn, uid) values (?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, members.getGroupID());
				statement.setString(2, members.getName());
				statement.setString(3, members.getUsername());
				statement.setString(4, members.getEmail());
				statement.setString(5, members.getMsisdn());
				statement.setString(6, members.getUid());
				return statement;
			}
		}, holder);

		Integer memberID = holder.getKey().intValue();

		if (req.getExternalMemberFields() != null && req.getExternalMemberFields().getParentID() != 0) {
			this.jdbcTemplate.update(
					"insert into external_members (member_id, parent_id, external_id, description) values (?, ?, ?, ?)",
					new Object[] { memberID, req.getExternalMemberFields().getParentID(),
							req.getExternalMemberFields().getExternalID(),
							req.getExternalMemberFields().getDescription() });
		}

		if (req.getCustomFields() != null) {
			jdbcTemplate.batchUpdate(
					"insert into member_custom_field_values (member_custom_field_id, member_id, value, internal_name) values (?, ?, ?, ?)",
					new BatchPreparedStatementSetter() {
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setLong(1, members.getCustomFields().get(i).getMemberCustomFieldID());
							ps.setLong(2, memberID);
							ps.setString(3, members.getCustomFields().get(i).getValue());
							ps.setString(4, members.getCustomFields().get(i).getInternalName());
						}

						@Override
						public int getBatchSize() {
							return members.getCustomFields().size();
						}
					});
		}

	}

	@Transactional
	public void updateMembers(UpdateMemberRequest req) {
		final UpdateMemberRequest members = req;
		jdbcTemplate.update(
				"update members set group_id = ?, name = ?, username = ?, email = ?, email_verify = ?, msisdn = ?, address = ?, id_card_no = ?, date_of_birth = ?, place_of_birth = ?, mother_maiden_name = ?, work = ?, sex = ?, nationality = ?, uid = ?, fcm_id = ?  where id = ?",
				members.getGroupID(), members.getName(), members.getUsername(), members.getEmail(),
				members.getEmailVerify(), members.getMsisdn(), members.getAddress(), members.getIdCardNo(),
				members.getDateOfBirth(), members.getPlaceOfBirth(), members.getMotherMaidenName(), members.getWork(),
				members.getSex(), members.getNationality(), members.getUid(), members.getFcmID(), members.getId());

		if (members.getCustomFields() != null) {
			jdbcTemplate.batchUpdate(
					"INSERT INTO member_custom_field_values (member_custom_field_id, member_id, value, internal_name) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE value = VALUES(value)",
					new BatchPreparedStatementSetter() {
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setLong(1, members.getCustomFields().get(i).getMemberCustomFieldID());
							ps.setLong(2, members.getId());
							ps.setString(3, members.getCustomFields().get(i).getValue());
							ps.setString(4, members.getCustomFields().get(i).getInternalName());

						}

						@Override
						public int getBatchSize() {
							return members.getCustomFields().size();
						}
					});
		}
	}

	public Integer loadKYCMemberByID(Integer id) {
		try {
			int memberID = this.jdbcTemplate.queryForObject("select  member_id from member_kyc_request where id = ?",
					new Object[] { id }, Integer.class);
			return memberID;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	public Integer countTotalMembers() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from members", Integer.class);
		return count;
	}

	public Integer countTotalKYCMembers() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from member_kyc_request", Integer.class);
		return count;
	}

	public Integer countTotalExternalMembers(Integer parentID) {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from external_members where parent_id = ?",
				new Object[] { parentID }, Integer.class);
		return count;
	}

	public Integer validateExternalMember(Integer memberID, Integer parentID) {
		try {
			int id = this.jdbcTemplate.queryForObject(
					"select  id from external_members where member_id = ? and parent_id = ?",
					new Object[] { memberID, parentID }, Integer.class);
			return id;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer validateExternalMember(Integer memberID, Integer parentID, String externalID) {
		try {
			int id = this.jdbcTemplate.queryForObject(
					"select  id from external_members where member_id = ? and parent_id = ? and external_id = ?",
					new Object[] { memberID, parentID, externalID }, Integer.class);
			return id;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void subscribeMembers(Members parent, Members req, String externalID, String description) {
		jdbcTemplate.update(
				"insert into external_members (member_id, parent_id, external_id, description) values (?, ?, ?, ?)",
				req.getId(), parent.getId(), externalID, description);
	}

	public void resubscribeMembers(Integer id, String externalID, String description) {
		jdbcTemplate.update("update external_members set external_id = ?, description = ? where id = ?", externalID,
				description, id);
	}

	public void unsubscribeMembers(Members parent, String externalID) {
		jdbcTemplate.update("delete from external_members where parent_id = ? and external_id = ?", parent.getId(),
				externalID);
	}

	@Transactional
	public void memberKYCRequest(Integer memberID, MemberKYCRequest req) {
		jdbcTemplate.update(
				"update members set date_of_birth = ?, place_of_birth = ?, address = ?, id_card_no = ?, mother_maiden_name = ?, work = ?, sex = ?, nationality= ? where username = ?",
				req.getDateOfBirth(), req.getPlaceOfBirth(), req.getAddress(), req.getIdCardNo(),
				req.getMotherMaidenName(), req.getWork(), req.getSex(), req.getNationality(), req.getUsername());

		jdbcTemplate.update(
				"insert into member_kyc_request (member_id, destination_group_id, image_path_1, image_path_2, image_path_3) values (?, ?, ?, ?, ?)",
				memberID, req.getGroupID(), req.getImagePath1(), req.getImagePath2(), req.getImagePath3());
	}

	@Transactional
	public void memberKycRequest(Integer memberID, MemberKYCRequest req) {
		jdbcTemplate.update(
				"update members set date_of_birth = ?, place_of_birth = ?, address = ?, id_card_no = ?, mother_maiden_name = ?, work = ?, sex = ?, nationality= ? where username = ?",
				req.getDateOfBirth(), req.getPlaceOfBirth(), req.getAddress(), req.getIdCardNo(),
				req.getMotherMaidenName(), req.getWork(), req.getSex(), req.getNationality(), req.getUsername());

		jdbcTemplate.update(
				"update member_kyc_request set destination_group_id = ?, image_path_1 = ?, image_path_2 = ?, image_path_3 = ?, status = 'REQUESTED' where member_id = ?",
				req.getGroupID(), req.getImagePath1(), req.getImagePath2(), req.getImagePath3(), memberID);
	}

	@Transactional
	public boolean memberKYCApproval(Integer approvedBy, Integer id) {
		int affectedRows = jdbcTemplate.update(
				"update member_kyc_request set status = 'APPROVED', approved_by = ?, approval_date = NOW(), approved = TRUE where id = ?",
				approvedBy, id);
		if (affectedRows == 1) {

			HashMap<String, Integer> kycmember = this.jdbcTemplate.queryForObject(
					"select  member_id, destination_group_id from member_kyc_request where id = ?;",
					new Object[] { id }, new RowMapper<HashMap<String, Integer>>() {
						public HashMap<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
							HashMap<String, Integer> kycmember = new HashMap<String, Integer>();
							kycmember.put("memberID", rs.getInt("member_id"));
							kycmember.put("groupID", rs.getInt("destination_group_id"));
							return kycmember;
						}
					});

			jdbcTemplate.update("update members set group_id = ? where id = ?;",
					new Object[] { kycmember.get("groupID"), kycmember.get("memberID") });
			return true;
		}
		return false;
	}

	public boolean memberKYCRejectApproval(Integer rejectBy, Integer id, String description) {
		int affectedRows = jdbcTemplate.update(
				"update member_kyc_request set status = 'REJECTED', approved_by = ?, approval_date = NOW(), description = ? where id = ?",
				rejectBy, description, id);
		if (affectedRows == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean memberKYCValidate(Integer validatedBy, Integer id) {
		int affectedRows = jdbcTemplate.update(
				"update member_kyc_request set status = 'PENDING', validated_by = ?, validate_date = NOW(), validated = TRUE where id = ?",
				validatedBy, id);
		if (affectedRows == 1) {
			return true;
		}
		return false;
	}

	public boolean memberKYCRejectValidate(Integer rejectBy, Integer id, String description) {
		int affectedRows = jdbcTemplate.update(
				"update member_kyc_request set status = 'REJECTED', validated_by = ?, validate_date = NOW(), validate_description = ? where id = ?",
				rejectBy, description, id);
		if (affectedRows == 1) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean approvalMemberKYCStatus(Integer memberID) {
		try {
			Boolean approved = this.jdbcTemplate.queryForObject(
					"select approved from member_kyc_request where member_id = ?", new Object[] { memberID },
					Boolean.class);
			if (Boolean.TRUE.equals(approved)) {
				return true;
			} else if (Boolean.FALSE.equals(approved)) {
				return false;
			} else {
				return null;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public String memberKycStatus(Integer memberID) {
		try {
			String status = this.jdbcTemplate.queryForObject(
					"select status from member_kyc_request where member_id = ?", new Object[] { memberID },
					String.class);
			return status;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MemberKYC> loadMemberKYCByID(Integer id) {
		try {
			List<MemberKYC> kyc = this.jdbcTemplate.query("select * from member_kyc_request where id = ?",
					new Object[] { id }, new RowMapper<MemberKYC>() {
						public MemberKYC mapRow(ResultSet rs, int rowNum) throws SQLException {
							MemberKYC kyc = new MemberKYC();
							kyc.setApprovalDate(rs.getTimestamp("approval_date"));
							kyc.setApprovalRequestDate(rs.getTimestamp("validate_date"));
							kyc.setValidateDate(rs.getTimestamp("validate_date"));
							kyc.setRequestedDate(rs.getTimestamp("created_date"));
							kyc.setStatus(rs.getString("status"));
							kyc.setValidated(rs.getBoolean("validated"));
							kyc.setApproved(rs.getBoolean("approved"));
							kyc.setDescription(rs.getString("description"));
							kyc.setValidateDescription(rs.getString("validate_description"));

							Members fromMember = new Members();
							fromMember.setId(rs.getInt("member_id"));
							kyc.setFromMember(fromMember);

							Members validatedMember = new Members();
							validatedMember.setId(rs.getInt("validated_by"));
							kyc.setValidatedMember(validatedMember);

							Members approvedMember = new Members();
							approvedMember.setId(rs.getInt("approved_by"));
							kyc.setApprovedMember(approvedMember);

							kyc.setFormattedRequestedDate(Utils.formatDate(rs.getTimestamp("created_date")));

							Groups group = new Groups();
							group.setId(rs.getInt("destination_group_id"));

							kyc.setGroup(group);
							kyc.setId(rs.getInt("id"));
							kyc.setImagePath1(rs.getString("image_path_1"));
							kyc.setImagePath2(rs.getString("image_path_2"));
							kyc.setImagePath3(rs.getString("image_path_3"));
							return kyc;
						}
					});
			return kyc;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MemberKYC> loadMemberKYC(Integer currentPage, Integer pageSize) {
		try {
			List<MemberKYC> kyc = this.jdbcTemplate.query("select * from member_kyc_request ORDER BY id DESC LIMIT ?,?",
					new Object[] { currentPage, pageSize }, new RowMapper<MemberKYC>() {
						public MemberKYC mapRow(ResultSet rs, int rowNum) throws SQLException {
							MemberKYC kyc = new MemberKYC();
							kyc.setRequestedDate(rs.getTimestamp("created_date"));
							kyc.setValidateDate(rs.getTimestamp("validate_date"));
							kyc.setApprovalRequestDate(rs.getTimestamp("validate_date"));
							kyc.setApprovalDate(rs.getTimestamp("approval_date"));

							kyc.setStatus(rs.getString("status"));
							kyc.setValidated(rs.getBoolean("validated"));
							kyc.setApproved(rs.getBoolean("approved"));
							kyc.setDescription(rs.getString("description"));
							kyc.setValidateDescription(rs.getString("validate_description"));

							Members fromMember = new Members();
							fromMember.setId(rs.getInt("member_id"));
							kyc.setFromMember(fromMember);

							Members validatedMember = new Members();
							validatedMember.setId(rs.getInt("validated_by"));
							kyc.setValidatedMember(validatedMember);

							Members approvedMember = new Members();
							approvedMember.setId(rs.getInt("approved_by"));
							kyc.setApprovedMember(approvedMember);

							kyc.setFormattedRequestedDate(Utils.formatDate(rs.getTimestamp("created_date")));

							Groups group = new Groups();
							group.setId(rs.getInt("destination_group_id"));

							kyc.setGroup(group);
							kyc.setId(rs.getInt("id"));
							kyc.setImagePath1(rs.getString("image_path_1"));
							kyc.setImagePath2(rs.getString("image_path_2"));
							kyc.setImagePath3(rs.getString("image_path_3"));
							return kyc;
						}
					});
			return kyc;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MemberKYC> loadMemberKYCByStatus(Integer currentPage, Integer pageSize, String status) {
		try {
			List<MemberKYC> kyc = this.jdbcTemplate.query(
					"select * from member_kyc_request where status = ? ORDER BY id DESC LIMIT ?,?",
					new Object[] { status, currentPage, pageSize }, new RowMapper<MemberKYC>() {
						public MemberKYC mapRow(ResultSet rs, int rowNum) throws SQLException {
							MemberKYC kyc = new MemberKYC();
							kyc.setApprovalRequestDate(rs.getTimestamp("validate_date"));
							kyc.setApprovalDate(rs.getTimestamp("approval_date"));
							kyc.setValidateDate(rs.getTimestamp("validate_date"));
							kyc.setRequestedDate(rs.getTimestamp("created_date"));

							kyc.setStatus(rs.getString("status"));
							kyc.setValidated(rs.getBoolean("validated"));
							kyc.setApproved(rs.getBoolean("approved"));
							kyc.setDescription(rs.getString("description"));
							kyc.setValidateDescription(rs.getString("validate_description"));

							Members fromMember = new Members();
							fromMember.setId(rs.getInt("member_id"));
							kyc.setFromMember(fromMember);

							Members validatedMember = new Members();
							validatedMember.setId(rs.getInt("validated_by"));
							kyc.setValidatedMember(validatedMember);

							Members approvedMember = new Members();
							approvedMember.setId(rs.getInt("approved_by"));
							kyc.setApprovedMember(approvedMember);

							kyc.setFormattedRequestedDate(Utils.formatDate(rs.getTimestamp("created_date")));

							Groups group = new Groups();
							group.setId(rs.getInt("destination_group_id"));

							kyc.setGroup(group);
							kyc.setId(rs.getInt("id"));
							kyc.setImagePath1(rs.getString("image_path_1"));
							kyc.setImagePath2(rs.getString("image_path_2"));
							kyc.setImagePath3(rs.getString("image_path_3"));
							return kyc;
						}
					});
			return kyc;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MemberKYC> loadMemberKYCByMemberID(Integer memberID) {
		try {
			List<MemberKYC> kyc = this.jdbcTemplate.query("select * from member_kyc_request where member_id = ?",
					new Object[] { memberID }, new RowMapper<MemberKYC>() {
						public MemberKYC mapRow(ResultSet rs, int rowNum) throws SQLException {
							MemberKYC kyc = new MemberKYC();
							kyc.setApprovalRequestDate(rs.getTimestamp("validate_date"));
							kyc.setApprovalDate(rs.getTimestamp("approval_date"));
							kyc.setValidateDate(rs.getTimestamp("validate_date"));
							kyc.setRequestedDate(rs.getTimestamp("created_date"));

							kyc.setStatus(rs.getString("status"));
							kyc.setValidated(rs.getBoolean("validated"));
							kyc.setApproved(rs.getBoolean("approved"));
							kyc.setDescription(rs.getString("description"));
							kyc.setValidateDescription(rs.getString("validate_description"));

							Members fromMember = new Members();
							fromMember.setId(rs.getInt("member_id"));
							kyc.setFromMember(fromMember);

							Members validatedMember = new Members();
							validatedMember.setId(rs.getInt("validated_by"));
							kyc.setValidatedMember(validatedMember);

							Members approvedMember = new Members();
							approvedMember.setId(rs.getInt("approved_by"));
							kyc.setApprovedMember(approvedMember);

							kyc.setFormattedRequestedDate(Utils.formatDate(rs.getTimestamp("created_date")));

							Groups group = new Groups();
							group.setId(rs.getInt("destination_group_id"));

							kyc.setGroup(group);
							kyc.setId(rs.getInt("id"));
							kyc.setImagePath1(rs.getString("image_path_1"));
							kyc.setImagePath2(rs.getString("image_path_2"));
							kyc.setImagePath3(rs.getString("image_path_3"));
							return kyc;
						}
					});
			return kyc;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalKYCMembersByStatus(String status) {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from member_kyc_request where status = ?",
				new Object[] { status }, Integer.class);
		return count;
	}

	@Transactional
	public void createMerchants(RegisterMerchantRequest req, String merchantUsername, Integer createdBy) {
		final RegisterMerchantRequest members = req;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDate = dateFormat.format(req.getMerchantOwner().getDateOfBirth());

		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into members (group_id, name, username, email, msisdn) values (?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, members.getGroupID());
				statement.setString(2, members.getName());
				statement.setString(3, merchantUsername);
				statement.setString(4, members.getEmail());
				statement.setString(5, members.getMsisdn());
				return statement;
			}
		}, holder);

		Integer memberID = holder.getKey().intValue();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into merchant_owner (name, id_card_no, address, date_of_birth, place_of_birth, image_path_1, image_path_2) values (?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, req.getMerchantOwner().getName());
				statement.setString(2, req.getMerchantOwner().getIdCardNo());
				statement.setString(3, req.getMerchantOwner().getAddress());
				statement.setString(4, strDate);
				statement.setString(5, req.getMerchantOwner().getPlaceOfBirth());
				statement.setString(6, req.getMerchantOwner().getImagePath1());
				statement.setString(7, req.getMerchantOwner().getImagePath2());
				return statement;
			}
		}, holder);

		Integer ownerID = holder.getKey().intValue();

		jdbcTemplate.update(
				"insert into merchant_details (member_id, owner_id, category_id, sub_category_id, scale_id, average_trx_value, tax_card_number, permission_number, address, store_photo_path, created_by) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				memberID, ownerID, req.getCategoryID(), req.getSubCategoryID(), req.getScaleID(),
				req.getAverageTrxValue(), req.getTaxCardNo(), req.getPermissionNumber(), req.getAddress(),
				req.getStorePhotoPath(), createdBy);
	}

	public Merchants loadMerchantsByMemberID(Integer memberID) {
		try {
			logger.info("[loadMerchantRequestByMemberID: " + memberID + " ]");
			Merchants merchants = this.jdbcTemplate.queryForObject(
					"select *  from merchant_details md join members m on m.id = md.member_id where member_id = ?", new Object[] { memberID },
					new RowMapper<Merchants>() {
						public Merchants mapRow(ResultSet rs, int rowNum) throws SQLException {
							Merchants merchants = new Merchants();
							merchants.setId(rs.getInt("id"));
							merchants.setUsername(rs.getString("username"));
							merchants.setEmail(rs.getString("email"));
							merchants.setMsisdn(rs.getString("msisdn"));
							merchants.setName(rs.getString("name"));
							merchants.setOwner(loadMerchantOwnerByID(rs.getInt("owner_id")));
							merchants.setCategory(loadMerchantCategoryByID(rs.getInt("category_id")));
							merchants.setSubCategory(loadMerchantSubCategoryByID(rs.getInt("sub_category_id")));
							merchants.setScale(loadMerchantBusinessScaleByID(rs.getInt("scale_id")));
							merchants.setAverageTrxValue(rs.getBigDecimal("average_trx_value"));
							merchants.setTaxCardNumber(rs.getString("tax_card_number"));
							merchants.setPermissionNumber(rs.getString("permission_number"));
							merchants.setAddress(rs.getString("address"));
							merchants.setStorePhotoPath(rs.getString("store_photo_path"));
							merchants.setApproved(rs.getBoolean("approved"));
							
							if (rs.getInt("approved_by") != 0) {
								Members approvedBy = findOneMembers("id", rs.getInt("approved_by"));
								MemberView approvedByMember = new MemberView();
								approvedByMember.setId(approvedBy.getId());
								approvedByMember.setUsername(approvedBy.getUsername());
								approvedByMember.setName(approvedBy.getName());
								merchants.setApprovedBy(approvedByMember);
								merchants.setApprovalDate(rs.getTimestamp("approval_date"));
								merchants.setFormattedApprovalDate(Utils.formatDate(rs.getTimestamp("approval_date")));
							}
							if (rs.getInt("created_by") != 0) {
								Members createdBy = findOneMembers("id", rs.getInt("created_by"));
								MemberView createdByMember = new MemberView();
								createdByMember.setId(createdBy.getId());
								createdByMember.setUsername(createdBy.getUsername());
								createdByMember.setName(createdBy.getName());
								merchants.setCreatedBy(createdByMember);
								merchants.setCreatedDate(rs.getTimestamp("created_date"));
								merchants.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							}
							
							merchants.setStatus(rs.getString("status"));
							merchants.setDescription(rs.getString("description"));
							return merchants;
						}
					});
			return merchants;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Merchants> loadMerchantRequestByMemberID(Integer memberID) {
		try {
			logger.info("[loadMerchantRequestByMemberID: " + memberID + " ]");
			List<Merchants> merchants = this.jdbcTemplate.query(
					"select * from merchant_details md join members m on m.id = md.member_id where member_id = ?",
					new Object[] { memberID }, new RowMapper<Merchants>() {
						public Merchants mapRow(ResultSet rs, int rowNum) throws SQLException {
							Merchants merchants = new Merchants();
							merchants.setId(rs.getInt("id"));
							merchants.setUsername(rs.getString("username"));
							merchants.setEmail(rs.getString("email"));
							merchants.setMsisdn(rs.getString("msisdn"));
							merchants.setName(rs.getString("name"));
							merchants.setOwner(loadMerchantOwnerByID(rs.getInt("owner_id")));
							merchants.setCategory(loadMerchantCategoryByID(rs.getInt("category_id")));
							merchants.setSubCategory(loadMerchantSubCategoryByID(rs.getInt("sub_category_id")));
							merchants.setScale(loadMerchantBusinessScaleByID(rs.getInt("scale_id")));
							merchants.setAverageTrxValue(rs.getBigDecimal("average_trx_value"));
							merchants.setTaxCardNumber(rs.getString("tax_card_number"));
							merchants.setPermissionNumber(rs.getString("permission_number"));
							merchants.setAddress(rs.getString("address"));
							merchants.setStorePhotoPath(rs.getString("store_photo_path"));
							merchants.setApproved(rs.getBoolean("approved"));

							if (rs.getInt("approved_by") != 0) {
								Members approvedBy = findOneMembers("id", rs.getInt("approved_by"));
								MemberView approvedByMember = new MemberView();
								approvedByMember.setId(approvedBy.getId());
								approvedByMember.setUsername(approvedBy.getUsername());
								approvedByMember.setName(approvedBy.getName());
								merchants.setApprovedBy(approvedByMember);
								merchants.setApprovalDate(rs.getTimestamp("approval_date"));
								merchants.setFormattedApprovalDate(Utils.formatDate(rs.getTimestamp("approval_date")));
							}

							if (rs.getInt("created_by") != 0) {
								Members createdBy = findOneMembers("id", rs.getInt("created_by"));
								MemberView createdByMember = new MemberView();
								createdByMember.setId(createdBy.getId());
								createdByMember.setUsername(createdBy.getUsername());
								createdByMember.setName(createdBy.getName());
								merchants.setCreatedBy(createdByMember);
								merchants.setCreatedDate(rs.getTimestamp("created_date"));
								merchants.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							}

							merchants.setStatus(rs.getString("status"));
							merchants.setDescription(rs.getString("description"));
							return merchants;
						}
					});
			return merchants;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Merchants> loadMerchantRequestByMemberID(Integer currentPage, Integer pageSize) {
		try {
			List<Merchants> merchants = this.jdbcTemplate.query(
					"select *  from merchant_details md join members m on m.id = md.member_id LIMIT ?, ?",
					new Object[] { currentPage, pageSize }, new RowMapper<Merchants>() {
						public Merchants mapRow(ResultSet rs, int rowNum) throws SQLException {
							Merchants merchants = new Merchants();
							merchants.setId(rs.getInt("id"));
							merchants.setUsername(rs.getString("username"));
							merchants.setEmail(rs.getString("email"));
							merchants.setMsisdn(rs.getString("msisdn"));
							merchants.setName(rs.getString("name"));
							merchants.setOwner(loadMerchantOwnerByID(rs.getInt("owner_id")));
							merchants.setCategory(loadMerchantCategoryByID(rs.getInt("category_id")));
							merchants.setSubCategory(loadMerchantSubCategoryByID(rs.getInt("sub_category_id")));
							merchants.setScale(loadMerchantBusinessScaleByID(rs.getInt("scale_id")));
							merchants.setAverageTrxValue(rs.getBigDecimal("average_trx_value"));
							merchants.setTaxCardNumber(rs.getString("tax_card_number"));
							merchants.setPermissionNumber(rs.getString("permission_number"));
							merchants.setAddress(rs.getString("address"));
							merchants.setStorePhotoPath(rs.getString("store_photo_path"));
							merchants.setApproved(rs.getBoolean("approved"));

							if (rs.getInt("approved_by") != 0) {
								Members approvedBy = findOneMembers("id", rs.getInt("approved_by"));
								MemberView approvedByMember = new MemberView();
								approvedByMember.setId(approvedBy.getId());
								approvedByMember.setUsername(approvedBy.getUsername());
								approvedByMember.setName(approvedBy.getName());
								merchants.setApprovedBy(approvedByMember);
								merchants.setApprovalDate(rs.getTimestamp("approval_date"));
								merchants.setFormattedApprovalDate(Utils.formatDate(rs.getTimestamp("approval_date")));
							}

							if (rs.getInt("created_by") != 0) {
								Members createdBy = findOneMembers("id", rs.getInt("created_by"));
								MemberView createdByMember = new MemberView();
								createdByMember.setId(createdBy.getId());
								createdByMember.setUsername(createdBy.getUsername());
								createdByMember.setName(createdBy.getName());
								merchants.setCreatedBy(createdByMember);
								merchants.setCreatedDate(rs.getTimestamp("created_date"));
								merchants.setFormattedCreatedDate(Utils.formatDate(rs.getTimestamp("created_date")));
							}

							merchants.setStatus(rs.getString("status"));
							merchants.setDescription(rs.getString("description"));
							return merchants;
						}
					});
			return merchants;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalMerchantRequest() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from merchant_details", Integer.class);
		return count;
	}

	@Transactional
	public boolean merchantApproval(Integer approvedBy, Integer memberID) {
		int affectedRows = jdbcTemplate.update(
				"update merchant_details set status = 'APPROVED', approved_by = ?, approval_date = NOW(), approved = TRUE where member_id = ?",
				approvedBy, memberID);
		if (affectedRows == 1) {
			jdbcTemplate.update(
					"update members set group_id = ? where id = ?",
					20, memberID);
			return true;
		}
		return false;
	}

	public boolean merchantRejectApproval(Integer rejectBy, Integer memberID, String description) {
		int affectedRows = jdbcTemplate.update(
				"update merchant_details set status = 'REJECTED', approved_by = ?, approval_date = NOW(), description = ? where member_id = ?",
				rejectBy, description, memberID);
		if (affectedRows == 1) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public void updateMerchants(UpdateMerchantRequest req) {
		final UpdateMerchantRequest merchants = req;
		int affectedRows = jdbcTemplate.update(
				"update members set group_id = ?, name = ?, email = ?, msisdn = ? where id = ?", merchants.getGroupID(),
				merchants.getName(), merchants.getEmail(), merchants.getMsisdn(), merchants.getId());

		if (affectedRows == 1) {
			int affectedRow = jdbcTemplate.update(
					"update merchant_details set category_id = ?, sub_category_id = ?, scale_id = ?, average_trx_value = ?, tax_card_number = ?, permission_number = ?, address = ?, store_photo_path = ? where member_id = ?",
					merchants.getCategoryID(), merchants.getSubCategoryID(), merchants.getScaleID(),
					merchants.getAverageTrxValue(), merchants.getTaxCardNumber(), merchants.getPermissionNumber(),
					merchants.getAddress(), merchants.getStorePhotoPath(), merchants.getId());

			if (affectedRow == 1) {
				jdbcTemplate.update(
						"update merchant_owner set name = ?, id_card_no = ?, address = ?, date_of_birth = ?, place_of_birth = ?, image_path_1 = ?, image_path_2 = ? where id = ?",
						merchants.getOwner().getName(), merchants.getOwner().getIdCardNo(),
						merchants.getOwner().getAddress(), merchants.getOwner().getDateOfBirth(),
						merchants.getOwner().getPlaceOfBirth(), merchants.getOwner().getImagePath1(),
						merchants.getOwner().getImagePath2(), merchants.getOwner().getId());
			}

		}

	}

	public MerchantCategory loadMerchantCategoryByID(Integer categoryID) {
		try {
			MerchantCategory merchantCategory = this.jdbcTemplate.queryForObject(
					"select *  from merchant_category where id = ?", new Object[] { categoryID },
					new RowMapper<MerchantCategory>() {
						public MerchantCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
							MerchantCategory merchantCategory = new MerchantCategory();
							merchantCategory.setId(rs.getInt("id"));
							merchantCategory.setCode(rs.getString("code"));
							merchantCategory.setName(rs.getString("name"));
							return merchantCategory;
						}
					});
			return merchantCategory;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public MerchantSubCategory loadMerchantSubCategoryByID(Integer subCategoryID) {
		try {
			MerchantSubCategory merchantSubCategory = this.jdbcTemplate.queryForObject(
					"select *  from merchant_sub_category where id = ?", new Object[] { subCategoryID },
					new RowMapper<MerchantSubCategory>() {
						public MerchantSubCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
							MerchantSubCategory merchantSubCategory = new MerchantSubCategory();
							merchantSubCategory.setId(rs.getInt("id"));
							merchantSubCategory.setCode(rs.getString("code"));
							merchantSubCategory.setName(rs.getString("name"));
							return merchantSubCategory;
						}
					});
			return merchantSubCategory;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public MerchantBusinessScale loadMerchantBusinessScaleByID(Integer scaleID) {
		try {
			MerchantBusinessScale businessScale = this.jdbcTemplate.queryForObject(
					"select *  from merchant_business_scale where id = ?", new Object[] { scaleID },
					new RowMapper<MerchantBusinessScale>() {
						public MerchantBusinessScale mapRow(ResultSet rs, int rowNum) throws SQLException {
							MerchantBusinessScale businessScale = new MerchantBusinessScale();
							businessScale.setId(rs.getInt("id"));
							businessScale.setScale(rs.getString("scale"));
							return businessScale;
						}
					});
			return businessScale;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public MerchantOwner loadMerchantOwnerByID(Integer ownerID) {
		try {
			MerchantOwner merchantOwner = this.jdbcTemplate.queryForObject("select *  from merchant_owner where id = ?",
					new Object[] { ownerID }, new RowMapper<MerchantOwner>() {
						public MerchantOwner mapRow(ResultSet rs, int rowNum) throws SQLException {
							MerchantOwner merchantOwner = new MerchantOwner();
							merchantOwner.setId(rs.getInt("id"));
							merchantOwner.setName(rs.getString("name"));
							merchantOwner.setAddress(rs.getString("address"));
							merchantOwner.setDateOfBirth(rs.getDate("date_of_birth"));
							merchantOwner.setIdCardNo(rs.getString("id_card_no"));
							merchantOwner.setPlaceOfBirth(rs.getString("place_of_birth"));
							merchantOwner.setImagePath1(rs.getString("image_path_1"));
							merchantOwner.setImagePath2(rs.getString("image_path_2"));
							return merchantOwner;
						}
					});
			return merchantOwner;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MerchantCategory> loadMerchantCategory(Integer currentPage, Integer pageSize) {
		try {
			List<MerchantCategory> merchantCategory = this.jdbcTemplate.query(
					"select * from merchant_category LIMIT ?, ?", new Object[] { currentPage, pageSize },
					new RowMapper<MerchantCategory>() {
						public MerchantCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
							MerchantCategory merchantCategory = new MerchantCategory();
							merchantCategory.setId(rs.getInt("id"));
							merchantCategory.setCode(rs.getString("code"));
							merchantCategory.setName(rs.getString("name"));
							return merchantCategory;
						}
					});
			return merchantCategory;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalMerchantCategory() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from merchant_category", Integer.class);
		return count;
	}

	public List<MerchantSubCategory> loadMerchantSubCategory(Integer currentPage, Integer pageSize) {
		try {
			List<MerchantSubCategory> merchantSubCategory = this.jdbcTemplate.query(
					"select * from merchant_sub_category LIMIT ?, ?", new Object[] { currentPage, pageSize },
					new RowMapper<MerchantSubCategory>() {
						public MerchantSubCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
							MerchantSubCategory merchantSubCategory = new MerchantSubCategory();
							merchantSubCategory.setId(rs.getInt("id"));
							merchantSubCategory.setCode(rs.getString("code"));
							merchantSubCategory.setName(rs.getString("name"));
							return merchantSubCategory;
						}
					});
			return merchantSubCategory;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalMerchantSubCategory() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from merchant_sub_category", Integer.class);
		return count;
	}

	public List<MerchantBusinessScale> loadMerchantBusinessScale(Integer currentPage, Integer pageSize) {
		try {
			List<MerchantBusinessScale> merchantBusinessScale = this.jdbcTemplate.query(
					"select * from merchant_business_scale LIMIT ?, ?", new Object[] { currentPage, pageSize },
					new RowMapper<MerchantBusinessScale>() {
						public MerchantBusinessScale mapRow(ResultSet rs, int rowNum) throws SQLException {
							MerchantBusinessScale merchantBusinessScale = new MerchantBusinessScale();
							merchantBusinessScale.setId(rs.getInt("id"));
							merchantBusinessScale.setScale(rs.getString("scale"));
							return merchantBusinessScale;
						}
					});
			return merchantBusinessScale;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalMerchantBusinessScale() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from merchant_business_scale", Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
