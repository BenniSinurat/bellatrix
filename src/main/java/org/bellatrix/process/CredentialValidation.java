package org.bellatrix.process;

import org.bellatrix.data.Accesses;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.StatusBuilder;
import org.bellatrix.data.TransactionException;
import org.bellatrix.data.WebServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CredentialValidation {

	@Autowired
	private AccessCredentialValidation accessValidation;
	@Autowired
	private BaseRepository baseRepository;

	public void validateCredential(WebServices ws, Integer accessTypeID, String credential, Members member)
			throws TransactionException {
		if (ws.isSecureTransaction()) {

			Accesses access = baseRepository.getAccessRepository().loadCredentialByUsername(member.getUsername(),
					accessTypeID);
			if (access == null) {
				throw new TransactionException(String.valueOf(Status.ACCESS_DENIED));
			}

			if (access.isBlocked()) {
				throw new TransactionException(String.valueOf(Status.BLOCKED));
			}

			if (!access.getPin().equals(Utils.getMD5Hash(credential))) {

				if (accessValidation.validateBruteForce(member.getId(), accessTypeID) == false) {
					throw new TransactionException(String.valueOf(Status.ACCESS_DENIED));
				}

				accessValidation.blockAttemptValidation(member.getId(), accessTypeID);
				throw new TransactionException(String.valueOf(Status.INVALID));
			}
		}
	}

	public void validateCredential(Integer accessTypeID, String credential, Members member)
			throws TransactionException {
		Accesses access = baseRepository.getAccessRepository().loadCredentialByUsername(member.getUsername(),
				accessTypeID);
		if (access == null) {
			throw new TransactionException(String.valueOf(Status.INVALID_PARAMETER));
		}

		if (access.isBlocked()) {
			throw new TransactionException(String.valueOf(Status.BLOCKED));
		}

		if (!access.getPin().equals(Utils.getMD5Hash(credential))) {

			if (accessValidation.validateBruteForce(member.getId(), accessTypeID) == false) {
				throw new TransactionException(String.valueOf(Status.ACCESS_DENIED));
			}

			accessValidation.blockAttemptValidation(member.getId(), accessTypeID);
			throw new TransactionException(String.valueOf(Status.INVALID));
		}
	}

}
