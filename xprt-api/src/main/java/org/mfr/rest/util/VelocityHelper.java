package org.mfr.rest.util;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.mfr.xprt.rest.controller.XLangtextDaoHelper;

public class VelocityHelper {
	private static String loggerName=VelocityHelper.class.getSimpleName();
	private static final Logger log = Logger.getLogger(loggerName);
	
	static{
		Velocity.setProperty(Velocity.RESOURCE_LOADER, "file");
		Velocity.setProperty("file.resource.loader.description","Velocity FileResource loader");
		Velocity.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		String servletPath=System.getProperty("servlet.path");
		Velocity.setProperty("file.resource.loader.path",servletPath+"/WEB-INF/templates/vm/");
		Velocity.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
			      "org.apache.velocity.runtime.log.Log4JLogChute" );
		//Velocity.setProperty("resourceLoaderPath", value);
		
		Velocity.setProperty("runtime.log.logsystem.log4j.logger",loggerName);
		Velocity.init();
	}
	
	Locale locale;
	Map arguments;
	XLangtextDaoHelper langHelper;
	String getText( String type,String code,String lang){
		return langHelper.getText(type, code, lang);
	}
	
	public VelocityHelper(XLangtextDaoHelper langHelper,Locale locale, Map arguments) {
		super();
		this.locale = locale;
		this.arguments = arguments;
		this.langHelper = langHelper;
	}
	public String parseEmailTemplate(String code){
		String template=getText("email",code, locale.getLanguage().toLowerCase());
		String mailMessageText = VelocityHelper.mergeString(template, arguments);
		
		return  mailMessageText;
	}
	
	
	static String mergeFile(String templateFile, Map arguments){
		return mergeTemplate(templateFile, arguments);

	}
	public static String mergeString(String tempateString,Map arguments){
		try {			
			VelocityContext context = new VelocityContext();
			Set argumentKeys = null;
			if(arguments!=null)
			{
				argumentKeys = arguments.keySet();
				for (Iterator iterator = argumentKeys.iterator(); iterator.hasNext();) {
					String argumentKey = (String) iterator.next();
					context.put(argumentKey, arguments.get(argumentKey));						
				}			
			}
			java.io.StringWriter sw = new java.io.StringWriter();
			Velocity.evaluate(context, sw, "string", new StringReader(tempateString));
			return sw.toString();
		} catch (Exception e) {
			log.error("",e);
		}				
		return null;
	}
	private static String mergeTemplate(String templateString, Map arguments) {
		
		java.io.StringWriter sw = null;
		try {			
			Template message = Velocity.getTemplate(templateString,"utf-8");
			if (message == null) {
				log.error("template is null!");
			}
			VelocityContext context = new VelocityContext();
			Set argumentKeys = null;
			
			if(arguments!=null)
			{
				argumentKeys = arguments.keySet();
				for (Iterator iterator = argumentKeys.iterator(); iterator.hasNext();) {
					String argumentKey = (String) iterator.next();
					context.put(argumentKey, arguments.get(argumentKey));						
				}			
			}
			
			sw = new java.io.StringWriter();
			message.merge(context, sw);
			
		} catch (Exception e) {
			log.error("",e);
		}				
		
		return sw.toString();
	}
}
