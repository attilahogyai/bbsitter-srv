package org.mfr.rest.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContextWrapper {
	static Log log = LogFactory.getLog(ContextWrapper.class);
	private HttpServletRequest request;
	private Map<String, Object> localAttribs=new HashMap<String, Object>();
	
	public ContextWrapper(HttpServletRequest request){
		this.request=request;
	}
	public void setLocalAttribute(String key,Object object){
		localAttribs.put(key, object);
	}
	public Object getAttribute(String key){
		Object parameter=request.getParameter(key);
		if(parameter==null){
			parameter=request.getAttribute(key);
			if(parameter==null){
				parameter=request.getSession().getAttribute(key);
				if(parameter==null){
					parameter=localAttribs.get(key);
				}
			}
		}
		return HtmlEscaper.escapeHtml((String)parameter);
	}
	public Object getNativeAttribute(String key){
		Object parameter=request.getParameter(key);
		if(parameter==null){
			parameter=request.getAttribute(key);
			if(parameter==null){
				parameter=request.getSession().getAttribute(key);
				if(parameter==null){
					parameter=localAttribs.get(key);
				}
			}
		}
		return parameter;
	}	
	public String [] getParameterValues(String key){
		return request.getParameterValues(key);
	}
	
	public void setSessionObject(String key, Object o)
	{
		request.getSession().setAttribute(key, o);
	}
	
	public Object getSessionObject(String key)
	{
		return request.getSession().getAttribute(key);
	}
	
	public void removeSessionAttribute(String key)
	{
		request.getSession().removeAttribute(key);
	}
	
	public Map<String, Object> getAllAttributes(){
		Map<String, Object> variables=(Map<String, Object>)request.getAttribute("viewVariables");
		if(variables==null){
			Enumeration<String> sessionNames=request.getSession().getAttributeNames();
			while (sessionNames.hasMoreElements()) {
				String key = (String) sessionNames.nextElement();
				variables.put(key, HtmlEscaper.escapeHtml((String) request.getSession().getAttribute(key)));	
			}
			Enumeration<String> attrNames=request.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String key = (String) attrNames.nextElement();
				variables.put(key, HtmlEscaper.escapeHtml((String)request.getSession().getAttribute(key)));	
			}
			
			Map m = request.getParameterMap();
			Set mapKs = m.keySet();
			
			for (Object key : mapKs) {
				variables.put((String) key, HtmlEscaper.escapeHtml((String) m.get(key)));
			}
		}
		variables.putAll(localAttribs);
		return variables;
	}
	
	public void putLocalAttributes(Map <String,Object> attribs) {
		attribs.putAll(attribs);
	}
	
	public String getConfigPath(){
		return request.getRealPath("WEB-INF/");
	}
	
	public HttpServletRequest getRequest() {
		   return request;
	}
	
}
