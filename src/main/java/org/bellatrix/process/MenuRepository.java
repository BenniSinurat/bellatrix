package org.bellatrix.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.bellatrix.data.ChildMenu;
import org.bellatrix.data.ParentMenu;
import org.bellatrix.data.ParentMenuData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class MenuRepository {

	private JdbcTemplate jdbcTemplate;

	public List<ParentMenuData> loadParentMenuByGroupID(Integer groupID) {
		try {
			List<ParentMenuData> parentMenu = this.jdbcTemplate.query(
					"select b.menu_parent_id as id, c.id as main_menu_id, c.name as main_menu_name, a.name, a.icon, a.sequence from menu_parent a join menu_permission b join menu_main c on a.id = b.menu_parent_id and a.menu_main_id = c.id where b.group_id = ? order by a.sequence;",
					new Object[] { groupID }, new RowMapper<ParentMenuData>() {
						public ParentMenuData mapRow(ResultSet rs, int rowNum) throws SQLException {
							ParentMenuData parentMenu = new ParentMenuData();
							parentMenu.setId(rs.getInt("id"));
							parentMenu.setIcon(rs.getString("icon"));
							parentMenu.setParentMenuName(rs.getString("name"));
							parentMenu.setSequenceNo(rs.getInt("sequence"));
							parentMenu.setMainMenuName(rs.getString("main_menu_name"));
							return parentMenu;
						}
					});
			return parentMenu;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public String loadWelcomeMenuLink(Integer groupID) {
		try {
			String welcomeMenu = this.jdbcTemplate.queryForObject("select link from menu_welcome where group_id = ? ",
					new Object[] { groupID }, String.class);
			return welcomeMenu;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<ChildMenu> loadChildMenuByParentID(List<Integer> parentID) {
		String sql = "select * from menu_child where menu_parent_id in (:parentID) order by sequence;";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("parentID", parentID);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		List<ChildMenu> childMenu = template.query(sql, paramMap, new RowMapper<ChildMenu>() {
			public ChildMenu mapRow(ResultSet rs, int rowNum) throws SQLException {
				ChildMenu childMenu = new ChildMenu();
				childMenu.setId(rs.getInt("id"));
				childMenu.setParentMenuID(rs.getInt("menu_parent_id"));
				childMenu.setChildMenuName(rs.getString("name"));
				childMenu.setLink(rs.getString("link"));
				childMenu.setSequenceNo(rs.getInt("sequence"));
				return childMenu;
			}
		});
		return childMenu;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
