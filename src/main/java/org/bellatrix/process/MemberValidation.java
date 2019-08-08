package org.bellatrix.process;

import java.util.List;
import org.bellatrix.data.ExternalMemberFields;
import org.bellatrix.data.Members;
import org.bellatrix.data.Status;
import org.bellatrix.data.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberValidation {

	@Autowired
	private BaseRepository baseRepository;

	public Members validateMember(String username, boolean source) throws TransactionException {
		Members member = baseRepository.getMembersRepository().findOneMembers("username", username);
		if (member == null) {
			if (source) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			} else {
				throw new TransactionException(String.valueOf(Status.DESTINATION_MEMBER_NOT_FOUND));
			}
		}
		List<ExternalMemberFields> externalMembers = baseRepository.getMembersRepository()
				.loadExternalMemberFields(member.getId());
		member.setExternalMembers(externalMembers);
		return member;
	}

	public Members validateMemberID(Integer memberID, boolean source) throws TransactionException {
		Members member = baseRepository.getMembersRepository().findOneMembers("id", memberID);
		if (member == null) {
			if (source) {
				throw new TransactionException(String.valueOf(Status.MEMBER_NOT_FOUND));
			} else {
				throw new TransactionException(String.valueOf(Status.DESTINATION_MEMBER_NOT_FOUND));
			}
		}
		List<ExternalMemberFields> externalMembers = baseRepository.getMembersRepository()
				.loadExternalMemberFields(member.getId());
		member.setExternalMembers(externalMembers);
		return member;
	}

}
