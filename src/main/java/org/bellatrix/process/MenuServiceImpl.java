package org.bellatrix.process;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.xml.ws.Holder;

import org.bellatrix.data.ChildMenu;
import org.bellatrix.data.Groups;
import org.bellatrix.data.Header;
import org.bellatrix.data.MainMenu;
import org.bellatrix.data.ParentMenu;
import org.bellatrix.data.ParentMenuData;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.LoadMenuByGroupsRequest;
import org.bellatrix.services.LoadMenuByGroupsResponse;
import org.bellatrix.services.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuServiceImpl implements Menu {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private BaseRepository baseRepository;

	@Override
	public LoadMenuByGroupsResponse loadMenuByGroups(Holder<Header> headerParam, LoadMenuByGroupsRequest req) {
		LoadMenuByGroupsResponse loadMenuByGroupsResponse = new LoadMenuByGroupsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Groups groups = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
			if (groups == null) {
				loadMenuByGroupsResponse.setRepsonse(StatusBuilder.getStatus(Status.INVALID_GROUP));
				return loadMenuByGroupsResponse;
			}

			List<ParentMenuData> parentList = baseRepository.getMenuRepository()
					.loadParentMenuByGroupID(req.getGroupID());

			List<Integer> parentIDs = new LinkedList<Integer>();
			for (ParentMenuData p : parentList) {
				parentIDs.add(p.getId());
			}

			if (parentIDs.size() != 0) {
				List<ChildMenu> childList = baseRepository.getMenuRepository().loadChildMenuByParentID(parentIDs);

				Map<Integer, List<ChildMenu>> childMap = childList.stream()
						.collect(Collectors.groupingBy(ChildMenu::getParentMenuID, Collectors.toList()));

				Map<String, List<ParentMenuData>> mainMenuMap = parentList.stream()
						.collect(Collectors.groupingBy(ParentMenuData::getMainMenuName, Collectors.toList()));

				Map<String, List<ParentMenuData>> mainMap = new TreeMap<String, List<ParentMenuData>>();
				mainMap.putAll(mainMenuMap);

				List<MainMenu> listMainMenu = new LinkedList<MainMenu>();
				for (Map.Entry<String, List<ParentMenuData>> entry : mainMap.entrySet()) {
					MainMenu mainMenu = new MainMenu();
					mainMenu.setMainMenuName(entry.getKey());
					List<ParentMenu> listParentMenu = new LinkedList<ParentMenu>();
					for (int i = 0; i < entry.getValue().size(); i++) {
						ParentMenu parentMenu = new ParentMenu();
						parentMenu.setIcon(entry.getValue().get(i).getIcon());
						parentMenu.setParentMenuName(entry.getValue().get(i).getParentMenuName());
						parentMenu.setSequenceNo(entry.getValue().get(i).getSequenceNo());
						parentMenu.setId(entry.getValue().get(i).getId());
						parentMenu.setChildMenu(childMap.get(entry.getValue().get(i).getId()));
						parentMenu.setMainMenuid(entry.getValue().get(i).getMainMenuID());
						listParentMenu.add(parentMenu);
					}
					mainMenu.setParentMenu(listParentMenu);
					listMainMenu.add(mainMenu);
				}

				loadMenuByGroupsResponse.setMainMenu(listMainMenu);
				loadMenuByGroupsResponse
						.setWelcomeMenu(baseRepository.getMenuRepository().loadWelcomeMenuLink(groups.getId()));
				loadMenuByGroupsResponse.setRepsonse(StatusBuilder.getStatus(Status.PROCESSED));
			}

			loadMenuByGroupsResponse.setRepsonse(StatusBuilder.getStatus(Status.PROCESSED));

		} catch (TransactionException ex) {
			loadMenuByGroupsResponse.setRepsonse(StatusBuilder.getStatus(ex.getMessage()));
			return loadMenuByGroupsResponse;
		}
		return loadMenuByGroupsResponse;
	}

}
