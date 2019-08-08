package org.bellatrix.process;

import org.bellatrix.data.RegisterVADoc;
import org.springframework.stereotype.Component;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.MapEvent;

@Component
public class RegisterVAListener implements EntryListener<String, RegisterVADoc> {

	@Override
	public void entryAdded(EntryEvent<String, RegisterVADoc> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void entryUpdated(EntryEvent<String, RegisterVADoc> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void entryRemoved(EntryEvent<String, RegisterVADoc> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void entryEvicted(EntryEvent<String, RegisterVADoc> event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mapCleared(MapEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mapEvicted(MapEvent arg0) {
		// TODO Auto-generated method stub
	}

}
