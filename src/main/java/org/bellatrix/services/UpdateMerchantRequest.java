package org.bellatrix.services;

import java.math.BigDecimal;
import org.bellatrix.data.MerchantOwner;

public class UpdateMerchantRequest {

	private Integer id;
	private String username;
	private String name;
	private String email;
	private String msisdn;
	private BigDecimal averageTrxValue;
	private String taxCardNumber;
	private String permissionNumber;
	private String address;
	private String storePhotoPath;
	private MerchantOwner owner;
	private Integer categoryID;
	private Integer subCategoryID;
	private Integer scaleID;
	private Integer groupID;

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

	public MerchantOwner getOwner() {
		return owner;
	}

	public void setOwner(MerchantOwner owner) {
		this.owner = owner;
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

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

}
