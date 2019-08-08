package org.bellatrix.process;

import java.util.List;

import org.apache.log4j.Logger;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.Terminal;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.services.LoadTerminalByUsernameRequest;
import org.bellatrix.services.PosCreateInvoiceRequest;
import org.bellatrix.services.PosInquiryRequest;
import org.bellatrix.services.PosPaymentRequest;
import org.bellatrix.services.UpdatePOSRequest;
import org.bellatrix.services.DeletePOSRequest;
import org.bellatrix.services.LoadTerminalByIDRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PosPaymentValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private AccountValidation accountValidation;
	private Logger logger = Logger.getLogger(PosPaymentValidation.class);

	public Terminal validatePosTerminal(PosCreateInvoiceRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(), toMember);

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public Terminal validatePosTerminal(UpdatePOSRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getUsername(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(), toMember);

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public Terminal validatePosTerminal(DeletePOSRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getUsername(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(), toMember);

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public List<Terminal> validatePosTerminal(LoadTerminalByUsernameRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getUsername(), true);

		/*
		 * Validate Terminal ID
		 */
		List<Terminal> terminal = baseRepository.getPosRepository().getPosTerminalDetail(toMember, req.getCurrentPage(),
				req.getPageSize());

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public Terminal validatePosTerminal(LoadTerminalByIDRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(), toMember);

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

	public Terminal validatePosInquiry(PosInquiryRequest req, String token) throws Exception {
		/*
		 * Validate Webservice Access
		 */
		webserviceValidation.validateWebservice(token);

		/*
		 * Validate FromMember
		 */
		Members fromMember = memberValidation.validateMember(req.getFromMember(), true);

		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), false);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(), toMember);

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		/*
		 * Validate TransferType
		 */
		TransferTypes transferType = transferTypeValidation.validateTransferType(terminal.getTransferTypeID(),
				fromMember.getGroupID(), fromMember.getId());

		/*
		 * Validate FromAccount
		 */
		accountValidation.validateAccount(transferType, fromMember, true);
		if (transferType.getFromAccounts() != transferType.getToAccounts()) {

			/*
			 * Validate ToAccount
			 */
			accountValidation.validateAccount(transferType, toMember, false);
		}

		return terminal;
	}

	public Terminal validatePosPayment(PosPaymentRequest req) throws Exception {
		/*
		 * Validate ToMember
		 */
		Members toMember = memberValidation.validateMember(req.getToMember(), true);

		/*
		 * Validate Terminal ID
		 */
		Terminal terminal = baseRepository.getPosRepository().getPosTerminalDetail(req.getTerminalID(), toMember);

		if (terminal == null) {
			throw new TransactionException(String.valueOf(Status.TERMINAL_NOT_FOUND));
		}

		return terminal;
	}

}
