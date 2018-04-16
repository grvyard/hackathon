package com.naukri.aray.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.naukri.aray.model.ArayStatistic;

public interface ArayStatsService {

	public List<ArayStatistic> getStatsForDate(String date) throws ParseException, ClassNotFoundException, SQLException;
	public List<ArayStatistic> getStats();
	public void sendEmail(String date) throws ParseException, ClassNotFoundException, SQLException;
	public void insert(String date) throws ParseException, ClassNotFoundException, SQLException;
}
