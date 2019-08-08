package org.bellatrix.auth;

import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class HashProcessor {

	private Logger logger = Logger.getLogger(HashProcessor.class);

	public String encodeHash(TreeMap<String, String> req, String secret) {

		StringBuffer sb = new StringBuffer();
		Set<String> keys = req.keySet();
		for (String k : keys) {
			sb.append("/" + k + "=" + req.get(k));
		}

		String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex("secret=" + secret + sb.toString());
		logger.debug("[Hash calculation : secret=" + secret + sb.toString() + "]");
		return sha256hex;
	}

}
