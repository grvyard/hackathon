package com.naukri.aray.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Aray_statistic")
public class ArayStatistic {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "date")
	private String date;
	@Column(name = "numofcompaniesdonetoday")
	private Integer numOfCompaniesDoneToday;
	@Column(name = "totalnumofcompaniestillnow")
	private Integer totalNumOfCompaniesTillNow;
	@Column(name = "recievedapplies")
	private Integer recievedApplies;
	@Column(name = "successfullapplies")
	private Integer successfullApplies;
	@Column(name = "failureapplies")
	private Integer failureApplies;
	@Column(name = "jobexpired")
	private Integer jobexpired;
	@Column(name = "internalservererror")
	private Integer internalServerError;
	@Column(name = "countrytype")
	private String countryType;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getNumOfCompaniesDoneToday() {
		return numOfCompaniesDoneToday;
	}
	public void setNumOfCompaniesDoneToday(Integer numOfCompaniesDoneToday) {
		this.numOfCompaniesDoneToday = numOfCompaniesDoneToday;
	}
	public Integer getTotalNumOfCompaniesTillNow() {
		return totalNumOfCompaniesTillNow;
	}
	public void setTotalNumOfCompaniesTillNow(Integer totalNumOfCompaniesTillNow) {
		this.totalNumOfCompaniesTillNow = totalNumOfCompaniesTillNow;
	}
	public Integer getRecievedApplies() {
		return recievedApplies;
	}
	public void setRecievedApplies(Integer recievedApplies) {
		this.recievedApplies = recievedApplies;
	}
	public Integer getSuccessfullApplies() {
		return successfullApplies;
	}
	public void setSuccessfullApplies(Integer successfullApplies) {
		this.successfullApplies = successfullApplies;
	}
	public Integer getFailureApplies() {
		return failureApplies;
	}
	public void setFailureApplies(Integer failureApplies) {
		this.failureApplies = failureApplies;
	}
	public Integer getJobexpired() {
		return jobexpired;
	}
	public void setJobexpired(Integer jobexpired) {
		this.jobexpired = jobexpired;
	}
	public Integer getInternalServerError() {
		return internalServerError;
	}
	public void setInternalServerError(Integer internalServerError) {
		this.internalServerError = internalServerError;
	}
	public String getCountryType() {
		return countryType;
	}
	public void setCountryType(String countryType) {
		this.countryType = countryType;
	}
	
	
	
}
