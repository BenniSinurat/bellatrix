package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.bellatrix.data.WebServicePermission;
import org.bellatrix.data.WebServices;
import org.bellatrix.services.LoadPermissionByWebServicesRequest;
import org.bellatrix.services.LoadWSByIDRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class WebserviceRepository {

	private JdbcTemplate jdbcTemplate;

	public WebServices validateWebService(String username, String password) {
		try {
			WebServices ws = this.jdbcTemplate.queryForObject(
					"select * from webservices where username = ? and password = ? and active = true",
					new Object[] { username, password }, new RowMapper<WebServices>() {
						public WebServices mapRow(ResultSet rs, int rowNum) throws SQLException {
							WebServices ws = new WebServices();
							ws.setActive(rs.getBoolean("active"));
							ws.setHash(rs.getString("hash"));
							ws.setId(rs.getInt("id"));
							ws.setName(rs.getString("name"));
							ws.setUsername(rs.getString("username"));
							ws.setSecureTransaction(rs.getBoolean("credentials"));
							return ws;
						}
					});
			return ws;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public WebServices loadSecretByUsername(String username) {
		try {
			WebServices ws = this.jdbcTemplate.queryForObject(
					"select * from webservices where username = ? and active = true", new Object[] { username },
					new RowMapper<WebServices>() {
						public WebServices mapRow(ResultSet rs, int rowNum) throws SQLException {
							WebServices ws = new WebServices();
							ws.setActive(rs.getBoolean("active"));
							ws.setId(rs.getInt("id"));
							ws.setName(rs.getString("name"));
							ws.setHash(rs.getString("hash"));
							ws.setSecureTransaction(rs.getBoolean("credentials"));
							return ws;
						}
					});
			return ws;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public boolean validateGroupAccessToWebService(Integer wsID, Integer groupID) {
		try {
			jdbcTemplate.queryForObject(
					"select id from webservice_permissions where webservice_id = ? and group_id = ?",
					new Object[] { wsID, groupID }, Integer.class);
			return true;
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<WebServices> findAllWebServices(Integer offset, Integer limit) {
		try {
			List<WebServices> webservices = this.jdbcTemplate.query(
					"select *  from webservices order by id desc limit " + offset + ", " + limit, new Object[] {},
					new RowMapper<WebServices>() {
						public WebServices mapRow(ResultSet rs, int rowNum) throws SQLException {
							WebServices ws = new WebServices();
							ws.setId(rs.getInt("id"));
							ws.setUsername(rs.getString("username"));
							ws.setName(rs.getString("name"));
							return ws;
						}
					});
			return webservices;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public WebServices findWebServicesByID(Integer id) {
		try {
			WebServices webservices = this.jdbcTemplate.queryForObject("select *  from webservices where id = ?",
					new Object[] { id }, new RowMapper<WebServices>() {
						public WebServices mapRow(ResultSet rs, int rowNum) throws SQLException {
							WebServices ws = new WebServices();
							ws.setId(rs.getInt("id"));
							ws.setUsername(rs.getString("username"));
							ws.setName(rs.getString("name"));
							ws.setActive(rs.getBoolean("active"));
							ws.setSecureTransaction(rs.getBoolean("credentials"));
							ws.setPassword(rs.getString("password"));
							return ws;
						}
					});
			return webservices;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer countTotalWebServices() {
		int count = this.jdbcTemplate.queryForObject("select  count(id) from webservices", Integer.class);
		return count;
	}

	public void createWebService(WebServices req) {
		jdbcTemplate.update(
				"insert into webservices (username, password, name, hash, active, credentials) values (?, ?, ?, ?, ?, ?)",
				req.getUsername(), req.getPassword(), req.getName(), req.getHash(), req.isActive(), req.isSecureTransaction());
	}

	public void createWebServicePermission(Integer webserviceID, Integer groupID) {
		jdbcTemplate.update("insert into webservice_permissions (webservice_id, group_id) values (?, ?)", webserviceID,
				groupID);
	}

	public List<WebServicePermission> listPermissionByWebservice(Integer id) {
		try {
			List<WebServicePermission> webService = this.jdbcTemplate.query(
					"select ttp.id, ttp.webservice_id, tt.name, g.id as groupId, g.name as groupName from webservice_permissions ttp inner join groups g on ttp.group_id = g.id inner join webservices tt on ttp.webservice_id = tt.id where ttp.webservice_id = ?",
					new Object[] { id }, new RowMapper<WebServicePermission>() {
						public WebServicePermission mapRow(ResultSet rs, int rowNum) throws SQLException {
							WebServicePermission webService = new WebServicePermission();
							webService.setId(rs.getInt("id"));
							webService.setWebServiceId(rs.getInt("webservice_id"));
							webService.setName(rs.getString("name"));
							webService.setGroupId(rs.getInt("groupId"));
							webService.setGroupName(rs.getString("groupName"));
							return webService;
						}
					});
			return webService;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void updatePermissionWebservice(LoadPermissionByWebServicesRequest req) {
		jdbcTemplate.update("update webservice_permissions set webservice_id = ?, group_id = ? where id = ?",
				req.getWebServiceID(), req.getGroupID(), req.getId());
	}

	public void deletePermissionWebservice(LoadWSByIDRequest req) {
		jdbcTemplate.update("delete from webservice_permissions where id = ?", new Object[] { req.getId() });
	}

	public Integer countTotalWebServicePermission(Integer webserviceID) {
		int count = this.jdbcTemplate.queryForObject(
				"select  count(*) from webservice_permissions where webservice_id = ?", Integer.class,
				new Object[] { webserviceID });
		return count;
	}
}
