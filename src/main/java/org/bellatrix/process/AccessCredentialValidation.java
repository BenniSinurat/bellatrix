package org.bellatrix.process;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.bellatrix.data.Groups;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccessCredentialValidation {

	@Autowired
	private BaseRepository baseRepository;
	@Value("${brute.force.threshold.limiter}")
	private int bruteForceLimiter;
	@Value("${brute.force.validation.enabled}")
	private Boolean bruteForceEnabled;
	private Logger logger = Logger.getLogger(AccessCredentialValidation.class);

	public void blockAttemptValidation(Integer memberID, Integer accessTypeID) {
		Groups group = baseRepository.getGroupsRepository().loadGroupsByMemberID(memberID);
		Integer count = baseRepository.getAccessRepository().countFailedAccessAttempts(memberID, accessTypeID);
		if (count + 1 >= group.getMaxPinTries()) {
			baseRepository.getAccessRepository().blockCredential(memberID, accessTypeID);
			baseRepository.getAccessRepository().clearAccessAttemptsRecord(memberID, accessTypeID);

		} else {
			baseRepository.getAccessRepository().flagAccessAttempts(memberID, accessTypeID);
		}
	}

	public boolean validateBruteForce(Integer memberID, Integer accessTypeID) {
		if (bruteForceEnabled == true) {
			Timestamp status = baseRepository.getAccessRepository().validateBruteForce(memberID, accessTypeID);

			if (status == null) {
				return true;
			}

			DateTime now = new DateTime(status.getTime());
			DateTime then = DateTime.now();
			int s = Seconds.secondsBetween(now, then).getSeconds();
			logger.info("[Access Time DIFF : " + s + "second(s)]");

			if (s <= bruteForceLimiter) {
				return false;
			}
		}
		return true;
	}

}
