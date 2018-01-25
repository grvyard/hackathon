package com.naukri.aray.service.Impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.naukri.aray.constants.Constants;
import com.naukri.aray.email.Email;
import com.naukri.aray.email.EmailService;
import com.naukri.aray.email.EmailTemplate;
import com.naukri.aray.model.ArayStatistic;
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
	public List<ArayStatistic> getStatsForDate(String date) throws ParseException {
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
	public void insert(String date) throws ParseException {
		List<ArayStatistic> arayStatsList = fetchArayStats(date);
		for (ArayStatistic arayStatistic : arayStatsList) {
			//arayStatisticRepository.save(arayStatistic);
		}
	}
	
	private List<ArayStatistic> fetchArayStats(String date) throws ParseException {

		// NI - oms
		int companiesDoneTodayNI = applyStatusRepository.countByBitflagDoneAndBitflagDateAndCountryType(Constants.YES, date, Constants.NI);
		int companiesDoneTillNowNI = applyStatusRepository.countByBitflagDoneAndBitflagDateLessThanAndCountryType(Constants.YES, date, Constants.NI);

		// NG - oms
		int companiesDoneTodayNG = applyStatusRepository.countByBitflagDoneAndBitflagDateAndCountryType(Constants.YES, date, Constants.NG);
		int companiesDoneTillNowNG = applyStatusRepository.countByBitflagDoneAndBitflagDateLessThanAndCountryType(Constants.YES, date, Constants.NG);
		List<Integer> allNGCompanies = applyStatusRepository.findCompanyIdsTillNow(Constants.YES, date, Constants.NG);

		// date fetching from aray_log
		SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = dateFormatt.parse(date); 
		

		// NI - arayLog
		int successHitsNI = arayLogRepository.findSuccessHitsNI(1, dateFormat, allNGCompanies);
		int failureHitsNI = arayLogRepository.findFailureHitsNI(0, dateFormat, Constants.STEP_FAILED, allNGCompanies)/2;
		int expiredJobsNI = arayLogRepository.findExpiredJobsNI(0, dateFormat, Constants.JOB_EXPIRED, allNGCompanies)/2;
		int internalServerErrorNI = arayLogRepository.findInternalServerErrorsNI(0, dateFormat, Constants.INTERNAL_SERVER_ERROR, allNGCompanies) / 2;

		// NG - arayLog
		int successHitsNG = arayLogRepository.findSuccessHitsNG(1, dateFormat, allNGCompanies);
		int failureHitsNG = arayLogRepository.findFailureHitsNG(0, dateFormat, Constants.STEP_FAILED, allNGCompanies)/ 2;
		int expiredJobsNG = arayLogRepository.findExpiredJobsNG(0, dateFormat, Constants.JOB_EXPIRED, allNGCompanies)/ 2;
		int internalServerErrorNG = arayLogRepository.findInternalServerErrorsNG(0, dateFormat, Constants.INTERNAL_SERVER_ERROR, allNGCompanies) / 2;
		
		ArayStatistic arayStatsforNI = new ArayStatistic();
		arayStatsforNI.setNumOfCompaniesDoneToday(companiesDoneTodayNI);
		arayStatsforNI.setTotalNumOfCompaniesTillNow(companiesDoneTillNowNI);
		arayStatsforNI.setSuccessfullApplies(successHitsNI);
		arayStatsforNI.setFailureApplies(failureHitsNI);
		arayStatsforNI.setJobexpired(expiredJobsNI);
		arayStatsforNI.setInternalServerError(internalServerErrorNI);
		arayStatsforNI.setCountryType(Constants.NI);
		arayStatsforNI.setDate(date);
		
		ArayStatistic arayStatsforNG = new ArayStatistic();
		arayStatsforNG.setNumOfCompaniesDoneToday(companiesDoneTodayNG);
		arayStatsforNG.setTotalNumOfCompaniesTillNow(companiesDoneTillNowNG);
		arayStatsforNG.setSuccessfullApplies(successHitsNG);
		arayStatsforNG.setFailureApplies(failureHitsNG);
		arayStatsforNG.setJobexpired(expiredJobsNG);
		arayStatsforNG.setInternalServerError(internalServerErrorNG);
		arayStatsforNG.setCountryType(Constants.NG);
		arayStatsforNG.setDate(date);
		
		List<ArayStatistic> arayStatsList = new ArrayList<ArayStatistic>();
		arayStatsList.add(arayStatsforNI);
		arayStatsList.add(arayStatsforNG);
		return arayStatsList;
	}
	
	@Override
	public void sendEmail(String date) throws ParseException {
		
		List<ArayStatistic> arayStats = fetchArayStats(date);
		ArayStatistic arayStatsforNI = arayStats.get(0);
		ArayStatistic arayStatsforNG = arayStats.get(1);
		List<Integer> allNGCompanies = applyStatusRepository.findCompanyIdsTillNow(Constants.YES, date, Constants.NG);
		
		//operation member - oms
        String operationMembers = environment.getProperty(Constants.OPS_TEAM);
        String[] operationMembersList = operationMembers.split(",");
        Map<String, Integer> numOfCompaniesByMember = new HashMap<>(); 
        for (String member : operationMembersList) {
        	Integer memberCount = applyStatusRepository.countByBitflagDoneAndBitflagDateAndPickedBy(Constants.YES, date, member);
        	//templateText = templateText.concat("<td>" + member + ": " + memberCount + "</td>");
        	numOfCompaniesByMember.put(member, memberCount);
		}
        
        //date fetching from aray_log
        SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFormat = dateFormatt.parse(date); 
        
        //NI - arayLog
        Object[] top5FailuresNI = arayLogRepository.findTop5ByFailuresNI(0, dateFormat, Constants.STEP_FAILED, allNGCompanies);
        Object[] top5expiredJobsNI = arayLogRepository.findTop5ByExpiredJobsNI(0, dateFormat, Constants.JOB_EXPIRED, allNGCompanies);
        Object[] top5InternalServerErrorsNI = arayLogRepository.findTop5ByInternalServerErrorNI(0, dateFormat, Constants.INTERNAL_SERVER_ERROR, allNGCompanies);
        
        //NG - arayLog
        Object[] top5FailuresNG = arayLogRepository.findTop5ByFailuresNG(0, dateFormat, Constants.STEP_FAILED, allNGCompanies);
        Object[] top5expiredJobsNG = arayLogRepository.findTop5ByExpiredJobsNG(0, dateFormat, Constants.JOB_EXPIRED, allNGCompanies);
        Object[] top5InternalServerErrorsNG = arayLogRepository.findTop5ByInternalServerErrorNG(0, dateFormat, Constants.INTERNAL_SERVER_ERROR, allNGCompanies);
        
        //from ,to and subject
        String from = "aray@localhost";
        String to = "search@naukri.com";
        String subject = "Aray Service Stats for " + date;
        
        //email template
        EmailTemplate template = new EmailTemplate("hello-world.html");
        String templateText = "<html><body><h1>Aray Service Stats - NI</h1><table style=\"width:100%\"><tr><td><h4>successhits</h4></td><td>{{successhitsNI}}</td></tr><tr><td><h4>failureHits</h4></td><td>{{failureHitsNI}}</td></tr><tr>	<td><h4>expiredJobs</h4></td><td>{{expiredJobsNI}}</td></tr><tr><td><h4>InternalServerError</h4></td><td>{{internalServerErrorNI}}</td></tr><tr><td><h4>companiesDoneTillNow</h4></td><td>{{companiesDoneTillNowNI}}</td></tr><tr><td><h4>companiesDoneToday</h4></td><td>{{companiesDoneTodayNI}}</td>";
        for (String member : numOfCompaniesByMember.keySet()) {
			templateText.concat("<td>" + member + ": " + numOfCompaniesByMember.get(member) + "</td>");
		}
        templateText = templateText.concat("</tr></table>");
        
        //adding message for top 5 stats
        templateText = templateText.concat("<h3> Top 5 Failure </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td><td>failedStep</td></tr>");
        for (Object arayLog : top5FailuresNI) {
        	if (arayLog instanceof Object[]) {
        	      Object[] row = (Object[]) arayLog;
        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td><td>" + row[2] +"</td></tr>");
        	}
        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Expired </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
        for (Object arayLog : top5expiredJobsNI) {
        	if (arayLog instanceof Object[]) {
        	      Object[] row = (Object[]) arayLog;
        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] +"</td></tr>");
        	}
        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Internal Server Error </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
        for (Object arayLog : top5InternalServerErrorsNI) {
        	if (arayLog instanceof Object[]) {
        	      Object[] row = (Object[]) arayLog;
        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td></tr>");
        	}
        }
        templateText = templateText.concat("</table>");
        
        
        //adding data for NG
        templateText = templateText.concat("<h1>Aray Service Stats - NG</h1><table style=\\\"width:100%\\\"><tr><td><h4>successhits</h4></td><td>{{successhitsNG}}</td></tr><tr><td><h4>failureHits</h4></td><td>{{failureHitsNG}}</td></tr><tr><td><h4>expiredJobs</h4></td><td>{{expiredJobsNG}}</td></tr><tr><td><h4>InternalServerError</h4></td><td>{{internalServerErrorNG}}</td></tr><tr><td><h4>companiesDoneTillNow</h4></td><td>{{companiesDoneTillNowNG}}</td></tr><tr><td><h4>companiesDoneToday</h4></td><td>{{companiesDoneTodayNG}}</td>");
        templateText = templateText.concat("</tr></table>");
      
        //adding message for top 5 stats
        templateText = templateText.concat("<h3> Top 5 Failure </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td><td>failedStep</td></tr>");
        for (Object arayLog : top5FailuresNG) {
        	if (arayLog instanceof Object[]) {
        	      Object[] row = (Object[]) arayLog;
        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td><td>" + row[2] +"</td></tr>");
        	}
        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Expired </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
        for (Object arayLog : top5expiredJobsNG) {
        	if (arayLog instanceof Object[]) {
        	      Object[] row = (Object[]) arayLog;
        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] +"</td></tr>");
        	}
        }
        templateText = templateText.concat("</table>");
        
        templateText = templateText.concat("<h3> Top 5 Internal Server Error </h3>");
        templateText = templateText.concat("<table style=\"width:100%\"><tr><td>CompanyId</td><td>count</td></tr>");
        for (Object arayLog : top5InternalServerErrorsNG) {
        	if (arayLog instanceof Object[]) {
        	      Object[] row = (Object[]) arayLog;
        	      templateText = templateText.concat("<tr><td>" + row[0] +   "</td><td>" + row[1] + "</td></tr>");
        	}
        }
        templateText = templateText.concat("</table>");
        
        //end of html body
        templateText = templateText.concat("</body></html>");
        
        //setting data in template
        template.setTemplate(templateText);
        Map<String, Integer> replacements = new HashMap<>();
        replacements.put("companiesDoneTodayNI", arayStatsforNI.getNumOfCompaniesDoneToday());
        replacements.put("companiesDoneTillNowNI", arayStatsforNI.getTotalNumOfCompaniesTillNow());
        replacements.put("successhitsNI", arayStatsforNI.getSuccessfullApplies());
        replacements.put("failureHitsNI", arayStatsforNI.getFailureApplies());
        replacements.put("expiredJobsNI", arayStatsforNI.getJobexpired());
        replacements.put("internalServerErrorNI", arayStatsforNI.getInternalServerError());
         
        replacements.put("companiesDoneTodayNG", arayStatsforNG.getNumOfCompaniesDoneToday());
        replacements.put("companiesDoneTillNowNG", arayStatsforNG.getTotalNumOfCompaniesTillNow());
        replacements.put("successhitsNG", arayStatsforNG.getSuccessfullApplies());
        replacements.put("failureHitsNG", arayStatsforNG.getFailureApplies());
        replacements.put("expiredJobsNG", arayStatsforNG.getJobexpired());
        replacements.put("internalServerErrorNG", arayStatsforNG.getInternalServerError());
        
        String message = template.getTemplate(replacements);
        Email email = new Email(from, to, subject, message);
        email.setHtml(true);
        emailService.send(email);
	}
}
