package org.bellatrix.process;

import java.util.List;

import javax.xml.ws.Holder;

import org.bellatrix.data.Groups;
import org.bellatrix.data.Header;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.CreateGroupsRequest;
import org.bellatrix.services.Group;
import org.bellatrix.services.LoadGroupsByIDRequest;
import org.bellatrix.services.LoadGroupsByIDResponse;
import org.bellatrix.services.LoadGroupsRequest;
import org.bellatrix.services.LoadGroupsResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupServiceImpl implements Group {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private WebserviceValidation webserviceValidation;

	@Override
	public LoadGroupsResponse loadAllGroups(Holder<Header> headerParam, LoadGroupsRequest req) {
		LoadGroupsResponse groupsResponse = new LoadGroupsResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				req.setCurrentPage(0);
				req.setPageSize(0);
			}
			List<Groups> groups = baseRepository.getGroupsRepository().loadAllGroups(req.getCurrentPage(),
					req.getPageSize());
			
			Integer totalRecords = baseRepository.getGroupsRepository().countTotalGroups();
			groupsResponse.setGroupsList(groups);
			groupsResponse.setTotalRecords(totalRecords);
			groupsResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return groupsResponse;
		} catch (TransactionException e) {
			groupsResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return groupsResponse;
		}
	}

	@Override
	public void createGroups(Holder<Header> headerParam, CreateGroupsRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		baseRepository.getGroupsRepository().createGroups(req.getGroups());
	}

	@Override
	public LoadGroupsByIDResponse loadGroupsByID(Holder<Header> headerParam, LoadGroupsByIDRequest req) {
		LoadGroupsByIDResponse groupsResponse = new LoadGroupsByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Groups groups = baseRepository.getGroupsRepository().loadGroupsByID(req.getId());
			groupsResponse.setGroups(groups);
			groupsResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return groupsResponse;
		} catch (TransactionException e) {
			groupsResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return groupsResponse;
		}

	}

}
