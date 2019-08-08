package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.bellatrix.data.GlobalConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class GlobalConfigRepository {

	private JdbcTemplate jdbcTemplate;

	public List<GlobalConfigs> loadGlobalConfig() {
		try {
			List<GlobalConfigs> configs = this.jdbcTemplate.query("select * from global_config", new Object[] {},
					new RowMapper<GlobalConfigs>() {
						public GlobalConfigs mapRow(ResultSet rs, int rowNum) throws SQLException {
							GlobalConfigs configs = new GlobalConfigs();
							configs.setId(rs.getInt("id"));
							configs.setDescription(rs.getString("description"));
							configs.setInternalName(rs.getString("internal_name"));
							configs.setName(rs.getString("name"));
							configs.setValue(rs.getString("value"));
							return configs;
						}
					});
			return configs;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public GlobalConfigs loadGlobalConfigByInternalName(String internalName) {
		try {
			GlobalConfigs configs = this.jdbcTemplate.queryForObject(
					"select * from global_config where internal_name = ?", new Object[] { internalName },
					new RowMapper<GlobalConfigs>() {
						public GlobalConfigs mapRow(ResultSet rs, int rowNum) throws SQLException {
							GlobalConfigs configs = new GlobalConfigs();
							configs.setId(rs.getInt("id"));
							configs.setDescription(rs.getString("description"));
							configs.setInternalName(rs.getString("internal_name"));
							configs.setName(rs.getString("name"));
							configs.setValue(rs.getString("value"));
							return configs;
						}
					});
			return configs;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
