package com.naukri.aray.scheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.naukri.aray.apply.repository.ApplyLogRepository;
import com.naukri.aray.constants.Constants;
import com.naukri.aray.email.Email;
import com.naukri.aray.email.EmailService;
import com.naukri.aray.email.EmailTemplate;
import com.naukri.aray.model.ArayLog;
import com.naukri.aray.oms.model.ApplyStatus;
import com.naukri.aray.oms.repository.ApplyStatusRepository;
import com.naukri.aray.repository.ArayLogRepository;
import com.naukri.aray.service.ArayStatsService;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	private ArayStatsService arayStatsService;
	
	@Autowired
	EmailService emailService;

	@Autowired
	private ApplyLogRepository applyLogRepository;
	
	@Autowired
	private ApplyStatusRepository applyStatusRepository;
	
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "0 0 11 * * *")
    public void reportCurrentTime() throws ParseException, ClassNotFoundException, SQLException {
    	
    	Date date = new Date();
    	String formattedDateInString = dateFormat.format(date);
    	
    	Calendar c = Calendar.getInstance();
    	c.setTime(dateFormat.parse(formattedDateInString));
    	c.add(Calendar.DATE, -1);  // number of days to add
    	date = c.getTime();
    	formattedDateInString = dateFormat.format(date);
    	arayStatsService.sendEmail(formattedDateInString);
    	
        //data fetching from oms-apply status
    	
    }
    
    @Scheduled(cron = "0 0 8 * * *")
    public void updateNumOfAppliesInOms() throws ClassNotFoundException, SQLException{
    	System.out.println("start");
    	Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn =DriverManager.getConnection("jdbc:mysql://172.10.114.170:3306/mynaukri", "aray", "ar@y|2E");
		//Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mynaukri", "root", "root");

		HashMap<String, String> applies = applyLogRepository.getnumOfApplies(conn);
		System.out.println(applies);
		Connection conn1 = DriverManager.getConnection("jdbc:mysql://172.10.24.101:3307/webjobs", "arayuser", "@pp1yus3r");
		//Connection conn1 = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/webjobs", "root", "root");

		applyStatusRepository.updateNumOfApplies(conn1, applies);
		System.out.println( "Executed");
		
		
		
    }
}

