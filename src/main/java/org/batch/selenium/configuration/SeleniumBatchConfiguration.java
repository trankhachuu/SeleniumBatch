package org.batch.selenium.configuration;

import java.util.ArrayList;
import java.util.List;

import org.batch.selenium.entity.SeleniumBatchEntity;
import org.batch.selenium.service.SeleniumBatchService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SeleniumBatchConfiguration {

	@Autowired
	private SeleniumBatchService selenium;

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public ItemReader<SeleniumBatchEntity> itemReader() {
		return new ListItemReader<>(selenium.getAllRelated());
	}

	@Bean
	public ItemProcessor<SeleniumBatchEntity, List<SeleniumBatchEntity>> itemProcessor() {
		return item -> {
			List<SeleniumBatchEntity> result = new ArrayList<SeleniumBatchEntity>();
			result.add(item);
			return result;
		};
	}

	@Bean
	public ItemWriter<List<SeleniumBatchEntity>> itemWriter() {
		return items -> {
			for (List<SeleniumBatchEntity> item : items) {
				for (SeleniumBatchEntity batchEntity : item) {
					System.out.println("SeleniumBatchEntity = " + batchEntity.getName());
				}
			}
		};
	}

	@Bean
	public Step step() {
		return steps.get("step").<SeleniumBatchEntity, List<SeleniumBatchEntity>>chunk(2).reader(itemReader())
				.processor(itemProcessor()).writer(itemWriter()).build();
	}

	@Bean
	public Job job() {
		return jobs.get("job").start(step()).build();
	}
	
}
