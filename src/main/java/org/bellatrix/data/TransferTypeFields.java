package org.bellatrix.data;

public class TransferTypeFields {

	private Integer id;
	private Integer fromAccounts;
	private Integer toAccounts;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getFromAccounts() {
		return fromAccounts;
	}

	public void setFromAccounts(Integer fromAccounts) {
		this.fromAccounts = fromAccounts;
	}

	public Integer getToAccounts() {
		return toAccounts;
	}

	public void setToAccounts(Integer toAccounts) {
		this.toAccounts = toAccounts;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
