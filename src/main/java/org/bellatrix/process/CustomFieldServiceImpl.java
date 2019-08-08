package org.bellatrix.process;

import java.util.List;

import javax.xml.ws.Holder;

import org.bellatrix.data.MemberCustomFields;
import org.bellatrix.data.Header;
import org.bellatrix.services.CreateMemberCustomFieldsRequest;
import org.bellatrix.services.CreatePaymentCustomFieldsRequest;
import org.bellatrix.services.CustomField;
import org.bellatrix.services.LoadCustomFieldsByGroupIDRequest;
import org.bellatrix.services.LoadCustomFieldsByIDRequest;
import org.bellatrix.services.LoadCustomFieldsByInternalNameRequest;
import org.bellatrix.services.LoadMemberCustomFieldsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomFieldServiceImpl implements CustomField {

	@Autowired
	private BaseRepository baseRepository;

	@Override
	public LoadMemberCustomFieldsResponse loadMemberCustomFieldsByID(Holder<Header> headerParam,
			LoadCustomFieldsByIDRequest req) {
		LoadMemberCustomFieldsResponse cfresponse = new LoadMemberCustomFieldsResponse();
		MemberCustomFields cf = baseRepository.getCustomFieldRepository().loadMemberCustomFields("id", req.getId());
		cfresponse.getCustomFields().add(cf);
		return cfresponse;
	}

	@Override
	public LoadMemberCustomFieldsResponse loadMemberCustomFieldsByGroup(Holder<Header> headerParam,
			LoadCustomFieldsByGroupIDRequest req) {
		LoadMemberCustomFieldsResponse cfresponse = new LoadMemberCustomFieldsResponse();
		List<MemberCustomFields> cf = baseRepository.getCustomFieldRepository().loadFieldsByGroupID(req.getGroupID());
		cfresponse.setCustomFields(cf);
		return cfresponse;
	}

	@Override
	public LoadMemberCustomFieldsResponse loadMemberCustomFieldsByInternalName(Holder<Header> headerParam,
			LoadCustomFieldsByInternalNameRequest req) {
		LoadMemberCustomFieldsResponse cfresponse = new LoadMemberCustomFieldsResponse();
		MemberCustomFields cf = baseRepository.getCustomFieldRepository().loadMemberCustomFields("internal_name",
				req.getInternalName());
		cfresponse.getCustomFields().add(cf);
		return cfresponse;
	}

	@Override
	@Transactional
	public void createMemberCustomFields(Holder<Header> headerParam, CreateMemberCustomFieldsRequest req)
			throws Exception {
		baseRepository.getCustomFieldRepository().createMemberCustomField(req);
	}

	@Override
	@Transactional
	public void createPaymentCustomFields(Holder<Header> headerParam, CreatePaymentCustomFieldsRequest req)
			throws Exception {
		baseRepository.getCustomFieldRepository().createPaymentCustomField(req);
	}

}
