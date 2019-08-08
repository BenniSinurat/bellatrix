package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.bellatrix.data.MemberCustomFields;
import org.bellatrix.data.MemberFields;
import org.bellatrix.data.PaymentCustomFields;
import org.bellatrix.data.PaymentFields;
import org.bellatrix.services.CreateMemberCustomFieldsRequest;
import org.bellatrix.services.CreatePaymentCustomFieldsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class CustomFieldRepository {

	private JdbcTemplate jdbcTemplate;

	public void createMemberCustomField(CreateMemberCustomFieldsRequest req) {
		this.jdbcTemplate.update("insert into member_custom_fields (group_id, internal_name, name) values (?, ?, ?)",
				req.getGroupID(), req.getInternalName(), req.getName());
	}

	public MemberCustomFields loadMemberCustomFields(String fieldName, Object req) {
		try {
			MemberCustomFields customfield = this.jdbcTemplate.queryForObject(
					"select * from member_custom_fields where " + fieldName + " = ?", new Object[] { req },
					new RowMapper<MemberCustomFields>() {
						public MemberCustomFields mapRow(ResultSet rs, int rowNum) throws SQLException {
							MemberCustomFields customfield = new MemberCustomFields();
							customfield.setId(rs.getInt("id"));
							customfield.setGroupID(rs.getInt("group_id"));
							customfield.setInternalName(rs.getString("internal_name"));
							customfield.setName(rs.getString("name"));
							customfield.setCreatedDate(rs.getDate("created_date"));
							return customfield;
						}
					});
			return customfield;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<MemberCustomFields> loadFieldsByGroupID(Integer req) {
		List<MemberCustomFields> customfield = this.jdbcTemplate.query(
				"select * from member_custom_fields where group_id = ?", new Object[] { req },
				new RowMapper<MemberCustomFields>() {
					public MemberCustomFields mapRow(ResultSet rs, int rowNum) throws SQLException {
						MemberCustomFields customfield = new MemberCustomFields();
						customfield.setId(rs.getInt("id"));
						customfield.setGroupID(rs.getInt("group_id"));
						customfield.setInternalName(rs.getString("internal_name"));
						customfield.setName(rs.getString("name"));
						customfield.setCreatedDate(rs.getDate("created_date"));
						return customfield;
					}
				});
		return customfield;
	}

	public List<MemberFields> loadFieldValuesByUsername(String req) {
		List<MemberFields> memberfield = this.jdbcTemplate.query(
				"select * from member_custom_field_values a join members b on a.member_id=b.id where b.username=?",
				new Object[] { req }, new RowMapper<MemberFields>() {
					public MemberFields mapRow(ResultSet rs, int rowNum) throws SQLException {
						MemberFields memberfield = new MemberFields();
						memberfield.setMemberCustomFieldID(rs.getInt("member_custom_field_id"));
						memberfield.setInternalName(rs.getString("internal_name"));
						memberfield.setValue(rs.getString("value"));
						return memberfield;
					}
				});
		return memberfield;
	}

	public List<MemberFields> loadFieldValuesByMemberID(Integer req) {
		List<MemberFields> memberfield = this.jdbcTemplate.query(
				"select * from member_custom_field_values join member_custom_fields on member_custom_field_values.member_custom_field_id = member_custom_fields.id where member_custom_field_values.member_id =?",
				new Object[] { req }, new RowMapper<MemberFields>() {
					public MemberFields mapRow(ResultSet rs, int rowNum) throws SQLException {
						MemberFields memberfield = new MemberFields();
						memberfield.setMemberCustomFieldID(rs.getInt("member_custom_field_id"));
						memberfield.setInternalName(rs.getString("internal_name"));
						memberfield.setName(rs.getString("name"));
						memberfield.setValue(rs.getString("value"));
						return memberfield;
					}
				});
		return memberfield;
	}

	public List<MemberFields> loadFieldValuesByMemberID(List<Integer> req) {
		String sql = "select * from member_custom_field_values join member_custom_fields on member_custom_field_values.member_custom_field_id = member_custom_fields.id where member_custom_field_values.member_id in (:memberID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("memberID", req);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		List<MemberFields> memberfield = template.query(sql, paramMap, new RowMapper<MemberFields>() {
			public MemberFields mapRow(ResultSet rs, int rowNum) throws SQLException {
				MemberFields memberfield = new MemberFields();
				memberfield.setMemberCustomFieldID(rs.getInt("member_custom_field_id"));
				memberfield.setInternalName(rs.getString("internal_name"));
				memberfield.setName(rs.getString("name"));
				memberfield.setValue(rs.getString("value"));
				return memberfield;
			}
		});
		return memberfield;
	}

	public void createPaymentCustomField(CreatePaymentCustomFieldsRequest req) {
		this.jdbcTemplate.update("insert into payment_custom_fields (account_id, internal_name, name) values (?, ?, ?)",
				req.getAccountID(), req.getInternalName(), req.getName());
	}

	public List<PaymentCustomFields> loadPaymentCustomFieldByTransferType(Integer req) {
		List<PaymentCustomFields> paymentfield = this.jdbcTemplate.query(
				"select * from payment_custom_fields where transfer_type_id = ?", new Object[] { req },
				new RowMapper<PaymentCustomFields>() {
					public PaymentCustomFields mapRow(ResultSet rs, int rowNum) throws SQLException {
						PaymentCustomFields paymentfield = new PaymentCustomFields();
						paymentfield.setId(rs.getInt("id"));
						paymentfield.setTransferTypeID(rs.getInt("transfer_type_id"));
						paymentfield.setInternalName(rs.getString("internal_name"));
						paymentfield.setName(rs.getString("name"));
						paymentfield.setCreatedDate(rs.getDate("created_date"));
						return paymentfield;
					}
				});
		return paymentfield;
	}

	public List<PaymentFields> loadPaymentFieldValuesByTransferID(Integer req) {
		List<PaymentFields> paymentfield = this.jdbcTemplate.query(
				"select * from payment_custom_field_values where transfer_id = ?", new Object[] { req },
				new RowMapper<PaymentFields>() {
					public PaymentFields mapRow(ResultSet rs, int rowNum) throws SQLException {
						PaymentFields paymentfield = new PaymentFields();
						paymentfield.setId(rs.getInt("id"));
						paymentfield.setTransferID(rs.getInt("transfer_id"));
						paymentfield.setInternalName(rs.getString("internal_name"));
						paymentfield.setPaymentCustomFieldID(rs.getInt("payment_custom_field_id"));
						paymentfield.setValue(rs.getString("value"));
						return paymentfield;
					}
				});
		return paymentfield;
	}

	public List<PaymentFields> loadMultiPaymentFieldValuesByTransferID(List<Integer> req) {
		String sql = "select * from payment_custom_field_values where transfer_id in (:transferID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("transferID", req);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		List<PaymentFields> paymentfield = template.query(sql, paramMap, new RowMapper<PaymentFields>() {
			public PaymentFields mapRow(ResultSet rs, int rowNum) throws SQLException {
				PaymentFields paymentfield = new PaymentFields();
				paymentfield.setId(rs.getInt("id"));
				paymentfield.setTransferID(rs.getInt("transfer_id"));
				paymentfield.setInternalName(rs.getString("internal_name"));
				paymentfield.setPaymentCustomFieldID(rs.getInt("payment_custom_field_id"));
				paymentfield.setValue(rs.getString("value"));
				return paymentfield;
			}
		});
		return paymentfield;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
