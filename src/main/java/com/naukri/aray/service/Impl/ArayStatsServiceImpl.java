package com.naukri.aray.service.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.naukri.aray.constants.Constants;
import com.naukri.aray.email.Email;
import com.naukri.aray.email.EmailService;
import com.naukri.aray.email.EmailTemplate;
import com.naukri.aray.model.ArayStatistic;
import com.naukri.aray.model.ErrorData;
import com.naukri.aray.oms.repository.ApplyStatusRepository;
import com.naukri.aray.repository.ArayLogRepository;
import com.naukri.aray.repository.ArayStatisticRepository;
import com.naukri.aray.service.ArayStatsService;

@Component
public class ArayStatsServiceImpl implements ArayStatsService {

	@Autowired
	private ApplyStatusRepository applyStatusRepository;
	
	@Autowired
	private ArayLogRepository arayLogRepository; 
	
	@Autowired
	private ArayStatisticRepository arayStatisticRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private Environment environment;
	
	Logger logger = Logger.getLogger(ArayStatsServiceImpl.class.getName());

	@Override
	public List<ArayStatistic> getStatsForDate(String date) throws ParseException, ClassNotFoundException, SQLException {
		List<ArayStatistic> arayStats =  new ArrayList<>() ;//arayStatisticRepository.findOneByDate(date);
		if (!arayStats.isEmpty()) {
			return arayStats;
		} else {
			return fetchArayStats(date);
		}
	}
	
	@Override
	public List<ArayStatistic> getStats() {
		List<ArayStatistic> a = new ArrayList<>();
		return a;
	}
	
	@Override
	public void insert(String date) throws ParseException, ClassNotFoundException, SQLException {
		List<ArayStatistic> arayStatsList = fetchArayStats(date);
		for (ArayStatistic arayStatistic : arayStatsList) {
			//arayStatisticRepository.save(arayStatistic);
		}
	}
	
	private List<ArayStatistic> fetchArayStats(String date) throws ParseException, ClassNotFoundException, SQLException {

		Connection conn = getDatabaseConnectionWebJobs();
		
		// NI - oms
		int companiesDoneTodayNI = applyStatusRepository.countByBitflagDoneAndBitflagDateAndCountryType(conn, date, Constants.NI);
		int companiesDoneTillNowNI = applyStatusRepository.countByBitflagDoneAndBitflagDateLessThanAndCountryType(conn, date, Constants.NI);

		// NG - oms
		int companiesDoneTodayNG = applyStatusRepository.countByBitflagDoneAndBitflagDateAndCountryType(conn, date, Constants.NG);
		int companiesDoneTillNowNG = applyStatusRepository.countByBitflagDoneAndBitflagDateLessThanAndCountryType(conn, date, Constants.NG);
		conn.close();
		
		// date fetching from aray_log
		SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = dateFormatt.parse(date); 
		
		Connection conn1 = getDatabaseConnectionARAY();
		Map<String, Integer> mapForNI = arayLogRepository.findReasonandCountMap(conn1, Constants.NI, date);
		Map<String, Integer> mapForNG = arayLogRepository.findReasonandCountMap(conn1, Constants.NG, date);
		conn1.close();
		
		ArayStatistic arayStatsforNI = new ArayStatistic();
		arayStatsforNI.setCountryType(Constants.NI);
		arayStatsforNI.setDate(date);
		arayStatsforNI.setMapForApplyStatus(mapForNI);
		arayStatsforNI.setNumOfCompaniesDoneToday(companiesDoneTodayNI);
		arayStatsforNI.setTotalNumOfCompaniesTillNow(companiesDoneTillNowNI);
		
		ArayStatistic arayStatsforNG = new ArayStatistic();
		arayStatsforNG.setCountryType(Constants.NG);
		arayStatsforNG.setDate(date);
		arayStatsforNG.setMapForApplyStatus(mapForNG);
		arayStatsforNG.setNumOfCompaniesDoneToday(companiesDoneTodayNG);
		arayStatsforNG.setTotalNumOfCompaniesTillNow(companiesDoneTillNowNG);
		
		
		List<ArayStatistic> arayStatsList = new ArrayList<ArayStatistic>();
		arayStatsList.add(arayStatsforNI);
		arayStatsList.add(arayStatsforNG);
		return arayStatsList;
	}
	
