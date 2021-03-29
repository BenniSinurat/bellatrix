package org.bellatrix.process;

import java.util.LinkedList;
import java.util.List;
import javax.xml.ws.Holder;

import org.bellatrix.data.Accounts;
import org.bellatrix.data.Fees;
import org.bellatrix.data.Header;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.TransferTypeView;
import org.bellatrix.data.TransferTypes;
import org.bellatrix.data.TransferTypesPermission;
import org.bellatrix.services.FeeRequest;
import org.bellatrix.services.LoadFeesByTransferTypeRequest;
import org.bellatrix.services.LoadFeesByTransferTypeResponse;
import org.bellatrix.services.LoadPermissionByTransferTypesRequest;
import org.bellatrix.services.LoadPermissionByTransferTypesResponse;
import org.bellatrix.services.LoadTransferTypesByAccountIDRequest;
import org.bellatrix.services.LoadTransferTypesByAccountIDResponse;
import org.bellatrix.services.LoadTransferTypesByIDRequest;
import org.bellatrix.services.LoadTransferTypesByIDResponse;
import org.bellatrix.services.LoadTransferTypesByUsernameRequest;
import org.bellatrix.services.LoadTransferTypesByUsernameResponse;
import org.bellatrix.services.LoadTransferTypesRequest;
import org.bellatrix.services.LoadTransferTypesResponse;
import org.bellatrix.services.TransferType;
import org.bellatrix.services.TransferTypePermissionRequest;
import org.bellatrix.services.TransferTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferTypeServiceImpl implements TransferType {

	@Autowired
	private WebserviceValidation webserviceValidation;
	@Autowired
	private MemberValidation memberValidation;
	@Autowired
	private BaseRepository baseRepository;
	@Autowired
	private TransferTypeValidation transferTypeValidation;
	@Autowired
	private AccountValidation accountValidation;

	@Override
	public LoadTransferTypesResponse loadAllTransferTypes(Holder<Header> headerParam, LoadTransferTypesRequest req) {
		LoadTransferTypesResponse tt = new LoadTransferTypesResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<TransferTypes> transferTypes = new LinkedList<TransferTypes>();
			Integer totalRecords = 0;
			if (req.getCurrentPage() == null || req.getPageSize() == null) {
				transferTypes = baseRepository.getTransferTypeRepository().findTransferTypesByGroupID(req.getGroupID());
				totalRecords = baseRepository.getTransferTypeRepository().countTotalTransferTypesByGroupID(req.getGroupID());
			} else {
				transferTypes = baseRepository.getTransferTypeRepository()
						.loadAllTransferTypes(req.getCurrentPage(), req.getPageSize());
				totalRecords = baseRepository.getTransferTypeRepository().countTotalTransferTypes();
			}
			tt.setTransferTypesList(transferTypes);
			tt.setTotalRecords(totalRecords);
			tt.setStatus(StatusBuilder.getStatus(Status.PROCESSED));

			return tt;
		} catch (Exception e) {
			tt.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tt;
		}
	}

	@Override
	public LoadTransferTypesByIDResponse loadTransferTypesByID(Holder<Header> headerParam,
			LoadTransferTypesByIDRequest req) {
		LoadTransferTypesByIDResponse tid = new LoadTransferTypesByIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			TransferTypes t = baseRepository.getTransferTypeRepository().findTransferTypeByID(req.getId());
			tid.setTransferTypes(t);
			tid.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return tid;
		} catch (Exception e) {
			tid.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tid;
		}
	}

	@Override
	public LoadTransferTypesByAccountIDResponse loadTransferTypesByAccountID(Holder<Header> headerParam,
			LoadTransferTypesByAccountIDRequest req) {
		LoadTransferTypesByAccountIDResponse taid = new LoadTransferTypesByAccountIDResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			List<TransferTypes> lt = baseRepository.getTransferTypeRepository()
					.listTransferTypeByAccountID(req.getAccountID());
			taid.setTransferTypes(lt);
			taid.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
			return taid;
		} catch (Exception e) {
			taid.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return taid;
		}
	}

	@Override
	public LoadTransferTypesByUsernameResponse loadTransferTypesByUsername(Holder<Header> headerParam,
			LoadTransferTypesByUsernameRequest req) {
		LoadTransferTypesByUsernameResponse tur = new LoadTransferTypesByUsernameResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Members member = memberValidation.validateMember(req.getUsername(), true);
			List<TransferTypeView> ltv = baseRepository.getTransferTypeRepository()
					.listTransferTypeByGroupID(member.getGroupID());
			tur.setTransferTypes(ltv);
			tur.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			tur.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tur;
		}
		return tur;
	}

	@Override
	public LoadFeesByTransferTypeResponse loadFeesByTransferType(Holder<Header> headerParam,
			LoadFeesByTransferTypeRequest req) {
		LoadFeesByTransferTypeResponse lfbt = new LoadFeesByTransferTypeResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getId());
			List<Fees> fees = baseRepository.getTransferTypeRepository().getListFeeFromTransferTypeID(req.getId());
			lfbt.setFees(fees);
			lfbt.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			lfbt.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return lfbt;
		}
		return lfbt;
	}

	@Override
	public void createTransferTypes(Holder<Header> headerParam, TransferTypeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			Accounts fromAcc = accountValidation.validateAccount(req.getFromAccountID());
			Accounts toAcc = accountValidation.validateAccount(req.getToAccountID());
			baseRepository.getTransferTypeRepository().createTransferType(fromAcc.getId(), toAcc.getId(), req.getName(),
					req.getDescription(), req.getMinAmount(), req.getMaxAmount(), req.getMaxCount());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateTransferTypes(Holder<Header> headerParam, TransferTypeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getId());
			Accounts fromAcc = accountValidation.validateAccount(req.getFromAccountID());
			Accounts toAcc = accountValidation.validateAccount(req.getToAccountID());
			baseRepository.getTransferTypeRepository().updateTransferType(req.getId(), fromAcc.getId(), toAcc.getId(),
					req.getName(), req.getDescription(), req.getMinAmount(), req.getMaxAmount(), req.getMaxCount());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void createTransferTypePermissions(Holder<Header> headerParam, TransferTypePermissionRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypeRepository().createTransferTypePermission(req.getTransferTypeID(),
					req.getGroupID());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateTransferTypePermission(Holder<Header> headerParam, LoadPermissionByTransferTypesRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypeRepository().updatePermissionTransferType(req);
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void deleteTransferTypePermissions(Holder<Header> headerParam, LoadPermissionByTransferTypesRequest req)
			throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			baseRepository.getTransferTypeRepository().deleteTransferTypePermission(req.getId());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void createFees(Holder<Header> headerParam, FeeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypeRepository().createFee(req);
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void updateFees(Holder<Header> headerParam, FeeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypeRepository().updateFee(req);
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public void deleteFees(Holder<Header> headerParam, FeeRequest req) throws TransactionException {
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			baseRepository.getTransferTypeRepository().deleteFee(req.getId(), req.getTransferTypeID());
		} catch (Exception e) {
			throw new TransactionException(e.getMessage());
		}
	}

	@Override
	public LoadPermissionByTransferTypesResponse loadPermissionsByTransferType(Holder<Header> headerParam,
			LoadPermissionByTransferTypesRequest req) {
		LoadPermissionByTransferTypesResponse tid = new LoadPermissionByTransferTypesResponse();
		try {
			webserviceValidation.validateWebservice(headerParam.value.getToken());
			transferTypeValidation.validateTransferType(req.getTransferTypeID());
			List<TransferTypesPermission> transferTypePermission = baseRepository.getTransferTypeRepository()
					.listPermissionByTransferType(req.getTransferTypeID());
			tid.setTransferTypePermissions(transferTypePermission);
			tid.setStatus(StatusBuilder.getStatus(Status.PROCESSED));
		} catch (Exception e) {
			tid.setStatus(StatusBuilder.getStatus(e.getMessage()));
			return tid;
		}
		return tid;
	}

}
