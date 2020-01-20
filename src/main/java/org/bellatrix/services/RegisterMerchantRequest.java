package org.bellatrix.services;

import java.math.BigDecimal;

import org.bellatrix.data.MerchantOwner;

public class RegisterMerchantRequest {
	private String username;
	private Integer groupID;
	private String name;
	private String email;
	private String msisdn;
	private Integer categoryID;
	private Integer subCategoryID;
	private Integer scaleID;
	private String taxCardNo;
	private String storePhotoPath;
	private BigDecimal averageTrxValue;
	private String address;
	private String permissionNumber;

	private MerchantOwner merchantOwner;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
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

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public Integer getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(Integer categoryID) {
		this.categoryID = categoryID;
	}

	public Integer getSubCategoryID() {
		return subCategoryID;
	}

	public void setSubCategoryID(Integer subCategoryID) {
		this.subCategoryID = subCategoryID;
	}

	public Integer getScaleID() {
		return scaleID;
	}

	public void setScaleID(Integer scaleID) {
		this.scaleID = scaleID;
	}

	public String getTaxCardNo() {
		return taxCardNo;
	}

	public void setTaxCardNo(String taxCardNo) {
		this.taxCardNo = taxCardNo;
	}

	public String getStorePhotoPath() {
		return storePhotoPath;
	}

	public void setStorePhotoPath(String storePhotoPath) {
		this.storePhotoPath = storePhotoPath;
	}

	public BigDecimal getAverageTrxValue() {
		return averageTrxValue;
	}

	public void setAverageTrxValue(BigDecimal averageTrxValue) {
		this.averageTrxValue = averageTrxValue;
	}

	public MerchantOwner getMerchantOwner() {
		return merchantOwner;
	}

	public void setMerchantOwner(MerchantOwner merchantOwner) {
		this.merchantOwner = merchantOwner;
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

}
