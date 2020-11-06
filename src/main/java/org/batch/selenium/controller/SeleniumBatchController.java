package org.batch.selenium.controller;

import org.batch.selenium.configuration.SeleniumBatchConfiguration;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/load")
public class SeleniumBatchController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

//	@Autowired
//	private SeleniumBatchService selenium;
	
	@GetMapping
    public BatchStatus load() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
		 ApplicationContext context = new AnnotationConfigApplicationContext(SeleniumBatchConfiguration.class);
	        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
	        Job job = context.getBean(Job.class);
	        JobExecution jobExecution  = jobLauncher.run(job, new JobParameters());
	        while (jobExecution.isRunning()) {
	            System.out.println("...");
	        }
	        return jobExecution.getStatus();
    }
	
}
