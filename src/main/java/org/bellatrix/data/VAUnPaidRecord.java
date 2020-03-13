package org.bellatrix.data;

import java.util.List;

public class VAUnPaidRecord {
	private List<VAStatusRecordView> vaUnPaidRecord;
	private Integer totalRecords;

	public List<VAStatusRecordView> getVaUnPaidRecord() {
		return vaUnPaidRecord;
	}

	public void setVaUnPaidRecord(List<VAStatusRecordView> vaUnPaidRecord) {
		this.vaUnPaidRecord = vaUnPaidRecord;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
}
