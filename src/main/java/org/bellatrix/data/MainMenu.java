package org.bellatrix.data;

import java.util.List;

public class MainMenu {

	private Integer id;
	private String mainMenuName;
	private List<ParentMenu> parentMenu;

	public String getMainMenuName() {
		return mainMenuName;
	}

	public void setMainMenuName(String mainMenuName) {
		this.mainMenuName = mainMenuName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ParentMenu> getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(List<ParentMenu> parentMenu) {
		this.parentMenu = parentMenu;
	}

}
