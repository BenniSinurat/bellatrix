package org.bellatrix.services;

import java.util.List;

import org.bellatrix.data.GlobalConfigs;
import org.bellatrix.data.ResponseStatus;
import org.bellatrix.data.Status;

public class GlobalConfigResponse {

	private List<GlobalConfigs> globalConfig;
	private ResponseStatus status;

	public List<GlobalConfigs> getGlobalConfig() {
		return globalConfig;
	}

	public void setGlobalConfig(List<GlobalConfigs> globalConfig) {
		this.globalConfig = globalConfig;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

}
