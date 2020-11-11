package org.bellatrix.process;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.ws.Holder;

import org.bellatrix.data.MemberCustomFields;
import org.apache.log4j.Logger;
import org.bellatrix.data.Billers;
import org.bellatrix.data.ExternalMemberFields;
import org.bellatrix.data.Groups;
import org.bellatrix.data.Header;
import org.bellatrix.data.MemberFields;
import org.bellatrix.data.MemberKYC;
import org.bellatrix.data.Members;
import org.bellatrix.data.MerchantBusinessScale;
import org.bellatrix.data.MerchantCategory;
import org.bellatrix.data.MerchantOwner;
import org.bellatrix.data.MerchantSubCategory;
import org.bellatrix.data.Merchants;
import org.bellatrix.data.Notifications;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.ConfirmKYCRequest;
import org.bellatrix.services.ConfirmKYCResponse;
import org.bellatrix.services.ConfirmMerchantRequest;
import org.bellatrix.services.ConfirmMerchantResponse;
import org.bellatrix.services.CreateCredentialRequest;
import org.bellatrix.services.LoadKYCRequest;
import org.bellatrix.services.LoadKYCResponse;
import org.bellatrix.services.LoadMembersByExternalIDRequest;
import org.bellatrix.services.LoadMembersByGroupIDRequest;
import org.bellatrix.services.LoadMembersByIDRequest;
import org.bellatrix.services.LoadMembersByUsernameRequest;
import org.bellatrix.services.LoadMembersRequest;
import org.bellatrix.services.LoadMembersResponse;
import org.bellatrix.services.LoadMerchantBusinessScaleRequest;
import org.bellatrix.services.LoadMerchantBusinessScaleResponse;
import org.bellatrix.services.LoadMerchantByUsernameRequest;
import org.bellatrix.services.LoadMerchantByUsernameResponse;
import org.bellatrix.services.LoadMerchantCategoryRequest;
import org.bellatrix.services.LoadMerchantCategoryResponse;
import org.bellatrix.services.LoadMerchantRequest;
import org.bellatrix.services.LoadMerchantResponse;
import org.bellatrix.services.LoadMerchantSubCategoryRequest;
import org.bellatrix.services.LoadMerchantSubCategoryResponse;
import org.bellatrix.services.Member;
import org.bellatrix.services.MemberKYCRequest;
import org.bellatrix.services.RegisterMemberRequest;
import org.bellatrix.services.RegisterMerchantRequest;
import org.bellatrix.services.SubscribeMemberRequest;
import org.bellatrix.services.UpdateMemberRequest;
import org.bellatrix.services.UpdateMerchantRequest;
import org.bellatrix.services.ValidateKYCRequest;
import org.bellatrix.services.ValidateKYCResponse;
import org.json.JSONObject;
import org.mule.module.client.MuleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements Member {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private Configurator configurator;
	@Autowired
	private AccessRepository accessRepository;

	@Value("${tyk.host}")
	private String tykHost;
	@Value("${tyk.authorization}")
	private String tykAuthorization;
	@Value("${api.management.id}")
	private String apiManagementID;
	@Value("${api.management.name}")
	private String apiManagementName;
	@Value("$(host.sandbox.url)")
	private String sandboxUrl;
	@Value("$(host.dashboard.url)")
	private String dashboardUrl;

	private Logger logger = Logger.getLogger(MemberServiceImpl.class);

	@Override
	public LoadMembersResponse loadMembersByID(Holder<Header> headerParam, LoadMembersByIDRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		List<Members> memberList = new LinkedList<Members>();

		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = baseRepository.getMembersRepository().findOneMembers("id", req.getId());
			if (members == null) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}

			List<ExternalMemberFields> extID = baseRepository.getMembersRepository()
					.loadExternalMemberFields(members.getId());
			List<MemberFields> memberfields = baseRepository.getCustomFieldRepository()
					.loadFieldValuesByMemberID(members.getId());
			List<Billers> billers = baseRepository.getBillPaymentRepository().loadBillerFromMember(members.getId());

			Boolean kycStatus = baseRepository.getMembersRepository().approvalMemberKYCStatus(members.getId());

			members.setKycStatus(kycStatus);
			memberList.add(members);
			members.setCustomFields(memberfields);
			members.setExternalMembers(extID);
			members.setBillers(billers);
			loadMembers.setMembers(memberList);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public LoadMembersResponse loadMembersByGroupID(Holder<Header> headerParam, LoadMembersByGroupIDRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Members> members = baseRepository.getMembersRepository().loadMembersByGroupID(req.getGroupID(),
					req.getCurrentPage(), req.getPageSize());
			if (members.size() == 0) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}

			List<Integer> memberIDs = new LinkedList<Integer>();
			for (Members m : members) {
				memberIDs.add(m.getId());
			}

			List<MemberFields> mfield = baseRepository.getCustomFieldRepository().loadFieldValuesByMemberID(memberIDs);

			Map<Integer, List<MemberFields>> result = mfield.stream()
					.collect(Collectors.groupingBy(MemberFields::getMemberCustomFieldID, Collectors.toList()));

			for (int i = 0; i < members.size(); i++) {
				members.get(i).setCustomFields(result.get(i));
			}

			Integer totalRecords = baseRepository.getMembersRepository().countTotalMembers();

			loadMembers.setMembers(members);
			loadMembers.setTotalRecords(totalRecords);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}

	}

	@Override
	public void registerMembers(Holder<Header> headerParam, RegisterMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Groups groups = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
			if (groups == null) {
				throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
			}

			if (req.getExternalMemberFields() != null && req.getExternalMemberFields().getParentID() != 0) {
				Members parent = baseRepository.getMembersRepository().findOneMembers("id",
						req.getExternalMemberFields().getParentID());
				if (parent == null) {
					throw new TransactionException(String.valueOf(Status.PARENT_ID_NOT_FOUND));
				}
			}

			if (req.getCustomFields() != null) {
				List<MemberCustomFields> fields = baseRepository.getCustomFieldRepository()
						.loadFieldsByGroupID(req.getGroupID());
				List<String> cfRefInternalName = new LinkedList<String>();
				List<Integer> cfRefID = new LinkedList<Integer>();
				for (int i = 0; i < fields.size(); i++) {
					cfRefInternalName.add(fields.get(i).getInternalName());
					cfRefID.add(fields.get(i).getId());
				}
				List<String> cfReqInternalName = new LinkedList<String>();
				List<Integer> cfRegID = new LinkedList<Integer>();
				for (int i = 0; i < req.getCustomFields().size(); i++) {
					cfReqInternalName.add(req.getCustomFields().get(i).getInternalName());
					cfRegID.add(req.getCustomFields().get(i).getMemberCustomFieldID());
				}
				cfReqInternalName.removeAll(cfRefInternalName);
				if (cfReqInternalName.size() != 0) {
					throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
				}

				cfRegID.removeAll(cfRefID);
				if (cfRegID.size() != 0) {
					throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
				}
			}
			baseRepository.getMembersRepository().createMembers(req);
		} catch (DuplicateKeyException ex) {
			throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
		}
	}

	@Override
	public LoadMembersResponse loadMembersByExternalID(Holder<Header> headerParam, LoadMembersByExternalIDRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Members> members = new LinkedList<Members>();

			if (req.getExternalID() != null) {
				members = baseRepository.getMembersRepository().findMembersByExternalID(req);
			} else {
				if (req.getCurrentPage() == null || req.getPageSize() == null) {
					req.setCurrentPage(0);
					req.setPageSize(0);
				}
				members = baseRepository.getMembersRepository().loadMembersByExternalID(req);
			}

			if (members.size() == 0) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}

			List<Integer> memberIDs = new LinkedList<Integer>();
			for (Members m : members) {
				memberIDs.add(m.getId());
			}

			List<MemberFields> mfield = baseRepository.getCustomFieldRepository().loadFieldValuesByMemberID(memberIDs);

			Map<Integer, List<MemberFields>> result = mfield.stream()
					.collect(Collectors.groupingBy(MemberFields::getMemberCustomFieldID, Collectors.toList()));

			for (int i = 0; i < members.size(); i++) {
				members.get(i).setCustomFields(result.get(i));
			}

			Integer totalRecords = baseRepository.getMembersRepository().countTotalExternalMembers(req.getPartnerID());
			loadMembers.setMembers(members);
			loadMembers.setTotalRecords(totalRecords);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public LoadMembersResponse loadMembersByUsername(Holder<Header> headerParam, LoadMembersByUsernameRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		List<Members> memberList = new LinkedList<Members>();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = baseRepository.getMembersRepository().findOneMembers("username", req.getUsername());
			if (members == null) {
				loadMembers.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMembers;
			}
			List<ExternalMemberFields> extID = baseRepository.getMembersRepository()
					.loadExternalMemberFields(members.getId());
			List<MemberFields> memberfields = baseRepository.getCustomFieldRepository()
					.loadFieldValuesByUsername(req.getUsername());
			List<Billers> billers = baseRepository.getBillPaymentRepository().loadBillerFromMember(members.getId());

			Boolean kycStatus = baseRepository.getMembersRepository().approvalMemberKYCStatus(members.getId());
			if (kycStatus == null) {
				members.setKycStatus(false);
			} else {
				members.setKycStatus(kycStatus);
			}

			members.setCustomFields(memberfields);
			members.setExternalMembers(extID);
			memberList.add(members);
			members.setBillers(billers);
			loadMembers.setMembers(memberList);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public void updateMembers(Holder<Header> headerParam, UpdateMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);
			req.setId(fromMember.getId());
			if (req.getGroupID() != null && fromMember.getGroupID() != req.getGroupID()) {
				Groups group = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
				if (group == null) {
					throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
				}
			} else {
				req.setGroupID(fromMember.getGroupID());
			}

			if (req.getEmail() == null) {
				req.setEmail(fromMember.getEmail());
			}

			if (req.getEmailVerify() == null) {
				req.setEmailVerify(fromMember.getEmailVerify());
			}

			if (req.getName() == null) {
				req.setName(fromMember.getName());
			}

			if (req.getMsisdn() == null) {
				req.setMsisdn(fromMember.getMsisdn());
			}

			if (req.getIdCardNo() == null) {
				req.setIdCardNo(fromMember.getIdCardNo());
			}

			if (req.getMotherMaidenName() == null) {
				req.setMotherMaidenName(fromMember.getMotherMaidenName());
			}

			if (req.getAddress() == null) {
				req.setAddress(fromMember.getAddress());
			}

			if (req.getPlaceOfBirth() == null) {
				req.setPlaceOfBirth(fromMember.getPlaceOfBirth());
			}

			if (req.getDateOfBirth() == null) {
				req.setDateOfBirth(fromMember.getDateOfBirth());
			}

			if (req.getNationality() == null) {
				req.setNationality(fromMember.getNationality());
			}

			if (req.getWork() == null) {
				req.setWork(fromMember.getWork());
			}

			if (req.getSex() == null) {
				req.setSex(fromMember.getSex());
			}
			
			if (req.getUid() == null) {
				req.setUid(fromMember.getUid());
			}

			baseRepository.getMembersRepository().updateMembers(req);
		} catch (DataIntegrityViolationException e) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}
	}

	@Override
	public void registerExternalMembers(Holder<Header> headerParam, SubscribeMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members parentMember = memberValidation.validateMemberID(req.getExternalMemberFields().getParentID(), true);
			Members fromMember = memberValidation.validateMember(req.getExternalMemberFields().getUsername(), true);
			Integer id = baseRepository.getMembersRepository().validateExternalMember(fromMember.getId(),
					parentMember.getId());
			if (id != null) {
				baseRepository.getMembersRepository().resubscribeMembers(id,
						req.getExternalMemberFields().getExternalID(), req.getExternalMemberFields().getDescription());
			} else {
				baseRepository.getMembersRepository().subscribeMembers(parentMember, fromMember,
						req.getExternalMemberFields().getExternalID(), req.getExternalMemberFields().getDescription());
			}
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void unregisterExternalMembers(Holder<Header> headerParam, SubscribeMemberRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members parentMember = memberValidation.validateMemberID(req.getExternalMemberFields().getParentID(), true);
			Members fromMember = memberValidation.validateMember(req.getExternalMemberFields().getUsername(), true);
			Integer id = baseRepository.getMembersRepository().validateExternalMember(fromMember.getId(),
					parentMember.getId(), req.getExternalMemberFields().getExternalID());
			if (id == null) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			}
			baseRepository.getMembersRepository().unsubscribeMembers(parentMember,
					req.getExternalMemberFields().getExternalID());
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}

	}

	@Override
	public void membersKYCRequest(Holder<Header> headerParam, MemberKYCRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);
			Groups group = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
			if (group == null) {
				throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
			}
			Boolean approvalStatusKYC = baseRepository.getMembersRepository()
					.approvalMemberKYCStatus(fromMember.getId());
			logger.info("[STATUS KYC (" + req.getUsername() + " :" + approvalStatusKYC + ")]");
			if (Boolean.FALSE.equals(approvalStatusKYC)) {
				String status = baseRepository.getMembersRepository().memberKycStatus(fromMember.getId());
				if (status.equalsIgnoreCase("REJECTED")) {
					logger.info("[STATUS KYC : REJECTED]");
					baseRepository.getMembersRepository().memberKycRequest(fromMember.getId(), req);
				} else if (status.equalsIgnoreCase("PENDING")) {
					logger.info("[STATUS KYC : PENDING]");
					throw new TransactionException(String.valueOf(Status.KYC_PENDING));
				} else if (status.equalsIgnoreCase("REQUESTED")) {
					logger.info("[STATUS KYC : REQUESTED]");
					throw new TransactionException(String.valueOf(Status.KYC_PENDING));
				} else {
					throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
				}
			} else if (Boolean.TRUE.equals(approvalStatusKYC)) {
				throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
			} else {
				logger.info("[NEW KYC (" + req.getUsername() + " :" + approvalStatusKYC + ")]");
				baseRepository.getMembersRepository().memberKYCRequest(fromMember.getId(), req);
			}
		} catch (TransactionException e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public ConfirmKYCResponse confirmKYCRequest(Holder<Header> headerParam, ConfirmKYCRequest req) throws Exception {
		ConfirmKYCResponse confirmKYCResponse = new ConfirmKYCResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);

			Integer id = baseRepository.getMembersRepository().loadKYCMemberByID(req.getId());
			String email = baseRepository.getMembersRepository().findOneMembers("id", id).getEmail();

			if (req.isAccepted()) {
				if (baseRepository.getMembersRepository().memberKYCApproval(member.getId(), req.getId())) {
					confirmKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
					MuleClient client;
					client = new MuleClient(configurator.getMuleContext());
					Map<String, Object> header = new HashMap<String, Object>();
					client.dispatch("KYCNotificationVM", email, header);
				} else {
					confirmKYCResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				}
			} else {
				baseRepository.getMembersRepository().memberKYCRejectApproval(member.getId(), req.getId(),
						req.getDescription());
				MuleClient client;
				client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				client.dispatch("KYCNotificationRejectVM", email, header);
				confirmKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			}
			return confirmKYCResponse;
		} catch (TransactionException e) {
			confirmKYCResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return confirmKYCResponse;
		}
	}

	@Override
	public ValidateKYCResponse validateKYCRequest(Holder<Header> headerParam, ValidateKYCRequest req) throws Exception {
		ValidateKYCResponse validateKYCResponse = new ValidateKYCResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);

			Integer id = baseRepository.getMembersRepository().loadKYCMemberByID(req.getId());
			Members reqMember = baseRepository.getMembersRepository().findOneMembers("id", id);
			List<Members> members = baseRepository.getMembersRepository().loadMembersByGroupID(1, 0, 10);

			if (req.isAccepted()) {
				if (baseRepository.getMembersRepository().memberKYCValidate(member.getId(), req.getId())) {
					validateKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
					String subject = "Approval KYC " + reqMember.getUsername();
					String body = "Member " + reqMember.getUsername() + " already validated by " + member.getUsername()
							+ ". Please to approve the kyc of member.";
					for (int i = 0; i < members.size(); i++) {
						baseRepository.getMessageRepository().sendMessage(member.getId(), members.get(i).getId(),
								subject, body);
					}
				} else {
					validateKYCResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				}
			} else {
				baseRepository.getMembersRepository().memberKYCRejectValidate(member.getId(), req.getId(),
						req.getDescription());
				MuleClient client;
				client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				client.dispatch("KYCNotificationRejectVM", reqMember.getEmail(), header);
				validateKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			}
			return validateKYCResponse;
		} catch (TransactionException e) {
			validateKYCResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return validateKYCResponse;
		}
	}

	@Override
	public LoadMembersResponse loadAllMembers(Holder<Header> headerParam, LoadMembersRequest req) {
		LoadMembersResponse loadMembers = new LoadMembersResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<Members> members = baseRepository.getMembersRepository().findAllMembers(req.getCurrentPage(),
					req.getPageSize());
			Integer totalRecords = baseRepository.getMembersRepository().countTotalMembers();
			loadMembers.setTotalRecords(totalRecords);
			loadMembers.setMembers(members);
			loadMembers.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMembers;
		} catch (TransactionException e) {
			loadMembers.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMembers;
		}
	}

	@Override
	public LoadKYCResponse loadKYCRequest(Holder<Header> headerParam, LoadKYCRequest req) throws Exception {
		LoadKYCResponse loadKYCResponse = new LoadKYCResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<MemberKYC> kyc = new LinkedList<MemberKYC>();

			if (req.getId() == null) {
				if (req.getStatus() == null) {
					if (req.getUsername() == null) {
						kyc = baseRepository.getMembersRepository().loadMemberKYC(req.getCurrentPage(),
								req.getPageSize());
					} else {
						Members member = memberValidation.validateMember(req.getUsername(), true);
						kyc = baseRepository.getMembersRepository().loadMemberKYCByMemberID(member.getId());
					}
				} else {
					kyc = baseRepository.getMembersRepository().loadMemberKYCByStatus(req.getCurrentPage(),
							req.getPageSize(), req.getStatus());
				}
				if (kyc.size() == 0) {
					loadKYCResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
					return loadKYCResponse;
				}

				List<Integer> fromIDs = new LinkedList<Integer>();
				for (MemberKYC m : kyc) {
					fromIDs.add(m.getFromMember().getId());
				}

				List<Integer> validatedIDs = new LinkedList<Integer>();
				for (MemberKYC m : kyc) {
					validatedIDs.add(m.getValidatedMember().getId());
				}

				List<Integer> approvedIDs = new LinkedList<Integer>();
				for (MemberKYC m : kyc) {
					approvedIDs.add(m.getApprovedMember().getId());
				}

				List<Integer> groupIDs = new LinkedList<Integer>();
				for (MemberKYC m : kyc) {
					groupIDs.add(m.getGroup().getId());
				}

				List<Members> fromMember = baseRepository.getMembersRepository().loadMembersByIds(fromIDs);
				List<Members> validatedMember = baseRepository.getMembersRepository().loadMembersByIds(validatedIDs);
				List<Members> approvedMember = baseRepository.getMembersRepository().loadMembersByIds(approvedIDs);
				List<Groups> groupDestination = baseRepository.getGroupsRepository().loadGroupByIds(groupIDs);

				Map<Integer, List<Members>> fromMemberMap = fromMember.stream()
						.collect(Collectors.groupingBy(Members::getId, Collectors.toList()));
				Map<Integer, List<Members>> validatedMemberMap = validatedMember.stream()
						.collect(Collectors.groupingBy(Members::getId, Collectors.toList()));
				Map<Integer, List<Members>> approvedMemberMap = approvedMember.stream()
						.collect(Collectors.groupingBy(Members::getId, Collectors.toList()));
				Map<Integer, List<Groups>> groupMap = groupDestination.stream()
						.collect(Collectors.groupingBy(Groups::getId, Collectors.toList()));

				for (int i = 0; i < kyc.size(); i++) {
					kyc.get(i).setFromMember(fromMemberMap.get(kyc.get(i).getFromMember().getId()).get(0));
					if (kyc.get(i).getValidatedMember().getId() != 0) {
						kyc.get(i).setValidatedMember(
								validatedMemberMap.get(kyc.get(i).getValidatedMember().getId()).get(0));
						if (kyc.get(i).getValidateDate() != null) {
							kyc.get(i).setFormattedValidateDate(Utils.formatDate(kyc.get(i).getValidateDate()));
						} else {
							kyc.get(i).setFormattedValidateDate(null);
						}
					} else {
						kyc.get(i).setValidatedMember(null);
					}

					if (kyc.get(i).getApprovedMember().getId() != 0) {
						kyc.get(i).setApprovedMember(
								approvedMemberMap.get(kyc.get(i).getApprovedMember().getId()).get(0));
						if (kyc.get(i).getApprovalDate() != null) {
							kyc.get(i).setFormattedApprovalDate(Utils.formatDate(kyc.get(i).getApprovalDate()));
						} else {
							kyc.get(i).setFormattedApprovalDate(null);
						}
					} else {
						kyc.get(i).setApprovedMember(null);
					}
					kyc.get(i).setGroup(groupMap.get(kyc.get(i).getGroup().getId()).get(0));
				}

				Integer kycCount = 0;
				if (req.getStatus() == null) {
					if (req.getUsername() == null) {
						kycCount = baseRepository.getMembersRepository().countTotalKYCMembers();
					}
				} else {
					kycCount = baseRepository.getMembersRepository().countTotalKYCMembersByStatus(req.getStatus());
				}
				loadKYCResponse.setTotalRecords(kycCount);

			} else {
				kyc = baseRepository.getMembersRepository().loadMemberKYCByID(req.getId());

				if (kyc.size() == 0) {
					loadKYCResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
					return loadKYCResponse;
				}

				Members fromMember = baseRepository.getMembersRepository().findOneMembers("id",
						kyc.get(0).getFromMember().getId());
				kyc.get(0).setFromMember(fromMember);

				if (kyc.get(0).getValidatedMember().getId() != 0) {
					Members validatedMember = baseRepository.getMembersRepository().findOneMembers("id",
							kyc.get(0).getValidatedMember().getId());
					kyc.get(0).setValidatedMember(validatedMember);
					if (kyc.get(0).getValidateDate() != null) {
						kyc.get(0).setFormattedValidateDate(Utils.formatDate(kyc.get(0).getValidateDate()));
					} else {
						kyc.get(0).setFormattedValidateDate(null);
					}
				} else {
					kyc.get(0).setValidatedMember(null);
				}

				if (kyc.get(0).getApprovedMember().getId() != 0) {
					Members approvedMember = baseRepository.getMembersRepository().findOneMembers("id",
							kyc.get(0).getApprovedMember().getId());
					kyc.get(0).setApprovedMember(approvedMember);
					if (kyc.get(0).getApprovalDate() != null) {
						kyc.get(0).setFormattedApprovalDate(Utils.formatDate(kyc.get(0).getApprovalDate()));
					} else {
						kyc.get(0).setFormattedApprovalDate(null);
					}
				} else {
					kyc.get(0).setApprovedMember(null);
				}
				Groups group = baseRepository.getGroupsRepository().loadGroupsByID(kyc.get(0).getGroup().getId());
				kyc.get(0).setGroup(group);
			}

			loadKYCResponse.setMemberKYC(kyc);
			loadKYCResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadKYCResponse;
		} catch (TransactionException e) {
			loadKYCResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadKYCResponse;
		}

	}

	@Override
	public void registerMerchants(Holder<Header> headerParam, RegisterMerchantRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Groups groups = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
			Members member = baseRepository.getMembersRepository().findOneMembers("username", req.getUsername());
			if (groups == null) {
				throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
			}

			String usernameMerchant = "1112" + Utils.GenerateRandomNumber(4);
			baseRepository.getMembersRepository().createMerchants(req, usernameMerchant, member.getId());

		} catch (DuplicateKeyException ex) {
			throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
		}
	}

	@Override
	public LoadMerchantByUsernameResponse loadMerchantsByUsername(Holder<Header> headerParam,
			LoadMerchantByUsernameRequest req) {
		LoadMerchantByUsernameResponse loadMerchant = new LoadMerchantByUsernameResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = baseRepository.getMembersRepository().findOneMembers("username", req.getUsername());
			if (members == null) {
				loadMerchant.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				return loadMerchant;
			}
			Groups group = baseRepository.getGroupsRepository().loadGroupsByID(members.getGroupID());
			Merchants merchants = baseRepository.getMembersRepository().loadMerchantsByMemberID(members.getId());

			merchants.setGroup(group);
			loadMerchant.setMerchants(merchants);
			loadMerchant.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return loadMerchant;
		} catch (TransactionException e) {
			loadMerchant.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return loadMerchant;
		}
	}

	@Override
	public void updateMerchants(Holder<Header> headerParam, UpdateMerchantRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members fromMember = memberValidation.validateMember(req.getUsername(), true);
			Merchants merchant = baseRepository.getMembersRepository().loadMerchantsByMemberID(fromMember.getId());

			req.setId(fromMember.getId());

			if (req.getGroupID() != null && fromMember.getGroupID() != req.getGroupID()) {
				Groups group = baseRepository.getGroupsRepository().loadGroupsByID(req.getGroupID());
				if (group == null) {
					throw new TransactionException(String.valueOf(Status.INVALID_GROUP));
				}
			} else {
				req.setGroupID(fromMember.getGroupID());
			}

			if (req.getName() == null) {
				req.setName(fromMember.getName());
			}

			if (req.getEmail() == null) {
				req.setEmail(fromMember.getEmail());
			}

			if (req.getMsisdn() == null) {
				req.setMsisdn(fromMember.getMsisdn());
			}

			if (req.getCategoryID() == null) {
				req.setCategoryID(merchant.getCategory().getId());
			}

			if (req.getSubCategoryID() == null) {
				req.setSubCategoryID(merchant.getSubCategory().getId());
			}

			if (req.getScaleID() == null) {
				req.setScaleID(merchant.getScale().getId());
			}

			if (req.getAverageTrxValue() == BigDecimal.ZERO) {
				req.setAverageTrxValue(merchant.getAverageTrxValue());
			}

			if (req.getTaxCardNumber() == null) {
				req.setTaxCardNumber(merchant.getTaxCardNumber());
			}

			if (req.getPermissionNumber() == null) {
				req.setPermissionNumber(merchant.getPermissionNumber());
			}

			if (req.getAddress() == null) {
				req.setAddress(merchant.getAddress());
			}

			if (req.getStorePhotoPath() == null) {
				req.setStorePhotoPath(merchant.getStorePhotoPath());
			}

			MerchantOwner owner = new MerchantOwner();
			if (req.getOwner().getId() == null) {
				owner.setId(merchant.getOwner().getId());
			}

			if (req.getOwner().getIdCardNo() == null) {
				owner.setIdCardNo(merchant.getOwner().getIdCardNo());
			}

			if (req.getOwner().getAddress() == null) {
				owner.setAddress(merchant.getOwner().getAddress());
			}

			if (req.getOwner().getDateOfBirth() == null) {
				owner.setDateOfBirth(merchant.getOwner().getDateOfBirth());
			}

			if (req.getOwner().getImagePath1() == null) {
				owner.setImagePath1(merchant.getOwner().getImagePath1());
			}

			if (req.getOwner().getImagePath2() == null) {
				owner.setImagePath2(merchant.getOwner().getImagePath2());
			}

			if (req.getOwner().getName() == null) {
				owner.setName(merchant.getOwner().getName());
			}

			if (req.getOwner().getPlaceOfBirth() == null) {
				owner.setPlaceOfBirth(merchant.getOwner().getPlaceOfBirth());
			}
			req.setOwner(owner);

			baseRepository.getMembersRepository().updateMerchants(req);
		} catch (DataIntegrityViolationException e) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}
	}

	@Override
	public LoadMerchantResponse loadMerchantRequest(Holder<Header> headerParam, LoadMerchantRequest req)
			throws Exception {
		LoadMerchantResponse lmr = new LoadMerchantResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = baseRepository.getMembersRepository().findOneMembers("username", req.getUsername());
			List<Merchants> merchant = new LinkedList<Merchants>();

			if (req.getCurrentPage() != null || req.getPageSize() != null) {
				merchant = baseRepository.getMembersRepository().loadMerchantRequestByMemberID(req.getCurrentPage(),
						req.getPageSize());

				lmr.setMerchants(merchant);
				lmr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
				lmr.setTotalRecords(baseRepository.getMembersRepository().countTotalMerchantRequest());
			} else {
				merchant = baseRepository.getMembersRepository().loadMerchantRequestByMemberID(members.getId());
				logger.info("Merchant Size: " + merchant.size() + "Merchant is Empty: " + merchant.isEmpty());
				if (merchant.isEmpty()) {
					lmr.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				}

				lmr.setMerchants(merchant);
				lmr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			}
			return lmr;
		} catch (TransactionException e) {
			lmr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lmr;
		}
	}

	@Override
	public ConfirmMerchantResponse confirmMerchantRequest(Holder<Header> headerParam, ConfirmMerchantRequest req)
			throws Exception {
		ConfirmMerchantResponse confirmMerchantResponse = new ConfirmMerchantResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			Members merchantMember = memberValidation.validateMember(req.getMerchantUsername(), true);

			if (req.isAccepted()) {
				if (baseRepository.getMembersRepository().merchantApproval(member.getId(), merchantMember.getId())) {

					PartnerTYKRegister ptr = new PartnerTYKRegister();
					String response = ptr.sendPost(merchantMember.getEmail(), tykHost, tykAuthorization,
							apiManagementID, apiManagementName);

					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
						String credential = Utils.GenerateRandomNumber(6);

						CreateCredentialRequest cReq = new CreateCredentialRequest();
						cReq.setAccessTypeID(1);
						cReq.setUsername(merchantMember.getUsername());
						cReq.setMemberID(merchantMember.getId());
						cReq.setCredential(credential);
						accessRepository.createCredential(cReq);

						CreateCredentialRequest cReqWeb = new CreateCredentialRequest();
						cReqWeb.setAccessTypeID(4);
						cReqWeb.setUsername(merchantMember.getUsername());
						cReqWeb.setMemberID(merchantMember.getId());
						cReqWeb.setCredential(credential);
						accessRepository.createCredential(cReqWeb);

						String secretKey = Utils.GenerateRandomNumber(10);
						CreateCredentialRequest cReqSecret = new CreateCredentialRequest();
						cReqSecret.setAccessTypeID(2);
						cReqSecret.setUsername(merchantMember.getUsername());
						cReqSecret.setMemberID(merchantMember.getId());
						cReqSecret.setCredential(secretKey);
						accessRepository.createCredential(cReqSecret);

						CreateCredentialRequest cReqKey = new CreateCredentialRequest();
						cReqKey.setAccessTypeID(3);
						cReqKey.setUsername(merchantMember.getUsername());
						cReqKey.setMemberID(merchantMember.getId());
						cReqKey.setCredential(jsonObject.getString("key"));
						accessRepository.createCredential(cReqKey);

						Notifications notif = new Notifications();
						notif.setModuleURL("emoney.notification.email");
						notif.setNotificationType("registerPartner");

						List<Notifications> lm = new LinkedList<Notifications>();
						lm.add(notif);

						HashMap<String, Object> notifMap = new HashMap<String, Object>();
						notifMap.put("notification", lm);
						notifMap.put("APIKey", jsonObject.getString("key"));
						notifMap.put("secretAuth", secretKey);
						notifMap.put("credential", cReq.getCredential());
						notifMap.put("email", merchantMember.getEmail());
						notifMap.put("sandbox", "https://sandbox.optima-s.co.id");
						notifMap.put("dashboard", "https://optima-s.co.id/admin/login");
						notifMap.put("username", merchantMember.getUsername());

						MuleClient client = new MuleClient(configurator.getMuleContext());
						Map<String, Object> header = new HashMap<String, Object>();
						client.dispatch("NotificationVM", notifMap, header);

						confirmMerchantResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
					} else {
						confirmMerchantResponse.setStatus(StatusBuilder.getStatus(Status.INVALID));
					}
				} else {
					confirmMerchantResponse.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
				}
			} else {
				baseRepository.getMembersRepository().merchantRejectApproval(member.getId(), merchantMember.getId(),
						req.getDescription());
				/*
				 * SEND EMAIL HERE
				 */
				Notifications notif = new Notifications();
				notif.setModuleURL("emoney.notification.email");
				notif.setNotificationType("merchantRegister");

				List<Notifications> lm = new LinkedList<Notifications>();
				lm.add(notif);

				HashMap<String, Object> notifMap = new HashMap<String, Object>();
				notifMap.put("notification", lm);
				notifMap.put("email", merchantMember.getEmail());
				notifMap.put("status", "rejected");

				MuleClient client;
				client = new MuleClient(configurator.getMuleContext());
				Map<String, Object> header = new HashMap<String, Object>();
				client.dispatch("NotificationVM", notifMap, header);

				confirmMerchantResponse.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			}
			return confirmMerchantResponse;
		} catch (TransactionException e) {
			confirmMerchantResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return confirmMerchantResponse;
		}
	}

	@Override
	public LoadMerchantCategoryResponse loadMerchantCategory(Holder<Header> headerParam,
			LoadMerchantCategoryRequest req) throws Exception {
		LoadMerchantCategoryResponse lmr = new LoadMerchantCategoryResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<MerchantCategory> category = new LinkedList<MerchantCategory>();

			category = baseRepository.getMembersRepository().loadMerchantCategory(req.getCurrentPage(),
					req.getPageSize());

			lmr.setCategory(category);
			lmr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			lmr.setTotalRecords(baseRepository.getMembersRepository().countTotalMerchantCategory());
			return lmr;
		} catch (TransactionException e) {
			lmr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lmr;
		}
	}

	@Override
	public LoadMerchantSubCategoryResponse loadMerchantSubCategory(Holder<Header> headerParam,
			LoadMerchantSubCategoryRequest req) throws Exception {
		LoadMerchantSubCategoryResponse lmr = new LoadMerchantSubCategoryResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<MerchantSubCategory> subCategory = baseRepository.getMembersRepository()
					.loadMerchantSubCategory(req.getCurrentPage(), req.getPageSize());

			lmr.setSubCategory(subCategory);
			lmr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			lmr.setTotalRecords(baseRepository.getMembersRepository().countTotalMerchantSubCategory());
			return lmr;
		} catch (TransactionException e) {
			lmr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lmr;
		}
	}

	@Override
	public LoadMerchantBusinessScaleResponse loadMerchantBusinessScale(Holder<Header> headerParam,
			LoadMerchantBusinessScaleRequest req) throws Exception {
		LoadMerchantBusinessScaleResponse lmr = new LoadMerchantBusinessScaleResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<MerchantBusinessScale> businessScale = baseRepository.getMembersRepository()
					.loadMerchantBusinessScale(req.getCurrentPage(), req.getPageSize());

			lmr.setBusinessScale(businessScale);
			lmr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			lmr.setTotalRecords(baseRepository.getMembersRepository().countTotalMerchantBusinessScale());
			return lmr;
		} catch (TransactionException e) {
			lmr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lmr;
		}
	}

}
