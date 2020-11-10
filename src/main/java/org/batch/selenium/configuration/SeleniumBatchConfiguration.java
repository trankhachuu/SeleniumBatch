package org.batch.selenium.configuration;

import java.io.FileWriter;
import java.io.IOException;
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
import au.com.bytecode.opencsv.CSVWriter;

@Configuration
@EnableBatchProcessing
public class SeleniumBatchConfiguration {

	@Autowired
	private SeleniumBatchService selenium;

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;
	
	protected CSVWriter writer;

	@Bean
	public ItemReader<SeleniumBatchEntity> itemReader() {
		return new ListItemReader<>(selenium.getAllRelated());
	}
	
	public SeleniumBatchConfiguration() {
		String currentWorkingDir = System.getProperty("user.dir");
		try {
			writer = new CSVWriter(new FileWriter(currentWorkingDir + "\\src\\main\\resources\\yourfile.csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			System.out.println(items);
			for (List<SeleniumBatchEntity> item : items) {
				for (SeleniumBatchEntity batchEntity : item) {
					// feed in your array (or convert your data to an array)
					List<String> list = new ArrayList<String>();
					// add some stuff
					list.add(Integer.toString(batchEntity.getId()));
					list.add(batchEntity.getName());
					String[] stringArray = list.toArray(new String[0]);
					writer.writeNext(stringArray); 
					if (batchEntity.getId() == 239) {
						writer.close();
					}
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
