package org.mfr.rest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringWebMvcInitializer extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected WebApplicationContext createServletApplicationContext() {
		return new XmlWebApplicationContext();
	}
	@Override
	protected WebApplicationContext createRootApplicationContext() {
		return new XmlWebApplicationContext();
	}
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/*"};
	}


}