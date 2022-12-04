/**
 * 
 */
package best.yiff.host.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
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
	
	@Value(value = "${s3.accessKey}")
	private String s3AccessKey;
	
	@Value(value = "${s3.secretKey}")
	private String s3SecretKey;
	
	@Value(value = "${s3.region}")
	private String s3Region;
	
	@Bean
	public AccountService accountService() {
		return new AccountServiceImpl();
	}
	
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3AccessKey, s3SecretKey)))
				.withRegion(Regions.fromName(s3Region)).build();
	}
	
	@Bean
	public StorageService storageService() {
		return new StorageServiceImpl();
	}
	
}
