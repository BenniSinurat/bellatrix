package org.bellatrix.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Merchants implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2459348856465227344L;
	private Integer id;
	private String username;
	private Date createdDate;
	private String formattedCreatedDate;
	private String name;
	private String email;
	private String msisdn;
	private BigDecimal averageTrxValue;
	private String taxCardNumber;
	private String permissionNumber;
	private String address;
	private String storePhotoPath;
	private Boolean approved;
	private String status;
	private String description;
	private Date approvalDate;
	private String formattedApprovalDate;
	private MemberView approvedBy;
	private MemberView createdBy;
	private Groups group;
	private MerchantOwner owner;
	private MerchantCategory category;
	private MerchantSubCategory subCategory;
	private MerchantBusinessScale scale;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getFormattedCreatedDate() {
		return formattedCreatedDate;
	}

	public void setFormattedCreatedDate(String formattedCreatedDate) {
		this.formattedCreatedDate = formattedCreatedDate;
	}

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

	public BigDecimal getAverageTrxValue() {
		return averageTrxValue;
	}

	public void setAverageTrxValue(BigDecimal averageTrxValue) {
		this.averageTrxValue = averageTrxValue;
	}

	public String getTaxCardNumber() {
		return taxCardNumber;
	}

	public void setTaxCardNumber(String taxCardNumber) {
		this.taxCardNumber = taxCardNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPermissionNumber() {
		return permissionNumber;
	}

	public void setPermissionNumber(String permissionNumber) {
		this.permissionNumber = permissionNumber;
	}

	public String getStorePhotoPath() {
		return storePhotoPath;
	}

	public void setStorePhotoPath(String storePhotoPath) {
		this.storePhotoPath = storePhotoPath;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getFormattedApprovalDate() {
		return formattedApprovalDate;
	}

	public void setFormattedApprovalDate(String formattedApprovalDate) {
		this.formattedApprovalDate = formattedApprovalDate;
	}

	public MemberView getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(MemberView approvedBy) {
		this.approvedBy = approvedBy;
	}

	public MemberView getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(MemberView createdBy) {
		this.createdBy = createdBy;
	}

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

	public MerchantOwner getOwner() {
		return owner;
	}

	public void setOwner(MerchantOwner owner) {
		this.owner = owner;
	}

	public MerchantCategory getCategory() {
		return category;
	}

	public void setCategory(MerchantCategory category) {
		this.category = category;
	}

	public MerchantSubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(MerchantSubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public MerchantBusinessScale getScale() {
		return scale;
	}

	public void setScale(MerchantBusinessScale scale) {
		this.scale = scale;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

}
