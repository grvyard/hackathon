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
		return arayStatisticRepository.findAll();
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
		arayStatsforNI.setCountryType(Constants.NG);
		arayStatsforNI.setDate(date);
		arayStatsforNI.setMapForApplyStatus(mapForNG);
		arayStatsforNI.setNumOfCompaniesDoneToday(companiesDoneTodayNG);
		arayStatsforNI.setTotalNumOfCompaniesTillNow(companiesDoneTillNowNG);
		
		
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
        
		Set<String> setNI = arayStatsforNI.getMapForApplyStatus().keySet();
		List<String> listNI = new ArrayList<>(setNI);
		Map<String, List<ErrorData>> top5FailuresNI = arayLogRepository.findTop5ErrosByType(conn, date, Constants.NI, listNI);
		
		Set<String> setNG = arayStatsforNG.getMapForApplyStatus().keySet();
		List<String> listNG = new ArrayList<>(setNG);
		Map<String, List<ErrorData>> top5FailuresNG = arayLogRepository.findTop5ErrosByType(conn, date, Constants.NG, listNG);
		
		
//        Object[] top5expiredJobsNI = arayLogRepository.findTop5ByExpiredJobsNI(0, dateFormat, Constants.JOB_EXPIRED);
//        Object[] top5InternalServerErrorsNI = arayLogRepository.findTop5ByInternalServerErrorNI(0, dateFormat, Constants.INTERNAL_SERVER_ERROR);
//        
//        //NG - arayLog
//        Object[] top5FailuresNG = arayLogRepository.findTop5ByFailuresNG(0, dateFormat, Constants.STEP_FAILED);
//        Object[] top5expiredJobsNG = arayLogRepository.findTop5ByExpiredJobsNG(0, dateFormat, Constants.JOB_EXPIRED);
//        Object[] top5InternalServerErrorsNG = arayLogRepository.findTop5ByInternalServerErrorNG(0, dateFormat, Constants.INTERNAL_SERVER_ERROR);
//        
        //from ,to and subject
        String from = "aray@localhost";
        String to = environment.getProperty("sendTo");
        String subject = "Aray Service Stats for " + date;
        
        //email template
        EmailTemplate template = new EmailTemplate("hello-world.html");
        String templateText = "<html><body><h1>Aray Service Stats - NI</h1><table style=\"width:100%\"><tr><td><h4>totalUniqueHitsNI</h4></td><td>{{totalUniqueHitsNI}}</td></tr><tr><td><h4>successhits</h4></td><td>{{successhitsNI}}</td></tr><tr><td><h4>failureHits</h4></td><td>{{failureHitsNI}}</td></tr><tr>	<td><h4>expiredJobs</h4></td><td>{{expiredJobsNI}}</td></tr><tr><td><h4>InternalServerError</h4></td><td>{{internalServerErrorNI}}</td></tr><tr><td><h4>companiesDoneTillNow</h4></td><td>{{companiesDoneTillNowNI}}</td></tr><tr><td><h4>companiesDoneToday</h4></td><td>{{companiesDoneTodayNI}}</td>";
//        for (String member : numOfCompaniesByMember.keySet()) {
//			templateText.concat("<td>" + member + ": " + numOfCompaniesByMember.get(member) + "</td>");
//		}
//        templateText = templateText.concat("</tr></table>");
//        
        //adding message for top 5 stats
        templateText = templateText.concat("<h3> Top 5 Failure </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td><td>failedStep</td></tr>");
