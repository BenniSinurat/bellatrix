package org.bellatrix.data;

import java.util.List;

public class VAPaidRecord {
	private List<VARecordView> vaPaidRecord;
	private Integer totalRecords;

	public List<VARecordView> getVaPaidRecord() {
		return vaPaidRecord;
	}

	public void setVaPaidRecord(List<VARecordView> vaPaidRecord) {
		this.vaPaidRecord = vaPaidRecord;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
}
