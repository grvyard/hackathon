package com.naukri.aray.service;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface EmployeeInfoService {

	public Double getAvgCtc(String college, String branch, String batch) throws ClassNotFoundException, SQLException;
	public Map<String, Double> getAvgCtcOfGivenRange(String college, String branch, String startBatch, String endBatch) throws ClassNotFoundException, SQLException;

	public Map<String, Double> getCompaniesListByBatch(String college, String branch, String batch) throws ClassNotFoundException, SQLException;
	public Map<String, Double> getCompaniesListByBatch(String college, String branch, String startBatch, String endBatch) throws ClassNotFoundException, SQLException;
}
