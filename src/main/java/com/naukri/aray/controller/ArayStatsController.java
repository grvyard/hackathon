package com.naukri.aray.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naukri.aray.model.ArayStatistic;
import com.naukri.aray.service.ArayStatsService;

@Controller
@RequestMapping("/arayStatistic")
public class ArayStatsController {

	@Autowired
	private ArayStatsService arayStatsService;
	
	@RequestMapping(value = "/{date}", method=RequestMethod.GET)
	public @ResponseBody List<ArayStatistic> getStatsForDate(@PathVariable("date") String date) throws ParseException, ClassNotFoundException, SQLException {
		return arayStatsService.getStatsForDate(date);
	}
	
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public @ResponseBody List<ArayStatistic> getStatsForDate() {
		return arayStatsService.getStats();
	}
	
	@RequestMapping(value="/sendEmail/{date}", method=RequestMethod.GET)
	public @ResponseBody void sendEmail(@PathVariable("date") String date) throws ParseException, ClassNotFoundException, SQLException {
		arayStatsService.sendEmail(date);
		return;
	}
	
//	@RequestMapping(value="/{date}", method=RequestMethod.POST)
//	public @ResponseBody void insertData(@PathVariable("date") String date) throws ParseException, ClassNotFoundException, SQLException {
//		arayStatsService.insert(date);
//	}
}
