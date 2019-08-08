package org.bellatrix.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseRepository {

	@Autowired
	private WebserviceRepository webServicesRepository;

	@Autowired
	private GroupRepository groupsRepository;

	@Autowired
	private MemberRepository membersRepository;

	@Autowired
	private AccessRepository accessRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomFieldRepository customFieldRepository;

	@Autowired
	private TransferTypeRepository transferTypeRepository;

	@Autowired
	private TransferRepository transferRepository;

	@Autowired
	private InterBankRepository interBankRepository;

	@Autowired
	private VirtualAccountRepository virtualAccountRepository;

	@Autowired
	private LogRepository logRepository;

	@Autowired
	private GlobalConfigRepository globalConfigRepository;

	@Autowired
	private BillPaymentRepository billPaymentRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private PosRepository posRepository;
	
	@Autowired
	private PersistenceRepository persistenceRepository;

	public WebserviceRepository getWebServicesRepository() {
		return webServicesRepository;
	}

	public void setWebServicesRepository(WebserviceRepository webServicesRepository) {
		this.webServicesRepository = webServicesRepository;
	}

	public GroupRepository getGroupsRepository() {
		return groupsRepository;
	}

	public void setGroupsRepository(GroupRepository groupsRepository) {
		this.groupsRepository = groupsRepository;
	}

	public MemberRepository getMembersRepository() {
		return membersRepository;
	}

	public void setMembersRepository(MemberRepository membersRepository) {
		this.membersRepository = membersRepository;
	}

	public AccessRepository getAccessRepository() {
		return accessRepository;
	}

	public void setAccessRepository(AccessRepository accessRepository) {
		this.accessRepository = accessRepository;
	}

	public CustomFieldRepository getCustomFieldRepository() {
		return customFieldRepository;
	}

	public void setCustomFieldRepository(CustomFieldRepository customFieldRepository) {
		this.customFieldRepository = customFieldRepository;
	}

	public AccountRepository getAccountRepository() {
		return accountRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public TransferTypeRepository getTransferTypeRepository() {
		return transferTypeRepository;
	}

	public void setTransferTypeRepository(TransferTypeRepository transferTypeRepository) {
		this.transferTypeRepository = transferTypeRepository;
	}

	public TransferRepository getTransferRepository() {
		return transferRepository;
	}

	public void setTransferRepository(TransferRepository transferRepository) {
		this.transferRepository = transferRepository;
	}

	public PersistenceRepository getPersistenceRepository() {
		return persistenceRepository;
	}

	public void setPersistenceRepository(PersistenceRepository persistenceRepository) {
		this.persistenceRepository = persistenceRepository;
	}

	public InterBankRepository getInterBankRepository() {
		return interBankRepository;
	}

	public void setInterBankRepository(InterBankRepository interBankRepository) {
		this.interBankRepository = interBankRepository;
	}

	public VirtualAccountRepository getVirtualAccountRepository() {
		return virtualAccountRepository;
	}

	public void setVirtualAccountRepository(VirtualAccountRepository virtualAccountRepository) {
		this.virtualAccountRepository = virtualAccountRepository;
	}

	public LogRepository getLogRepository() {
		return logRepository;
	}

	public void setLogRepository(LogRepository logRepository) {
		this.logRepository = logRepository;
	}

	public GlobalConfigRepository getGlobalConfigRepository() {
		return globalConfigRepository;
	}

	public void setGlobalConfigRepository(GlobalConfigRepository globalConfigRepository) {
		this.globalConfigRepository = globalConfigRepository;
	}

	public BillPaymentRepository getBillPaymentRepository() {
		return billPaymentRepository;
	}

	public void setBillPaymentRepository(BillPaymentRepository billPaymentRepository) {
		this.billPaymentRepository = billPaymentRepository;
	}

	public MenuRepository getMenuRepository() {
		return menuRepository;
	}

	public void setMenuRepository(MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	public MessageRepository getMessageRepository() {
		return messageRepository;
	}

	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public PosRepository getPosRepository() {
		return posRepository;
	}

	public void setPosRepository(PosRepository posRepository) {
		this.posRepository = posRepository;
	}

}
