package org.batch.selenium.service;

import java.util.ArrayList;
import java.util.List;

import org.batch.selenium.entity.SeleniumBatchEntity;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SeleniumBatchService {
	
	private static final String URL = "https://relatedwords.org/relatedto/";
		
	public List<SeleniumBatchEntity> getAllRelated() {
		// khởi tạo selenium headless
		String currentWorkingDir = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", currentWorkingDir + "\\src\\main\\java\\org\\batch\\selenium\\service\\chromedriver.exe");
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setHeadless(true);
		chromeOptions.addArguments("--headless");
		ChromeDriver driver = new ChromeDriver(chromeOptions);
		driver.get(URL + "Hello");
		
		// get data to URL
		final WebElement words = driver.findElement(By.className("words"));
		final List<WebElement> wordList = words.findElements(By.tagName("a"));
		
		// create List<SeleniumBatchEntity>
		List<SeleniumBatchEntity> entities = new ArrayList<SeleniumBatchEntity>();
		int index = 0;
		for (WebElement element : wordList) {
			SeleniumBatchEntity batchEntity = new SeleniumBatchEntity();
			batchEntity.setName(element.getText());
			batchEntity.setId(index);
			entities.add(batchEntity);
			index ++;
		}
		
		return entities;
	}
	
}
