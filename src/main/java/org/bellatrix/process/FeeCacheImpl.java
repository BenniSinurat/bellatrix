package org.bellatrix.process;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bellatrix.data.Fees;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.MapStore;

public class FeeCacheImpl implements MapStore<Integer, List<Fees>> {

	@Autowired
	private BaseRepository baseRepository;

	@Override
	public List<Fees> load(Integer arg0) {
		return baseRepository.getTransferTypeRepository().getFeeFromTransferTypeID(arg0);
	}

	@Override
	public Map<Integer, List<Fees>> loadAll(Collection<Integer> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Integer> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Integer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll(Collection<Integer> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void store(Integer arg0, List<Fees> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeAll(Map<Integer, List<Fees>> arg0) {
		// TODO Auto-generated method stub

	}

}
