package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.MainMenu;
import org.bellatrix.data.ResponseStatus;

public class LoadMenuByGroupsResponse {

	private List<MainMenu> mainMenu;
	private String welcomeMenu;
	private ResponseStatus repsonse;

	public List<MainMenu> getMainMenu() {
		return mainMenu;
	}

	public void setMainMenu(List<MainMenu> mainMenu) {
		this.mainMenu = mainMenu;
	}

	public ResponseStatus getRepsonse() {
		return repsonse;
	}

	public void setRepsonse(ResponseStatus repsonse) {
		this.repsonse = repsonse;
	}

	public String getWelcomeMenu() {
		return welcomeMenu;
	}

	public void setWelcomeMenu(String welcomeMenu) {
		this.welcomeMenu = welcomeMenu;
	}

}
