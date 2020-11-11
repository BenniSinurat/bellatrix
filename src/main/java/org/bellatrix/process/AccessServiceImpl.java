package org.bellatrix.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.bellatrix.data.AccessStatus;
import org.bellatrix.data.AccessType;
import org.bellatrix.data.Accesses;
import org.bellatrix.data.Groups;
import org.bellatrix.data.Header;
import org.bellatrix.data.Members;
import org.bellatrix.data.Notifications;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.Access;
import org.bellatrix.services.AccessTypeRequest;
import org.bellatrix.services.ChangeCredentialRequest;
import org.bellatrix.services.CreateCredentialRequest;
import org.bellatrix.services.CredentialStatusRequest;
import org.bellatrix.services.CredentialStatusResponse;
import org.bellatrix.services.LoadAccessTypeRequest;
import org.bellatrix.services.LoadAccessTypeResponse;
import org.bellatrix.services.ResetCredentialRequest;
import org.bellatrix.services.ResetCredentialResponse;
import org.bellatrix.services.CredentialRequest;
import org.bellatrix.services.CredentialResponse;
import org.bellatrix.services.UnblockCredentialRequest;
import org.bellatrix.services.ValidateCredentialRequest;
import org.bellatrix.services.ValidateCredentialResponse;
import org.mule.api.MuleException;
import org.mule.module.client.MuleClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccessServiceImpl implements Access {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private AccessCredentialValidation accessValidation;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private Configurator configurator;
	private Logger logger = Logger.getLogger(AccessServiceImpl.class);

	@Override
	@Transactional
	public void createCredential(Holder<Header> headerParam, CreateCredentialRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = memberValidation.validateMember(req.getUsername(), true);
			req.setMemberID(members.getId());
			baseRepository.getAccessRepository().createCredential(req);
		} catch (Exception e) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}
	}

	@Override
	public ValidateCredentialResponse validateCredential(Holder<Header> headerParam, ValidateCredentialRequest req) {
		ValidateCredentialResponse validateResponse = new ValidateCredentialResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = memberValidation.validateMember(req.getUsername(), true);

			Accesses access = baseRepository.getAccessRepository().loadCredentialByUsername(req.getUsername(),
					req.getAccessTypeID());
			if (access == null) {
				validateResponse.setStatus(StatusBuilder.getStatus(Status.INVALID_PARAMETER));
				return validateResponse;
			}

			if (baseRepository.getGroupsRepository().loadGroupsByID(members.getGroupID()).getName()
					.equalsIgnoreCase("CLOSED")) {
				validateResponse.setStatus(StatusBuilder.getStatus(Status.BLOCKED));
				return validateResponse;
			}

			if (access.isBlocked()) {
				validateResponse.setStatus(StatusBuilder.getStatus(Status.BLOCKED));
				return validateResponse;
			}

			if (!access.getPin().equals(Utils.getMD5Hash(req.getCredential()))) {

				if (accessValidation.validateBruteForce(members.getId(), req.getAccessTypeID()) == false) {
					validateResponse.setStatus(StatusBuilder.getStatus(Status.ACCESS_DENIED));
					return validateResponse;
				}

				accessValidation.blockAttemptValidation(members.getId(), req.getAccessTypeID());
				validateResponse.setStatus(StatusBuilder.getStatus(Status.INVALID));
				return validateResponse;
			}

			validateResponse.setStatus(StatusBuilder.getStatus(Status.VALID));
			return validateResponse;
		} catch (TransactionException e) {
			validateResponse.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return validateResponse;
		}

	}

	@Override
	public CredentialStatusResponse credentialStatus(Holder<Header> headerParam, CredentialStatusRequest req) {
		CredentialStatusResponse crStatus = new CredentialStatusResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());

			AccessStatus status = baseRepository.getAccessRepository().accessStatus(req.getUsername(),
					req.getAccessTypeID());
			if (status == null) {
				crStatus.setStatus(StatusBuilder.getStatus(Status.CREDENTIAL_INVALID));
				return crStatus;
			}
			crStatus.setAccessStatus(status);
			crStatus.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return crStatus;
		} catch (TransactionException e) {
			crStatus.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return crStatus;
		}
	}

	@Override
	@Transactional
	public ResetCredentialResponse resetCredential(Holder<Header> headerParam, ResetCredentialRequest req) {
		ResetCredentialResponse reset = new ResetCredentialResponse();
		try {
			String newPin = "";
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			
			Groups group = baseRepository.getGroupsRepository().loadGroupsByID(member.getGroupID());
			if(req.getNewCredential() == null || req.getNewCredential().equalsIgnoreCase("")) {
				newPin = Utils.GenerateRandomNumber(group.getPinLength());
			} else {
				newPin = req.getNewCredential();
			}

			HashMap<String, Object> notifMap = new HashMap<String, Object>();

			if (req.getUsernameMember() != null) {
				Members user = memberValidation.validateMember(req.getUsernameMember(), true);
				if (user.getGroupID() != 1) {
					logger.info("[RESET BY: " + user.getName() + " /GROUP ID: " + user.getGroupID() + "]");
					if (req.getEmail().length() > 4) {
						if (req.getEmail().equalsIgnoreCase(member.getEmail())) {
							if (member.getEmailVerify().equals(false)) {
								/*
								 * SEND SMS HERE
								 */
								Notifications notif = new Notifications();
								notif.setModuleURL("emoney.notification.sms");
								notif.setNotificationType("resetCredentialOTP");

								List<Notifications> lm = new LinkedList<Notifications>();
								lm.add(notif);

								notifMap.put("notification", lm);
								notifMap.put("msisdn", member.getMsisdn());
								notifMap.put("pin", newPin);
								
								MuleClient client;
								client = new MuleClient(configurator.getMuleContext());
								Map<String, Object> header = new HashMap<String, Object>();
								client.dispatch("NotificationVM", notifMap, header);

								baseRepository.getAccessRepository().changeCredential(member.getId(), req.getAccessTypeID(), newPin);
								baseRepository.getAccessRepository().unblockCredential(member.getId(), req.getAccessTypeID());
								baseRepository.getAccessRepository().clearAccessAttemptsRecord(member.getId(), req.getAccessTypeID());

								reset.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
								return reset;
							}
						} else {
							reset.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
							return reset;
						}
					}
				} else {
					logger.info("[RESET BY: " + user.getName() + " /GROUP ID: " + user.getGroupID() + "]");
					if (!req.getEmail().equalsIgnoreCase(member.getEmail())) {
						reset.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
						return reset;
					}
				}
			} else {
				logger.info("[RESET BY: " + req.getUsername() + "]");
				if (req.getEmail().length() > 4) {
					if (req.getEmail().equalsIgnoreCase(member.getEmail())) {
						if (member.getEmailVerify().equals(false)) {
							/*
							 * SEND SMS HERE
							 */
							Notifications notif = new Notifications();
							notif.setModuleURL("emoney.notification.sms");
							notif.setNotificationType("resetCredentialOTP");

							List<Notifications> lm = new LinkedList<Notifications>();
							lm.add(notif);

							notifMap.put("notification", lm);
							notifMap.put("msisdn", member.getMsisdn());
							notifMap.put("pin", newPin);
							
							MuleClient client;
							client = new MuleClient(configurator.getMuleContext());
							Map<String, Object> header = new HashMap<String, Object>();
							client.dispatch("NotificationVM", notifMap, header);

							baseRepository.getAccessRepository().changeCredential(member.getId(), req.getAccessTypeID(), newPin);
							baseRepository.getAccessRepository().unblockCredential(member.getId(), req.getAccessTypeID());
							baseRepository.getAccessRepository().clearAccessAttemptsRecord(member.getId(), req.getAccessTypeID());

							reset.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
							return reset;
						}
					} else {
						reset.setStatus(StatusBuilder.getStatus(Status.MEMBER_NOT_FOUND));
						return reset;
					}
				}
			}

			if (req.getEmail().length() > 4) {
				/*
				 * SEND EMAIL HERE
				 */
				Notifications notif = new Notifications();
				notif.setModuleURL("emoney.notification.email");
				notif.setNotificationType("resetCredential");

				List<Notifications> lm = new LinkedList<Notifications>();
				lm.add(notif);

				notifMap.put("notification", lm);
				notifMap.put("email", member.getEmail());
				notifMap.put("pin", newPin);
			} else {
				/*
				 * SEND SMS HERE
				 */
				Notifications notif = new Notifications();
				notif.setModuleURL("emoney.notification.sms");
				notif.setNotificationType("resetCredentialOTP");

				List<Notifications> lm = new LinkedList<Notifications>();
				lm.add(notif);

				notifMap.put("notification", lm);
				notifMap.put("msisdn", member.getMsisdn());
				notifMap.put("pin", newPin);
			}

			MuleClient client;
			client = new MuleClient(configurator.getMuleContext());
			Map<String, Object> header = new HashMap<String, Object>();
			client.dispatch("NotificationVM", notifMap, header);

			baseRepository.getAccessRepository().changeCredential(member.getId(), req.getAccessTypeID(), newPin);
			baseRepository.getAccessRepository().unblockCredential(member.getId(), req.getAccessTypeID());
			baseRepository.getAccessRepository().clearAccessAttemptsRecord(member.getId(), req.getAccessTypeID());

			reset.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return reset;
		} catch (TransactionException e) {
			reset.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return reset;
		} catch (MuleException e) {
			reset.setStatus(StatusBuilder.getStatus(Status.UNKNOWN_ERROR));
			return reset;
		}

	}

	@Override
	@Transactional
	public void unblockCredential(Holder<Header> headerParam, UnblockCredentialRequest req) throws Exception {
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		Members member = memberValidation.validateMember(req.getUsername(), true);
		baseRepository.getAccessRepository().unblockCredential(member.getId(), req.getAccessTypeID());
		baseRepository.getAccessRepository().clearAccessAttemptsRecord(member.getId(), req.getAccessTypeID());
	}

	@Override
	public void changeCredential(Holder<Header> headerParam, ChangeCredentialRequest req) throws Exception {
		ValidateCredentialRequest validate = new ValidateCredentialRequest();
		webserviceValidation.validateWebservice(headerParam.value.getToken());
		validate.setAccessTypeID(req.getAccessTypeID());
		validate.setCredential(req.getOldCredential());
		validate.setUsername(req.getUsername());
		Accesses access = baseRepository.getAccessRepository().validateCredential(validate);
		if (access == null) {
			throw new TransactionException(String.valueOf(Status.INVALID));
		}
		baseRepository.getAccessRepository().changeCredential(access.getMemberID(), req.getAccessTypeID(),
				req.getNewCredential());
	}

	@Override
	public CredentialResponse getCredential(Holder<Header> headerParam, CredentialRequest req) throws Exception {
		CredentialResponse cr = new CredentialResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members members = memberValidation.validateMember(req.getUsername(), true);
			cr = baseRepository.getAccessRepository().getSecretFromMemberID(members.getId(), req.getAccessTypeID());
			cr.setCredential(cr.getCredential());
			cr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return cr;
		} catch (TransactionException ex) {
			cr.setStatus(StatusBuilder.getStatus(ex.getMessage()));
			return cr;
		}
	}

	@Override
	public LoadAccessTypeResponse loadAccessType(Holder<Header> headerParam, LoadAccessTypeRequest req)
			throws Exception {
		LoadAccessTypeResponse latr = new LoadAccessTypeResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				req.setCurrentPage(0);
				req.setPageSize(0);
			}
			List<AccessType> accessType = baseRepository.getAccessRepository()
					.getAllCredentialType(req.getCurrentPage(), req.getPageSize());
			Integer totalRecords = baseRepository.getAccessRepository().countTotalAccount();
			latr.setAccessType(accessType);
			latr.setTotalRecords(totalRecords);
			latr.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return latr;
		} catch (TransactionException e) {
			latr.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return latr;
		}

	}

	@Override
	public void updateAccessType(Holder<Header> headerParam, AccessTypeRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getAccessRepository().updateCredentialType(req.getId(), req.getName(), req.getInternalName(),
					req.getDescription());
		} catch (TransactionException ex) {
			throw new TransactionException(ex.getMessage());
		}
	}

	@Override
	public void createAccessType(Holder<Header> headerParam, AccessTypeRequest req) throws Exception {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getAccessRepository().createCredentialType(req.getName(), req.getInternalName(),
					req.getDescription());
		} catch (TransactionException ex) {
			throw new TransactionException(ex.getMessage());
		}
	}

}
