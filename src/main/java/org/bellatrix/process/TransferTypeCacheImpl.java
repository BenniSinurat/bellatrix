package org.bellatrix.process;

import java.util.Collection;
import java.util.Map;

import org.bellatrix.data.TransferTypeKey;
import org.bellatrix.data.TransferTypes;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.MapStore;

public class TransferTypeCacheImpl implements MapStore<TransferTypeKey, TransferTypes> {

	@Autowired
	private BaseRepository baseRepository;

	@Override
	public TransferTypes load(TransferTypeKey arg0) {
		if (arg0.getGroupID() != 0) {
			return baseRepository.getTransferTypeRepository().findTransferTypeByGroupID(arg0.getTrxTypeID(),
					arg0.getGroupID());
		}
		return baseRepository.getTransferTypeRepository().findTransferTypeByID(arg0.getTrxTypeID());
	}

	@Override
	public Map<TransferTypeKey, TransferTypes> loadAll(Collection<TransferTypeKey> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<TransferTypeKey> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TransferTypeKey arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Collection<TransferTypeKey> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(TransferTypeKey arg0, TransferTypes arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeAll(Map<TransferTypeKey, TransferTypes> arg0) {
		// TODO Auto-generated method stub

	}

}
