package org.bellatrix.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Members implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8205025178339095170L;
	private Integer id;
	private String username;
	private Date createdDate;
	private String formattedCreatedDate;
	private Integer groupID;
	private String name;
	private String email;
	private Boolean emailVerify;
	private String msisdn;
	private String idCardNo;
	private String address;
	private Date dateOfBirth;
	private String placeOfBirth;
	private String motherMaidenName;
	private String nationality;
	private String work;
	private String sex;
	private Boolean kycStatus;
	private List<MemberFields> customFields;
	private List<ExternalMemberFields> externalMembers;
	private List<Billers> billers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public List<MemberFields> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(List<MemberFields> customFields) {
		this.customFields = customFields;
	}

	public List<ExternalMemberFields> getExternalMembers() {
		return externalMembers;
	}

	public void setExternalMembers(List<ExternalMemberFields> externalMembers) {
		this.externalMembers = externalMembers;
	}

	public Boolean getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(Boolean kycStatus) {
		this.kycStatus = kycStatus;
	}

	public List<Billers> getBillers() {
		return billers;
	}

	public void setBillers(List<Billers> billers) {
		this.billers = billers;
	}

	public String getFormattedCreatedDate() {
		return formattedCreatedDate;
	}

	public void setFormattedCreatedDate(String formattedCreatedDate) {
		this.formattedCreatedDate = formattedCreatedDate;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getMotherMaidenName() {
		return motherMaidenName;
	}

	public void setMotherMaidenName(String motherMaidenName) {
		this.motherMaidenName = motherMaidenName;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getEmailVerify() {
		return emailVerify;
	}

	public void setEmailVerify(Boolean emailVerify) {
		this.emailVerify = emailVerify;
	}

}
