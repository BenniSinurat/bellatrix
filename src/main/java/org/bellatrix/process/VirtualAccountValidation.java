package org.bellatrix.process;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bellatrix.data.Members;
import org.bellatrix.data.RegisterVADoc;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.VADetails;
import org.bellatrix.data.VAEventDoc;
import org.bellatrix.data.VARecordView;
import org.bellatrix.data.VirtualAccounts;
import org.bellatrix.services.LoadVAByIDRequest;
import org.bellatrix.services.LoadVAByMemberRequest;
import org.bellatrix.services.LoadVAStatusByMemberRequest;
import org.bellatrix.services.VABankRequest;
import org.bellatrix.services.VADeleteRequest;
import org.bellatrix.services.VAPaymentRequest;
import org.bellatrix.services.VARegisterBankRequest;
import org.bellatrix.services.VARegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Component
public class VirtualAccountValidation {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private HazelcastInstance instance;
	private Logger logger = Logger.getLogger(VirtualAccountValidation.class);

	public VADetails validateVARequest(String token, VARegisterRequest req) throws TransactionException {
		//IMap<String, RegisterVADoc> mapRVAMap = instance.getMap("RegisterVAMap");
		webserviceValidation.validateWebservice(token);
		Members member = memberValidation.validateMember(req.getUsername(), false);
		Members fromMember = null;
		if (req.getFromMember() != null) {
			fromMember = memberValidation.validateMember(req.getFromMember(), true);
		}
		Integer binID = baseRepository.getVirtualAccountRepository().getBinID(member.getId(), req.getBankID());
		VirtualAccounts va = baseRepository.getVirtualAccountRepository().loadVAInfo(req.getBankID(), binID);

		if (va == null) {
			throw new TransactionException(String.valueOf(Status.BANK_NOT_FOUND));
		}

		TransferTypes trxType = transferTypeValidation.validateTransferType(va.getTransferTypeID(), req.getAmount());
		String paymentCode = null;

		if (req.isPersistent() == false) {
			if (req.getReferenceNumber().length() > va.getReferenceCodeLength().length()) {
				//String rand = Utils.getRandomNumberInRange(va.getReferenceCodeLength());
				String rand = Utils.getRandomNumberInRange("9")+req.getReferenceNumber();
				paymentCode = va.getBinNumber() + rand.substring(0,va.getReferenceCodeLength().length());
			} else {
				//paymentCode = va.getBinNumber() + StringUtils.leftPad(req.getReferenceNumber(),	String.valueOf(va.getReferenceCodeLength()).length(), '0');
				String rand = Utils.getRandomNumberInRange("9")+req.getReferenceNumber();
				if(rand.length() > va.getReferenceCodeLength().length()) {
					paymentCode = va.getBinNumber() + rand.substring(0,va.getReferenceCodeLength().length());
				}else {
					paymentCode = va.getBinNumber() + StringUtils.leftPad(rand,	String.valueOf(va.getReferenceCodeLength()).length(), '0');
				}
			}
		} else {
			String rand = Utils.getRandomNumberInRange(va.getReferenceCodeLength());
			paymentCode = va.getBinNumber() + rand;
		}
		
		RegisterVADoc rv = baseRepository.getPersistenceRepository()
				.retrieve(new Query(Criteria.where("_id").is(paymentCode)), RegisterVADoc.class);
		//RegisterVADoc rv = mapRVAMap.get(paymentCode);
		if (rv != null) {
			throw new TransactionException(String.valueOf(Status.DUPLICATE_TRANSACTION));
		}

		VADetails vad = new VADetails();
		vad.setToMember(member);
		vad.setFromMember(fromMember);
		vad.setTrxType(trxType);
		vad.setVirtualAccount(va);
		vad.setPaymentCode(paymentCode);
		return vad;
	}

	public RegisterVADoc validateVAPaymentCode(String token, String paymentCode, String username)
			throws TransactionException {
		//IMap<String, RegisterVADoc> mapRVAMap = instance.getMap("RegisterVAMap");
		webserviceValidation.validateWebservice(token);
		RegisterVADoc rva = baseRepository.getPersistenceRepository()
				.retrieve(new Query(Criteria.where("_id").is(paymentCode)), RegisterVADoc.class);
		//RegisterVADoc rva = mapRVAMap.get(paymentCode);
		logger.info("PAYMENT CODE: "+rva);
		if (rva == null) {
			throw new TransactionException(String.valueOf(Status.PAYMENT_CODE_NOT_FOUND));
		}

		if (username != null) {
			if (!rva.getMember().getUsername().equalsIgnoreCase(username)) {
				throw new TransactionException(String.valueOf(Status.SERVICE_NOT_ALLOWED));
			}
		}
		return rva;
	}

