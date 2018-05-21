package com.naukri.aray.service;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.naukri.aray.model.CompanyStatsList;

@Service
public interface CompanyStatsService {

	public CompanyStatsList getCompanyStats(String date, String companyId) throws ClassNotFoundException, SQLException;
	
	public CompanyStatsList getStepFailedAtVerifyStep(String date, String companyId) throws ClassNotFoundException, SQLException;
}
