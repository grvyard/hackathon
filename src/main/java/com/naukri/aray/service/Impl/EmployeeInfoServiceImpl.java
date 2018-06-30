package com.naukri.aray.service.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.naukri.aray.apply.repository.EmployeeInfoRepository;
import com.naukri.aray.service.EmployeeInfoService;

@Component
public class EmployeeInfoServiceImpl implements EmployeeInfoService {

	@Autowired
	private EmployeeInfoRepository employeeInfoRepository;
	
	Logger logger = Logger.getLogger(EmployeeInfoServiceImpl.class.getName());

	@Override
	public Double getAvgCtc(String college, String branch, String batch) throws ClassNotFoundException, SQLException {
		Connection conn = getDatabaseConnectionEmployeeInfo();
		return  employeeInfoRepository.findAvgPackageOfThatBatch(conn, college, branch, batch);
		
	}
	
	@Override
	public Map<String, Double> getAvgCtcOfGivenRange(String college, String branch, String startBatch, String endBatch) throws ClassNotFoundException, SQLException {
		Connection conn = getDatabaseConnectionEmployeeInfo();
		
		return employeeInfoRepository.findAvgPackageOfGivenRange(conn, college, branch, startBatch, endBatch);
	}
	
	@Override
	public Map<String, Double> getCompaniesListByBatch(String college, String branch, String batch) throws ClassNotFoundException, SQLException {
		Connection conn = getDatabaseConnectionEmployeeInfo();
		
		Map<String, Double> companyBypercentage = new HashMap<>();
		
		HashMap<String, Integer> map = new HashMap<>();
		int size = 0;
		List<String> list = employeeInfoRepository.getCompaniesListByBatch(conn, college, branch, batch);
		for (String companiesOfCandidateTillNow : list) {
			String[] companyList  = companiesOfCandidateTillNow.split(",");
			size += companyList.length;
			for (String company : companyList) {
				company = company.toUpperCase();
				if (map.containsKey(company)) {
					map.put(company, map.get(company) + 1);
				} else {
					map.put(company, 1);
				}
			}
		}
		
		for (String company : map.keySet()) {
			companyBypercentage.put(company, (double) ((map.get(company) * 100)/size));
		}
		return companyBypercentage;
		
	}
	
	@Override
	public Map<String, Double> getCompaniesListByBatch(String college, String branch, String startBatch,
			String endBatch) throws ClassNotFoundException, SQLException {
		Connection conn = getDatabaseConnectionEmployeeInfo();
		
		Map<String, Double> companyBypercentage = new HashMap<>();
		
		HashMap<String, Integer> map = new HashMap<>();
		int size = 0;
		List<String> list = employeeInfoRepository.getCompaniesListByGivenRange(conn, college, branch, startBatch, endBatch);
		for (String companiesOfCandidateTillNow : list) {
			String[] companyList  = companiesOfCandidateTillNow.split(",");
			size += companyList.length;
			for (String company : companyList) {
				company = company.toUpperCase();
				if (map.containsKey(company)) {
					map.put(company, map.get(company) + 1);
				} else {
					map.put(company, 1);
				}
			}
		}
		
		for (String company : map.keySet()) {
			companyBypercentage.put(company, (double) ((map.get(company) * 100)/size));
		}
		return companyBypercentage;
	}

	public Connection getDatabaseConnectionEmployeeInfo() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hackathon", "root", "Km7Iv80l");
		//Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ARAY", "root", "Km7Iv80l");
		return conn;
	}

	
}
