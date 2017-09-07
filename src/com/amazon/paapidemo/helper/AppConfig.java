package com.amazon.paapidemo.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Configuration
@Getter
@Setter
@PropertySource("classpath:config.properties")
public class AppConfig {

	@Value("#{configProperties['AssociateTag']}")
	private String associateTag;
	
	@Value("#{configProperties['AWSAccessKeyId']}")
	private String awsAccessKeyId;

	@Value("#{configProperties['AWSSecretKey']}")
	private String awsSecretKey;

}
