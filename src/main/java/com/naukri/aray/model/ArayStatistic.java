package com.naukri.aray.model;

import java.util.Map;

public class ArayStatistic {

	private Integer id;
	private String date;
	private Integer numOfCompaniesDoneToday;
	private Integer totalNumOfCompaniesTillNow;
	private String countryType;
	private Map<String, Integer> mapForApplyStatus;
	private Integer ApplyOnEmail;
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
	public String getCountryType() {
		return countryType;
	}
	public void setCountryType(String countryType) {
		this.countryType = countryType;
	}
	public Map<String, Integer> getMapForApplyStatus() {
		return mapForApplyStatus;
	}
	public void setMapForApplyStatus(Map<String, Integer> mapForApplyStatus) {
		this.mapForApplyStatus = mapForApplyStatus;
	}
	public Integer getApplyOnEmail() {
		return ApplyOnEmail;
	}
	public void setApplyOnEmail(Integer applyOnEmail) {
		ApplyOnEmail = applyOnEmail;
	}
	
	
	
	
}
