package org.mfr.rest;

import javax.servlet.MultipartConfigElement;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@org.springframework.context.annotation.Configuration
@ComponentScan
public class Configuration {
	@Bean
	MultipartConfigElement multipartConfigElement() {
		return new MultipartConfigElement("tmpFile");
	}	
}
