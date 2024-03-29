package org.bellatrix.data;

import java.util.List;

public class VAPaidRecord {
	private List<VAStatusRecordView> vaPaidRecord;
	private Integer totalRecords;

	public List<VAStatusRecordView> getVaPaidRecord() {
		return vaPaidRecord;
	}

	public void setVaPaidRecord(List<VAStatusRecordView> vaPaidRecord) {
		this.vaPaidRecord = vaPaidRecord;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}
}
