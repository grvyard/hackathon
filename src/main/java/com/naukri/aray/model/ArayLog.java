package com.naukri.aray.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ARAY_LOG")
public class ArayLog {

	@Id
	@Column(name = "id")
	private int id;
	@Column(name = "companyid")
	private int companyId;
	@Column(name = "status")
	private int status;
	@Column(name = "reason")
	private String reason;
	@Column(name = "failedstep")
	private String failedStep;
	@Column(name = "createdat")
	private Timestamp createdAt;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getFailedStep() {
		return failedStep;
	}
	public void setFailedStep(String failedStep) {
		this.failedStep = failedStep;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}
