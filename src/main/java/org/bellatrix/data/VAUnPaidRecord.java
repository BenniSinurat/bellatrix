package org.bellatrix.data;

import java.util.List;

public class VAUnPaidRecord {
	private List<VARecordView> vaUnPaidRecord;
	private Integer totalRecords;

	public List<VARecordView> getVaPaidRecord() {
		return vaUnPaidRecord;
	}

	public void setVaUnPaidRecord(List<VARecordView> vaUnPaidRecord) {
		this.vaUnPaidRecord = vaUnPaidRecord;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
}
