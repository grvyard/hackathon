package com.naukri.aray.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class ArayStatistic {

	private Integer id;
	private String date;
	private Integer numOfCompaniesDoneToday;
	private Integer totalNumOfCompaniesTillNow;
	private String countryType;
	private Map<String, Integer> mapForApplyStatus;
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
	
	
	
}
