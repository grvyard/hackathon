package com.naukri.aray.oms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APPLY_STATUS")
public class ApplyStatus {

	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "compid")
	private int compId;
	@Column(name = "bitflagdone")
	private String bitflagDone;
	@Column(name = "bitflagdate")
	private String bitflagDate;
	@Column(name = "pickedby")
	private String pickedBy;
	@Column(name = "countrytype")
	private String countryType;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCompId() {
		return compId;
	}
	public void setCompId(int compId) {
		this.compId = compId;
	}
	public String getBitflagDone() {
		return bitflagDone;
	}
	public void setBitflagDone(String bitflagDone) {
		this.bitflagDone = bitflagDone;
	}
	public String getBitflagDate() {
		return bitflagDate;
	}
	public void setBitflagDate(String bitflagDate) {
		this.bitflagDate = bitflagDate;
	}
	public String getPickedBy() {
		return pickedBy;
	}
	public void setPickedBy(String pickedBy) {
		this.pickedBy = pickedBy;
	}
	public String getCountryType() {
		return countryType;
	}
	public void setCountryType(String countryType) {
		this.countryType = countryType;
	}
	
}
