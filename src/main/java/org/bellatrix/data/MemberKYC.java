package org.bellatrix.data;

import java.util.Date;

public class MemberKYC {

	private Integer id;
	private Members fromMember;
	private Members validatedMember;
	private Members approvedMember;
	private String imagePath1;
	private String imagePath2;
	private String imagePath3;
	private Groups group;
	private String status;
	private Boolean validated;
	private Boolean approved;
	private String description;
	private String validateDescription;
	private Date requestedDate;
	private Date approvalRequestDate;
	private Date validateDate;
	private Date approvalDate;
	private String formattedRequestedDate;
	private String formattedApprovalRequestedDate;
	private String formattedValidateDate;
	private String formattedApprovalDate;

	public Members getFromMember() {
		return fromMember;
	}

	public void setFromMember(Members fromMember) {
		this.fromMember = fromMember;
	}

	public Members getApprovedMember() {
		return approvedMember;
	}

	public void setApprovedMember(Members approvedMember) {
		this.approvedMember = approvedMember;
	}

	public String getImagePath1() {
		return imagePath1;
	}

	public void setImagePath1(String imagePath1) {
		this.imagePath1 = imagePath1;
	}

	public String getImagePath2() {
		return imagePath2;
	}

	public void setImagePath2(String imagePath2) {
		this.imagePath2 = imagePath2;
	}

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getFormattedRequestedDate() {
		return formattedRequestedDate;
	}

	public void setFormattedRequestedDate(String formattedRequestedDate) {
		this.formattedRequestedDate = formattedRequestedDate;
	}

	public String getFormattedApprovalDate() {
		return formattedApprovalDate;
	}

	public void setFormattedApprovalDate(String formattedApprovalDate) {
		this.formattedApprovalDate = formattedApprovalDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImagePath3() {
		return imagePath3;
	}

	public void setImagePath3(String imagePath3) {
		this.imagePath3 = imagePath3;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Members getValidatedMember() {
		return validatedMember;
	}

	public void setValidatedMember(Members validatedMember) {
		this.validatedMember = validatedMember;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public Date getApprovalRequestDate() {
		return approvalRequestDate;
	}

	public void setApprovalRequestDate(Date approvalRequestDate) {
		this.approvalRequestDate = approvalRequestDate;
	}

	public Date getValidateDate() {
		return validateDate;
	}

	public void setValidateDate(Date validateDate) {
		this.validateDate = validateDate;
	}

	public String getFormattedApprovalRequestedDate() {
		return formattedApprovalRequestedDate;
	}

	public void setFormattedApprovalRequestedDate(String formattedApprovalRequestedDate) {
		this.formattedApprovalRequestedDate = formattedApprovalRequestedDate;
	}

	public String getFormattedValidateDate() {
		return formattedValidateDate;
	}

	public void setFormattedValidateDate(String formattedValidateDate) {
		this.formattedValidateDate = formattedValidateDate;
	}

	public String getValidateDescription() {
		return validateDescription;
	}

	public void setValidateDescription(String validateDescription) {
		this.validateDescription = validateDescription;
	}

}