	@Override
	public void sendEmail(String date) throws ParseException, ClassNotFoundException, SQLException {
		
		List<ArayStatistic> arayStats = fetchArayStats(date);
		ArayStatistic arayStatsforNI = arayStats.get(0);
		ArayStatistic arayStatsforNG = arayStats.get(1);
        
        //date fetching from aray_log
        SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = dateFormatt.parse(date); 
        
		Connection conn = getDatabaseConnectionARAY();
        Set<String> s = arayStatsforNI.getMapForApplyStatus().keySet();
		//NI - arayLog
		
		Map<String, List<ErrorData>> top5ErrorsOfAllCategoriesNI = arayLogRepository.findTop5ErrosByType(conn, date, Constants.NI, s);
		Map<String, List<ErrorData>> top5ErrorsOfAllCategoriesNG = arayLogRepository.findTop5ErrosByType(conn, date, Constants.NG, s);
		conn.close();
		
		//from ,to and subject
        String from = "aray@localhost";
        String to = environment.getProperty("sendTo");
        String subject = "Aray Service Stats for " + date;
        
        //email template
        EmailTemplate template = new EmailTemplate("hello-world.html");
        
        //setting data for NI
        String templateText = "<html><body><h1>Aray Service Stats - NI</h1><table style=\"width:100%\">";
        for (String status : s) {
        	if (arayStatsforNI.getMapForApplyStatus().get(status) != null) {
        		templateText = templateText.concat("<tr><td><h4>" + status + "</h4></td><td>" + arayStatsforNI.getMapForApplyStatus().get(status) + "</td></tr>");
        	}
		}
        templateText = templateText.concat("<tr><td><h4>companiesDoneTillNow</h4></td><td>" + arayStatsforNI.getTotalNumOfCompaniesTillNow() + "</td></tr>");
        templateText = templateText.concat("<tr><td><h4>companiesDoneToday</h4></td><td>" + arayStatsforNI.getNumOfCompaniesDoneToday() + "</td></tr>");
        templateText = templateText.concat("</table>");
        
        for (String status : top5ErrorsOfAllCategoriesNI.keySet()) {
        	if (top5ErrorsOfAllCategoriesNI.get(status) == null) {
        		continue;
        	}
        	templateText = templateText.concat("<h3> Top " + status + "</h3>");
        	templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td><td>Step</td></tr>");
        	for (ErrorData errorData : top5ErrorsOfAllCategoriesNI.get(status)) {
				templateText = templateText.concat("<tr><td>" + errorData.getCompanyId() +   "</td><td>" + errorData.getCnt() + "</td><td>" + errorData.getFailedStep() +"</td></tr>");
			}
        	templateText = templateText.concat("</table>");
		}
        
        //setting data for NG
        templateText = templateText.concat("<html><body><h1>Aray Service Stats - NG</h1><table style=\"width:100%\">");
        for (String status : s) {
        	if (arayStatsforNG.getMapForApplyStatus().get(status) != null) {
        		templateText = templateText.concat("<tr><td><h4>" + status + "</h4></td><td>" + arayStatsforNG.getMapForApplyStatus().get(status) + "</td></tr>");
        	}
		}
        templateText = templateText.concat("<tr><td><h4>companiesDoneTillNow</h4></td><td>" + arayStatsforNG.getTotalNumOfCompaniesTillNow() + "</td></tr>");
        templateText = templateText.concat("<tr><td><h4>companiesDoneToday</h4></td><td>" + arayStatsforNG.getNumOfCompaniesDoneToday() + "</td></tr>");
        templateText = templateText.concat("</table>");
        
        for (String status : top5ErrorsOfAllCategoriesNG.keySet()) {
        	if (top5ErrorsOfAllCategoriesNG.get(status) == null) {
        		continue;
        	}
        	templateText = templateText.concat("<h3> Top " + status + "</h3>");
        	templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td><td>Step</td></tr>");
        	for (ErrorData errorData : top5ErrorsOfAllCategoriesNG.get(status)) {
				templateText = templateText.concat("<tr><td>" + errorData.getCompanyId() +   "</td><td>" + errorData.getCnt() + "</td><td>" + errorData.getFailedStep() +"</td></tr>");
			}
        	templateText = templateText.concat("</table>");
		}
        templateText = templateText.concat("</body></html>");
        
        Email email = new Email(from, to, subject, templateText);
        email.setHtml(true);
        emailService.send(email);
	}
	
	public Connection getDatabaseConnectionARAY() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://172.10.115.91:3306/ARAY", "aray", "Km7Iv80l");
		//Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ARAY", "root", "Km7Iv80l");
		return conn;
	}
	
	public Connection getDatabaseConnectionWebJobs() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://172.10.24.101:3307/webjobs", "arayuser", "@pp1yus3r");
		//Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ARAY", "root", "Km7Iv80l");
		return conn;
	}
}
