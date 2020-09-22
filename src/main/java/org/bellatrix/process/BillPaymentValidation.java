package org.bellatrix.process;

import java.util.List;

import org.apache.log4j.Logger;
import org.bellatrix.data.Billers;
import org.bellatrix.data.Members;
import org.bellatrix.data.PaymentChannel;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.bellatrix.services.BillerRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BillPaymentValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	private Logger logger = Logger.getLogger(BillPaymentValidation.class);

	public List<Billers> validateMemberBillers() throws TransactionException {
		List<Billers> billers = baseRepository.getBillPaymentRepository().getBillerList();
		if (billers == null) {
			throw new TransactionException(String.valueOf(Status.BILLER_UNAVAILABLE));
		}
		return billers;
	}

	public Billers validateMemberBillers(String username, Integer billerID) throws TransactionException {
		Members member = memberValidation.validateMember(username, true);
		Billers billers = baseRepository.getBillPaymentRepository().loadBillerFromID(member.getId(), billerID);
		if (billers == null) {
			throw new TransactionException(String.valueOf(Status.BILLER_UNAVAILABLE));
		}

		return billers;
	}

	public List<Billers> validateMemberBillers(String username) throws TransactionException {
		Members member = memberValidation.validateMember(username, true);
		List<Billers> billers = baseRepository.getBillPaymentRepository().loadBillerFromMember(member.getId());
		if (billers == null) {
			throw new TransactionException(String.valueOf(Status.BILLER_UNAVAILABLE));
		}

		return billers;
	}

	public void validateMemberBillers(BillerRegisterRequest req) throws TransactionException {
		Members member = memberValidation.validateMember(req.getUsername(), true);
		Integer billerID = baseRepository.getBillPaymentRepository().getBillerIDFromMember(member.getId());
		if (billerID != null) {
			throw new TransactionException(String.valueOf(Status.MEMBER_ALREADY_REGISTERED));
		} else {
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getBillPaymentRepository().registerBiller(req, member.getId());
		}
	}

	public List<PaymentChannel> validatePaymentChannel() throws TransactionException {
		List<PaymentChannel> channel = baseRepository.getBillPaymentRepository().loadChannels();
		return channel;
	}

	public PaymentChannel validatePaymentChannel(Integer channelID) throws TransactionException {
		PaymentChannel channel = baseRepository.getBillPaymentRepository().findPaymentChannelByID(channelID);
		if (channel == null) {
			logger.info("[Invalid Transfer Type ID [" + channelID + "]]");
			throw new TransactionException(String.valueOf(Status.INVALID_PAYMENT_CHANNEL));
		}
		return channel;
	}

}
