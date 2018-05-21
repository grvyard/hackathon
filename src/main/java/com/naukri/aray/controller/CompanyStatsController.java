package com.naukri.aray.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naukri.aray.model.CompanyStats;
import com.naukri.aray.model.CompanyStatsList;
import com.naukri.aray.service.CompanyStatsService;

@Controller
@RequestMapping(value = "/companyStats")
public class CompanyStatsController {
	
	@Autowired
	private CompanyStatsService companyStatsService;

	@RequestMapping(value = "/date/{date}/companyId/{companyId}", method = RequestMethod.GET)
	public @ResponseBody CompanyStatsList getFetchCompanyStats(@PathVariable("date") String date, @PathVariable("companyId") String companyId) throws ClassNotFoundException, SQLException {
		return companyStatsService.getCompanyStats(date, companyId);
	}
	
	@RequestMapping(value = "/date/{date}/companyId/{companyId}/verifyStep", method = RequestMethod.GET)
	public @ResponseBody CompanyStatsList getStepFailedAtVerifyStep(@PathVariable("date") String date, @PathVariable("companyId") String companyId) throws ClassNotFoundException, SQLException {
		return companyStatsService.getStepFailedAtVerifyStep(date, companyId);
	}
}
