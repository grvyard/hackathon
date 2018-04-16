package com.naukri.aray.scheduler;

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

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "0 0 11 * * *")
    public void reportCurrentTime() throws ParseException {
    	
    	Date date = new Date();
    	String formattedDateInString = dateFormat.format(date);
    	
    	Calendar c = Calendar.getInstance();
    	c.setTime(dateFormat.parse(formattedDateInString));
    	c.add(Calendar.DATE, -1);  // number of days to add
    	date = c.getTime();
    	formattedDateInString = dateFormat.format(date);
    	//arayStatsService.sendEmail(formattedDateInString);
    	
        //data fetching from oms-apply status
    	
    }
    
    @Scheduled(cron = "0 0 0/1 * * *")
    public void getCurrentStats() throws ParseException {
    	
    	Date date = new Date();
    	String formattedDateInString = dateFormat.format(date);
    	
    	Calendar c = Calendar.getInstance();
    	c.setTime(dateFormat.parse(formattedDateInString));
    	c.add(Calendar.DATE, -1);  // number of days to add
    	date = c.getTime();
    	formattedDateInString = dateFormat.format(date);
    	//arayStatsService.getStatsForDate(formattedDateInString);
    	
        //data fetching from oms-apply status
    	
     }
}