	public VADetails validateVAPayment(String token, VAPaymentRequest req) throws TransactionException {
		//IMap<String, RegisterVADoc> mapRVAMap = instance.getMap("RegisterVAMap");
		webserviceValidation.validateWebservice(token);
		Members fromMember = memberValidation.validateMember(req.getFromMember(), true);
		RegisterVADoc rva = baseRepository.getPersistenceRepository()
				.retrieve(new Query(Criteria.where("_id").is(req.getPaymentCode())), RegisterVADoc.class);
		//RegisterVADoc rva = mapRVAMap.get(req.getPaymentCode());
		if (rva == null) {
			throw new TransactionException(String.valueOf(Status.PAYMENT_CODE_NOT_FOUND));
		} else {
			logger.info("PAYMENT CODE: " + rva.getPaymentCode() + "/EXPIRED DATE: " +rva.getExpiredAt());
			String id = baseRepository.getVirtualAccountRepository().getVAID(rva);
			logger.info("ID " + id);
			if (id == null) {
				throw new TransactionException(String.valueOf(Status.PAYMENT_CODE_NOT_FOUND));
			} else {
				rva.setId(id);
				rva.setPaymentCode(req.getPaymentCode());
			}
		}

		if (rva.isFullPayment()) {
			if (rva.getAmount().compareTo(req.getAmount()) != 0) {
				throw new TransactionException(String.valueOf(Status.INVALID_AMOUNT));
			}
		} else {
			if (rva.getMinimumPayment() != null && rva.getMinimumPayment().compareTo(BigDecimal.ZERO) != 0
					&& rva.getMinimumPayment().compareTo(req.getAmount()) == 0) {
				throw new TransactionException(String.valueOf(Status.INVALID_AMOUNT));
			} /**
				 * else { BigDecimal newAmount = rva.getAmount().subtract(req.getAmount()); if
				 * (newAmount.compareTo(BigDecimal.ZERO) > 0) {
				 * rva.setAmount(rva.getAmount().subtract(req.getAmount()));
				 * baseRepository.getPersistenceRepository().update(rva);; } else { throw new
				 * TransactionException(String.valueOf(Status.INVALID_AMOUNT)); } }
				 */
		}

		VADetails vad = new VADetails();
		vad.setFromMember(fromMember);
		vad.setRegVA(rva);
		vad.setPaymentCode(req.getPaymentCode());
		return vad;
	}

