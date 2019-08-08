package org.bellatrix.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

@Component
public class JWTProcessor {

	public String createJWTHMAC256(String username, String secret) {
		try {
			String token = JWT.create().withIssuer("Bellatrix").withSubject("auth").withJWTId(username)
					.sign(Algorithm.HMAC256(secret));
			return token;
		} catch (Exception exception) {
			exception.printStackTrace();
			return "0";
		}
	}

	public DecodedJWT verifyJWTHMAC256(String token, String secret) throws Exception {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withIssuer("Bellatrix").build();
		DecodedJWT jwt = verifier.verify(token);
		return jwt;
	}

	public String decodeJWTHMAC256(String token) throws Exception {
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getId();
	}

}
