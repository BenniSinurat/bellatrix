package org.bellatrix.services;

public class LoadAccountPermissionsRequest {


		private Integer id;
		private Integer accountID;
		private Integer groupID;

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

		public Integer getAccountID() {
			return accountID;
		}

		public void setAccountID(Integer accountID) {
			this.accountID = accountID;
		}

	}