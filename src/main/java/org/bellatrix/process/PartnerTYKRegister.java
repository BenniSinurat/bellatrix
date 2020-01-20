package org.bellatrix.process;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PartnerTYKRegister {
	private final String CONTENTTYPE = "application/json";

	public String sendPost(String email, String url, String authorization, String managementID, String managementName)
			throws IOException {
		URL obj = new URL(null, url, new sun.net.www.protocol.http.Handler());
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("x-tyk-authorization", authorization);
		con.setRequestProperty("Content-Type", CONTENTTYPE);

		String json = "{\n" + "	        \"allowance\": 1000,\n" + "	        \"rate\": 1000,\n"
				+ "	        \"per\": 60,\n" + "	        \"expires\": -1,\n" + "	        \"quota_max\": -1,\n"
				+ "	        \"quota_renews\": 1505891841,\n" + "	        \"quota_remaining\": -1,\n"
				+ "	        \"quota_renewal_rate\": 60,\n" + "	        \"access_rights\": {\n" + "	            \""
				+ managementID + "\": {\n" + "	                \"api_name\": \"" + managementName + "\",\n"
				+ "	                \"api_id\": \"" + managementID + "\"\n" + "	            }\n" + "	        },\n"
				+ "	        \"org_id\": \"1\",\n" + "	        \"is_inactive\": false,\n" + "	        \"alias\": \""
				+ email + "\"}";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json);
		wr.flush();
		wr.close();

		BufferedReader in;
		if (con.getResponseCode() == 200) {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
}
