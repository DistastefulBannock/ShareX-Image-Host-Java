/**
 * 
 */
package best.yiff.host.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import best.yiff.host.service.account.AccountService;
import best.yiff.host.service.account.impl.AccountServiceImpl;
import best.yiff.host.service.storage.StorageService;
import best.yiff.host.service.storage.impl.StorageServiceImpl;

/**
 * @author DistastefulBannock
 *
 */
@Configuration
public class ServiceConfig {
	
	@Bean
	public AccountService accountService() {
		return new AccountServiceImpl();
	}
	
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION).build();
	}
	
	@Bean
	public StorageService storageService() {
		return new StorageServiceImpl();
	}
	
}