	public void validateVADeletion(String token, VADeleteRequest req) throws TransactionException {
		//IMap<String, RegisterVADoc> mapRVAMap = instance.getMap("RegisterVAMap");
		webserviceValidation.validateWebservice(token);
		memberValidation.validateMember(req.getUsername(), true);
		//RegisterVADoc rva = mapRVAMap.get(req.getPaymentCode());
		RegisterVADoc rva = baseRepository.getPersistenceRepository()
				.retrieve(new Query(Criteria.where("_id").is(req.getPaymentCode())), RegisterVADoc.class);
		if (rva == null) {
			throw new TransactionException(String.valueOf(Status.PAYMENT_CODE_NOT_FOUND));
		}

		if (!rva.getMember().getUsername().equalsIgnoreCase(req.getUsername())) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}
		//mapRVAMap.delete(req.getPaymentCode());
		baseRepository.getPersistenceRepository().delete(new Query(Criteria.where("_id").is(req.getPaymentCode())), RegisterVADoc.class);
		baseRepository.getVirtualAccountRepository().deleteVA(req.getPaymentCode());
	}

	public List<BankVA> validateBankVA(String token, VABankRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(token);
		Members member = memberValidation.validateMember(req.getUsername(), true);
		List<BankVA> bankVA = baseRepository.getVirtualAccountRepository().loadBankVA(member.getId());
		return bankVA;
	}
	
	public List<BankVA> validateListBankVA(String token, VABankRequest req) throws TransactionException {
		webserviceValidation.validateWebservice(token);
		List<BankVA> bankVA = baseRepository.getVirtualAccountRepository().listBankVA();
		return bankVA;
	}

	public BankVA validateBankVA(String token, VARegisterBankRequest req) throws Exception {
		BankVA bankVA = new BankVA();
		try {
			webserviceValidation.validateWebservice(token);
			Members member = memberValidation.validateMember(req.getUsername(), true);
			bankVA = baseRepository.getVirtualAccountRepository().loadBankVA(member.getId(), req.getVaBankID(),
					req.getVaBinID());
			if (bankVA == null) {
				baseRepository.getVirtualAccountRepository().registerMemberVA(member.getId(), req.getVaBankID(),
						req.getVaBinID());
			}
		} catch (Exception e) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}

		return bankVA;
	}

	public List<RegisterVADoc> validateLoadVA(String token, LoadVAByMemberRequest request) throws TransactionException {
		//IMap<String, RegisterVADoc> mapRVAMap = instance.getMap("RegisterVAMap");
		webserviceValidation.validateWebservice(token);
		List<RegisterVADoc> listVA = new LinkedList<RegisterVADoc>();
		Pageable paging = new PageRequest(request.getCurrentPage(), request.getPageSize());
		Query query = new Query();
		query.addCriteria(Criteria.where("member.username").is(request.getUsername()));
		query.with(paging);
		query.with(new Sort(Sort.Direction.DESC, "createdDate"));
		
		//listVA = mapRVAMap.getAll();
		listVA = baseRepository.getPersistenceRepository().loadAll(query, RegisterVADoc.class);
		return listVA;
	}

	public List<RegisterVADoc> validateLoadVA(String token, LoadVAByIDRequest request) throws TransactionException {
		webserviceValidation.validateWebservice(token);
		List<RegisterVADoc> listVA = new LinkedList<RegisterVADoc>();
		Query query = new Query();
		query.addCriteria(Criteria.where("ticketID").is(request.getTicketID()));
		query.with(new Sort(Sort.Direction.DESC, "createdDate"));

		listVA = baseRepository.getPersistenceRepository().loadAll(query, RegisterVADoc.class);
		return listVA;
	}

	public List<VAEventDoc> validateLoadVAEvent(String token, String username, Integer start, Integer size)
			throws TransactionException {
		webserviceValidation.validateWebservice(token);
		memberValidation.validateMember(username, true);
		List<VAEventDoc> listVA = new LinkedList<VAEventDoc>();
		Pageable paging = new PageRequest(start, size);
		Query query = new Query();
		query.addCriteria(Criteria.where("member.username").is(username));
		query.with(paging);
		query.with(new Sort(Sort.Direction.DESC, "createdDate"));

		listVA = baseRepository.getPersistenceRepository().loadAll(query, VAEventDoc.class);
		return listVA;
	}

	public VAEventDoc validateLoadVAEventByID(String token, String username, String ticketID)
			throws TransactionException {
		webserviceValidation.validateWebservice(token);
		VAEventDoc vadoc = new VAEventDoc();
		if (username == null) {
			vadoc = baseRepository.getPersistenceRepository().retrieve(new Query(Criteria.where("_id").is(ticketID)),
					VAEventDoc.class);
		} else {
			memberValidation.validateMember(username, true);
			vadoc = baseRepository.getPersistenceRepository().retrieve(
					new Query(Criteria.where("member.username").is(username).and("_id").is(ticketID)),
					VAEventDoc.class);
		}
		if (vadoc == null) {
			throw new TransactionException(String.valueOf(Status.PAYMENT_CODE_NOT_FOUND));
		}
		return vadoc;
	}

	public Members validateVAEvent(String token, String username) throws TransactionException {
		webserviceValidation.validateWebservice(token);
		memberValidation.validateMember(username, true);
		Members member = memberValidation.validateMember(username, true);
		return member;
	}

	public void validateVAEventDelete(String token, String username, String ticketID) throws TransactionException {
		webserviceValidation.validateWebservice(token);
		memberValidation.validateMember(username, true);
		baseRepository.getPersistenceRepository().delete(new Query(Criteria.where("_id").is(ticketID)),
				VAEventDoc.class);
	}

	public List<VARecordView> validateLoadVAStatus(String token, LoadVAStatusByMemberRequest req, Integer memberID)
			throws TransactionException {
		webserviceValidation.validateWebservice(token);

		String fromDate = req.getFromDate() != null ? req.getFromDate() : Utils.GetDate("yyyy-MM-dd");
		String toDate = req.getFromDate() != null ? req.getToDate() : Utils.GetDate("yyyy-MM-dd");
		logger.info("From Date: " + fromDate + " /To Date: " + toDate);
		List<VARecordView> listVA = new LinkedList<VARecordView>();
		listVA = baseRepository.getVirtualAccountRepository().loadVAByStatus(req.getUsername(), memberID, fromDate,
				toDate, req.getCurrentPage(), req.getPageSize());
		return listVA;
	}
	
	public void validateVABillingStatus(String token, String username, String traceNumber, String transactionNumber) throws TransactionException {
		webserviceValidation.validateWebservice(token);
		memberValidation.validateMember(username, true);
		baseRepository.getVirtualAccountRepository().updateStatusBillingVA(traceNumber, transactionNumber);
	}

}
