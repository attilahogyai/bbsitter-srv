package org.mfr.rest.util;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public class HtmlEscaper  {
	private static Logger log = Logger.getLogger(HtmlEscaper.class);
	@SuppressWarnings("unchecked")
	public List escapeList(List l){
		if(l!=null){
			escapeBeans(l.toArray(new Object[0]));
		}
		return l;
	}
	public Object escapeObject(Object o){
		decode(o,o.getClass());
		return o;
	}
	public void escapeBeans(Object... msgs){
		for(Object msg:msgs){
			decode(msg,msg.getClass());
		}
	}



	@SuppressWarnings("unchecked")
	private void decode(Object msg, Class clazz) {
		if(!clazz.getSuperclass().getSimpleName().equals("Object")){
			decode(msg,clazz.getSuperclass());
		}
		Field[] fields = clazz.getDeclaredFields();
		for(Field field:fields){
			if(field.getType()==String.class){
				field.setAccessible(true);
				try {
					String str=(String) field.get(msg);
					String str2 =escapeHtml(str);
					if(str !=null && !str.equals("") && !str.equals(str2)){
//						String str2 =escapeHtml(str);
						field.set(msg, str2);
					}
				} catch (Exception e) {
					log.error("error on escaping field:"+field.getName(), e);
				}
				
			}
		}
	}
	public static String escapeHtml(String str) {
		if(str==null || str.equals("")){
			return str;
		}
		if(str.contains("\"")){
			str=str.replaceAll("\"", "&quot;");
		}
		if(str.contains("<")){
			str=str.replaceAll("<", "&lt;");
		}
		if(str.contains(">")){
			str=str.replaceAll(">", "&gt;");
		}
		if(str.contains("'")){
			str=str.replaceAll("'", "&apos;");
		}
		
		return str;
	}
}
