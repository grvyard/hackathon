package com.naukri.aray.model;

public class CompanyStats {

	private String companyId;
	private String applyJson;
	private String failedStep;
	private String reason;
	private int count;
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getApplyJson() {
		return applyJson;
	}
	public void setApplyJson(String applyJson) {
		this.applyJson = applyJson;
	}
	public String getFailedStep() {
		return failedStep;
	}
	public void setFailedStep(String failedStep) {
		this.failedStep = failedStep;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}
