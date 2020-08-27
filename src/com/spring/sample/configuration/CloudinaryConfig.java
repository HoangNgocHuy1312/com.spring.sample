package com.spring.sample.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.spring.sample.uploader.ImageUploader;
import com.spring.sample.uploader.cloudinary.CloudinaryImageUploader;

@Configuration
@PropertySource("classpath:application.properties")
public class CloudinaryConfig {

	@Value("${cloudinary.cloud_name}")
	private String cloudName;
	@Value("${cloudinary.api_key}")
	private String apiKey;
	@Value("${cloudinary.api_secret}")
	private String apiSecret;
	@Value("${CLOUDINARY_URL}")
	private String cloudinaryURL;

	@Bean
	public void registerCloudinary() {
		Cloudinary cloudinary = new Cloudinary(cloudinaryURL);
		Singleton.registerCloudinary(cloudinary);
	}

	@Bean("cloudinaryUploader")
	public ImageUploader cloudinaryImageUploader() {
		return new CloudinaryImageUploader();
	}
}