package org.bellatrix.process;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogProcessor {

	@Autowired
	private BaseRepository baseRepository;

	public void processLog(HashMap<String, String> req) {
		if (req.get("logger").equalsIgnoreCase("request")) {
			System.out.println("Masuk RequestLog");
			baseRepository.getLogRepository().logRequest(req.get("traceNumber"), req.get("payload"),
					req.get("messageID"), req.get("type"));
		} else {
			System.out.println("Masuk ResponseLog");
			baseRepository.getLogRepository().logResponse(req.get("traceNumber"), req.get("payload"),
					req.get("messageID"), req.get("trxNo"), req.get("status"));
		}
	}

}