package com.naukri.aray.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naukri.aray.service.EmployeeInfoService;

@Controller
@CrossOrigin
@RequestMapping("/hackathon")
public class EmployeeInfoController {

	@Autowired
	private EmployeeInfoService employeeInfoService;
	
	
	@RequestMapping(value = "/getAvgCtc", method=RequestMethod.POST)
	public @ResponseBody Double getAvgCtc(@RequestParam("college") String college, @RequestParam("branch") String branch, @RequestParam("batch") String batch) throws ClassNotFoundException, SQLException {
		return employeeInfoService.getAvgCtc(college, branch, batch);
	}
	
	@RequestMapping(value = "/getAvgCtcOFGivenRange", method=RequestMethod.POST)
	public @ResponseBody Map<String, Double> getAvgCtcOfGivenRange(@RequestParam("college") String college, @RequestParam("branch") String branch, @RequestParam("startBatch") String startBatch, @RequestParam("endBatch") String endBatch) throws ClassNotFoundException, SQLException {
		return employeeInfoService.getAvgCtcOfGivenRange(college, branch, startBatch, endBatch);
	}
	
	@RequestMapping(value = "/getCompaniesListByBatch", method=RequestMethod.POST)
	public @ResponseBody Map<String, Double> getCompaniesListByBatch(@RequestParam("college") String college, @RequestParam("branch") String branch, @RequestParam("batch") String batch) throws ClassNotFoundException, SQLException {
		return employeeInfoService.getCompaniesListByBatch(college, branch, batch);
	}
	
	@RequestMapping(value = "/getCompaniesListOfGivenRange", method=RequestMethod.POST)
	public @ResponseBody Map<String, Double> getCompaniesListByBatch(@RequestParam("college") String college, @RequestParam("branch") String branch, @RequestParam("startBatch") String startBatch, @RequestParam("endBatch") String endBatch) throws ClassNotFoundException, SQLException {
		return employeeInfoService.getCompaniesListByBatch(college, branch, startBatch, endBatch);
	}
	
}