//        for (Object arayLog : top5FailuresNI) {
//        	if (arayLog instanceof Object[]) {
//        	      Object[] row = (Object[]) arayLog;
//        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td><td>" + row[2] +"</td></tr>");
//        	}
//        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Expired </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
//        for (Object arayLog : top5expiredJobsNI) {
//        	if (arayLog instanceof Object[]) {
//        	      Object[] row = (Object[]) arayLog;
//        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] +"</td></tr>");
//        	}
//        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Internal Server Error </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
//        for (Object arayLog : top5InternalServerErrorsNI) {
//        	if (arayLog instanceof Object[]) {
//        	      Object[] row = (Object[]) arayLog;
//        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td></tr>");
//        	}
//        }
        templateText = templateText.concat("</table>");
        
        
        //adding data for NG
        templateText = templateText.concat("<h1>Aray Service Stats - NG</h1><table style=\\\"width:100%\\\"><tr><td><h4>totalUniqueHitsNG</h4></td><td>{{totalUniqueHitsNG}}</td></tr><tr><td><h4>successhits</h4></td><td>{{successhitsNG}}</td></tr><tr><td><h4>failureHits</h4></td><td>{{failureHitsNG}}</td></tr><tr><td><h4>expiredJobs</h4></td><td>{{expiredJobsNG}}</td></tr><tr><td><h4>InternalServerError</h4></td><td>{{internalServerErrorNG}}</td></tr><tr><td><h4>companiesDoneTillNow</h4></td><td>{{companiesDoneTillNowNG}}</td></tr><tr><td><h4>companiesDoneToday</h4></td><td>{{companiesDoneTodayNG}}</td>");
        templateText = templateText.concat("</tr></table>");
      
        //adding message for top 5 stats
        templateText = templateText.concat("<h3> Top 5 Failure </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td><td>failedStep</td></tr>");
//        for (Object arayLog : top5FailuresNG) {
//        	if (arayLog instanceof Object[]) {
//        	      Object[] row = (Object[]) arayLog;
//        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td><td>" + row[2] +"</td></tr>");
//        	}
//        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Expired </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
//        for (Object arayLog : top5expiredJobsNG) {
//        	if (arayLog instanceof Object[]) {
//        	      Object[] row = (Object[]) arayLog;
//        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] +"</td></tr>");
//        	}
//        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Internal Server Error </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
//        for (Object arayLog : top5InternalServerErrorsNG) {
//        	if (arayLog instanceof Object[]) {
//        	      Object[] row = (Object[]) arayLog;
//        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td></tr>");
//        	}
//        }
        templateText = templateText.concat("</table>");
        
        //end of html body
        templateText = templateText.concat("</body></html>");
        
        //setting data in template
        template.setTemplate(templateText);
        Map<String, Integer> replacements = new HashMap<>();
        replacements.put("companiesDoneTodayNI", arayStatsforNI.getNumOfCompaniesDoneToday());
        replacements.put("companiesDoneTillNowNI", arayStatsforNI.getTotalNumOfCompaniesTillNow());
//        replacements.put("totalUniqueHitsNI", arayStatsforNI.getRecievedApplies());
//        replacements.put("successhitsNI", arayStatsforNI.getSuccessfullApplies());
//        replacements.put("failureHitsNI", arayStatsforNI.getFailureApplies());
//        replacements.put("expiredJobsNI", arayStatsforNI.getJobexpired());
//        replacements.put("internalServerErrorNI", arayStatsforNI.getInternalServerError());
         
        replacements.put("companiesDoneTodayNG", arayStatsforNG.getNumOfCompaniesDoneToday());
        replacements.put("companiesDoneTillNowNG", arayStatsforNG.getTotalNumOfCompaniesTillNow());
//        replacements.put("totalUniqueHitsNG", arayStatsforNG.getRecievedApplies());
//        replacements.put("successhitsNG", arayStatsforNG.getSuccessfullApplies());
//        replacements.put("failureHitsNG", arayStatsforNG.getFailureApplies());
//        replacements.put("expiredJobsNG", arayStatsforNG.getJobexpired());
//        replacements.put("internalServerErrorNG", arayStatsforNG.getInternalServerError());
        
        String message = template.getTemplate(replacements);
        Email email = new Email(from, to, subject, message);
        email.setHtml(true);
        emailService.send(email);
	}
	
	public Connection getDatabaseConnectionARAY() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		//Connection conn = DriverManager.getConnection("jdbc:mysql://172.10.115.91:3306/ARAY", "aray", "Km7Iv80l");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ARAY", "root", "Km7Iv80l");
		return conn;
	}
	
	public Connection getDatabaseConnectionWebJobs() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		//Connection conn = DriverManager.getConnection("jdbc:mysql://172.10.24.101:3307/webjobs", "arayuser", "@pp1yus3r");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ARAY", "root", "Km7Iv80l");
		return conn;
	}
}
