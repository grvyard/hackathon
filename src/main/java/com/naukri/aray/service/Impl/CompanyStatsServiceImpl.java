package com.naukri.aray.service.Impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.naukri.aray.model.CompanyStats;
import com.naukri.aray.model.CompanyStatsList;
import com.naukri.aray.repository.ArayLogRepository;
import com.naukri.aray.service.ArayStatsService;
import com.naukri.aray.service.CompanyStatsService;

@Component
public class CompanyStatsServiceImpl implements CompanyStatsService{

	@Autowired
	ArayStatsService arayStatsService;
	
	@Autowired
	ArayLogRepository arayLogRepository;
	
	public CompanyStatsList getCompanyStats(String date, String companyId) throws ClassNotFoundException, SQLException {
		Connection conn = arayStatsService.getDatabaseConnectionARAY();
		List<CompanyStats> companyList = arayLogRepository.fetchCompanyStats(date, companyId, conn);
		CompanyStatsList companyStatsList = new CompanyStatsList();
		companyStatsList.setCompanyStatsList(companyList);
		conn.close();
		return companyStatsList;
	}
	
	public CompanyStatsList getStepFailedAtVerifyStep(String date, String companyId) throws ClassNotFoundException, SQLException {
		Connection conn = arayStatsService.getDatabaseConnectionARAY();
		List<CompanyStats> companyList = arayLogRepository.getStepFailedAtVerifyStep(date, companyId, conn);
		CompanyStatsList companyStatsList = new CompanyStatsList();
		companyStatsList.setCompanyStatsList(companyList);
		conn.close();
		return companyStatsList;
	}
	
}
