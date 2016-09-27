package org.mfr.xprt.rest.controller;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.PersistenceException;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.exception.ConstraintViolationException;
import org.mfr.data.PersistentObject;
import org.mfr.data.Useracc;
import org.mfr.rest.serializer.MfrObjectMapper;
import org.mfr.rest.util.DateTimeFormatterHelper;
import org.mfr.rest.util.HttpHelper;
import org.mfr.rest.util.MailHelper;
import org.mfr.rest.util.ThreadSetupFilter;
import org.mfr.rest.util.VelocityHelper;
import org.mfr.xprt.data.XEventDao;
import org.mfr.xprt.data.XLangtextDao;
import org.mfr.xprt.rest.exception.AccessDenied;
import org.mfr.xprt.rest.exception.AuthException;
import org.mfr.xprt.rest.exception.MissingValuesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public abstract class AbstractBaseController<T> {
	ExecutorService executorService = Executors.newFixedThreadPool(4);
	
	protected static final String YOU_ARE_NOT_OWNER_OF_OBJECT = "you are not owner of object";

	protected Calendar calendar = Calendar.getInstance();
	protected HttpHelper helper = new HttpHelper();
	protected static ScriptEngineManager engineManager = new ScriptEngineManager();

	protected static Map<Class, String> classJsonRemap = new HashMap<Class, String>();
	static {
		// classJsonRemap.put(Useracc.class, "user");
	}

	private static PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
	@Autowired
	protected XLangtextDaoHelper langHelper;

	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "not owner of object")
	@ExceptionHandler({ AccessDenied.class})
	public void accessDenied(HttpServletRequest req, Exception exception) {
		log.warn("AccessDenied exception:"+exception.getMessage());
	}
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "unauthorized")
	@ExceptionHandler({ AuthException.class})
	public void unauthorized(HttpServletRequest req, Exception exception) {
		log.warn("AccessDenied exception:"+exception.getMessage());
	}
	
	
	
	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler({ PersistenceException.class})
	@ResponseBody
	public Object exceptionHandler(HttpServletRequest req, PersistenceException exception) {
		log.error("persistence exception:"+exception.getMessage());		
		Map m=initResultMap("error");
		if(exception.getCause() instanceof ConstraintViolationException){
			m.put("error", errorMap("/registration/error_unique_email", exception.getMessage()));
			try {
				return this.objectMapper.writeValueAsString(m);
			} catch (JsonProcessingException e) {
				log.error("cover to JSON error",e);
			}
		}
		return m.put("error", exception.getMessage());
	}
	
	@ExceptionHandler({ Exception.class})
	public void exceptionHandler(HttpServletRequest req, Exception exception) {
		log.error("exception:"+exception.getMessage(),exception);
	}
	private static final ThreadLocal<SimpleDateFormat> dateFormatHolder = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			System.out.println("Creating SimpleDateFormat for Thread : "
					+ Thread.currentThread().getName());
			return new SimpleDateFormat("yyyyMMdd");
		}
	};

	

	protected Map fillClientRequestData(HttpServletRequest request) {
		Map result = new HashMap();
		result.put("clientIP", request.getRemoteAddr());
		return result;
	}

	protected void setSerializeJustId(boolean v) {
		ThreadSetupFilter.getThreadCache().put(ThreadSetupFilter.SERIALIZE_JUST_ID, v);
	}

	protected void setSerializeHidden(boolean v) {
		ThreadSetupFilter.getThreadCache().put(ThreadSetupFilter.SERIALIZE_HIDDEN, v);
	}

	protected void addSerializeSkipClass(Class c) {
		log.debug("addSerializeSkipClass:"+c);
		getSerializeSkipClass().add(c);
	}

	protected List getSerializeSkipClass() {
		if(log.isTraceEnabled()){
			log.trace("thread local: "+ThreadSetupFilter.getThreadCache());
		}
		return (List) ThreadSetupFilter.getThreadCache().get(ThreadSetupFilter.SERIALIZE_SKIP_CLASS);
	}

	public static boolean isSeralizeJustId() {
		boolean b = (boolean) ThreadSetupFilter.getThreadCache().get(ThreadSetupFilter.SERIALIZE_JUST_ID);
		return b;
	}

	public static boolean isSerializeHidden() {
		boolean b = (boolean) ThreadSetupFilter.getThreadCache().get(ThreadSetupFilter.SERIALIZE_HIDDEN);
		return b;
	}

	public static DateFormat getDateFormatter() {
		return dateFormatHolder.get();
	}

	private Useracc getUser(Authentication auth) {
		return ((Useracc) auth.getPrincipal());
	}

	@Autowired
	protected XEventDao generalDao;
	@Autowired
	protected MfrObjectMapper objectMapper;
	@Autowired
	protected XLangtextDao commonDao;

	protected static List<String> putIgnoreProperties = new ArrayList<String>();
	static {
		putIgnoreProperties.add("id");
	}

	protected Object findObject(String NAME, HttpServletRequest request) {
		if (request.getParameter("id") != null) {
			return wrapPayload(NAME, commonDao.findByIdG(
					Integer.parseInt(request.getParameter("id")), getClazz()));
		}
		return wrapPayload(NAME, commonDao.findAllG(getClazz()));
	}

	public Object updateObject(String NAME, Integer objId,
			String[] ignoreProperties, JsonNode eventJson) {
		Object source = this.extractFromJson(eventJson.get(NAME), getClazz());
		Object target = commonDao.findByIdG(objId, getClazz());
		copyBeans(source, target, ignoreProperties);
		commonDao.mergeG(target);
		return wrapPayload(NAME, target);
	}

	public Object createObject(String NAME, JsonNode event) {
		Object object = this.extractFromJson(event.get(NAME), getClazz());
		generalDao.persistG(object);
		return wrapPayload(NAME, object);
	}

	public Object deleteObject(String NAME, Integer objId) {
		Object target = commonDao.findByIdG(objId, getClazz());
		commonDao.removeG(target);
		return wrapPayload(NAME, target);
	}

	private static Logger log = LoggerFactory
			.getLogger(AbstractBaseController.class);

	protected Map<String, Object> wrapPayload(String name, Object o) {
		Map result = new HashMap();
		if (o instanceof List) {
			for (Object item : (List) o) {
				mergeObjectToMap(result, item);
			}
			if (result.size() == 0) {
				result.put(name, new ArrayList<Object>());
			}
		} else if (o instanceof Set) {
			for (Object item : (Set) o) {
				mergeObjectToMap(result, item);
			}
			if (result.size() == 0) {
				result.put(name, new HashSet<Object>());
			}
		} else {
			mergeObjectToMap(result, o);
		}
		return result;
	}

	protected void mergeObjectToMap(Map resultMap, Object o) {
		mergeObjectToMap(resultMap, o, false, null);
	}

	protected Map initResultMap(String name) {
		Map result = new HashMap();
		result.put(name, new LinkedHashSet());
		return result;
	}
	protected Map errorMap(String code,String text){
		Map result = new HashMap();
		result.put("code", code);
		result.put("text", text);
		return result;
	}

	protected void mergeObjectToMap(Map resultMap, Object o, boolean skipProp) {
		if (!skipProp)
			throw new UnsupportedOperationException(
					"should not call this method if skipProp is false");
		mergeObjectToMap(resultMap, o, skipProp, null);
	}

	protected void mergeObjectToMap(Map resultMap, Object o, boolean skipProp,
			List<String> skipProperties) {
		if (o == null)
			return;
		if (getSerializeSkipClass().contains(o.getClass())) {
			log.debug("serializable skip class");
			return;
		}
		boolean collection = false;
		String className = o.getClass().getSimpleName();
		// int proxyOffset=className.indexOf("_$$");
		// if(proxyOffset>-1){
		// className=className.substring(0,proxyOffset);
		// }

		if (o instanceof List) {
			List l = (List) o;
			if (l.size() == 0)
				return;
			collection = true;
			className = l.get(0).getClass().getSimpleName();
		} else if (classJsonRemap.containsKey(o.getClass())) {
			className = classJsonRemap.get(o.getClass());
		}
		if (className.startsWith("X")) {
			className = className.substring(1);
		}
		String propertyName = className.substring(0, 3).toLowerCase()
				+ className.substring(3);

		Set l = new LinkedHashSet();
		if (resultMap.containsKey(propertyName)) {
			l = ((Set) resultMap.get(propertyName));
		} else {
			resultMap.put(propertyName, l);
		}
		if (collection) {
			l.addAll((List) o);
		} else {
			l.add(o);
		}
		if (!skipProp) {
			if (o instanceof List) {
				for (Object object : l) {
					inspectObject(resultMap, object, skipProperties);
				}
			} else {
				inspectObject(resultMap, o, skipProperties);
			}

		}
	}
	
	protected String toJson(Object o){
		ObjectMapper om=new ObjectMapper();
		try {
			return om.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			log.error("error during serialze",e);
			return e.getMessage();
		}
	}

	private void inspectObject(Map resultMap, Object o,
			List<String> skipProperties) {
		PropertyDescriptor[] properties = PropertyUtils
				.getPropertyDescriptors(o.getClass());
		for (int i = 0; i < properties.length; i++) {
			if (skipProperties != null
					&& skipProperties.contains(properties[i].getName())) {
				continue;
			}
			Method getMethod = properties[i].getReadMethod();
			PersistentObject po = getMethod
					.getAnnotation(PersistentObject.class);
			Class type = null;
			if (po != null) {
				type = po.clazz();
			} else {
				type = properties[i].getPropertyType();
			}
			if (MfrObjectMapper.hanledObjects.indexOf(type) > -1) {
				try {
					if (po != null) {
						Integer[] ids = (Integer[]) propertyUtils.getProperty(
								o, properties[i].getName());
						if (ids != null && ids.length > 0) {
							type = po.clazz();
							List objects = commonDao.findAll(type,
									(Integer[]) propertyUtils.getProperty(o,
											properties[i].getName()));
							mergeObjectToMap(resultMap, objects);
						}
					} else {
						mergeObjectToMap(
								resultMap,
								propertyUtils.getProperty(o,
										properties[i].getName()));
					}
				} catch (Exception e) {
					log.error("error during serialization", e);
					e.printStackTrace();
				}
			}
		}
	}

	protected T extractFromJson(JsonNode node, Class<T> clazz) {
		return objectMapper.convertValue(node, clazz);
	}

	protected static String[] getIgnorePropertiesForPut(String... array) {
		if (array != null && array.length > 0) {
			for (int i = 0; i < array.length; i++) {
				putIgnoreProperties.add(array[i]);
			}
		}
		return putIgnoreProperties.toArray(new String[putIgnoreProperties
				.size()]);
	}

	protected static List<String> getIgnorePropertiesForSerializer(
			String... array) {
		List l = new ArrayList();
		if (array != null && array.length > 0) {
			for (int i = 0; i < array.length; i++) {
				l.add(array[i]);
			}
		}
		return l;
	}

	protected Useracc checkForAuth(Authentication auth) {
		boolean authenticated = false;
		if (auth != null) {
			Useracc user = ((Useracc) auth.getPrincipal());
			if (user != null && user.getId() != null) {
				setSerializeHidden(true);
				return user;
			}
		}
		throw new AuthException();
	}

	protected boolean hasEmpty(Object... args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				throw new MissingValuesException(i
						+ "th value is null or empty");
			}
		}
		return false;
	}

	protected boolean hasEmpty(String... args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null || args[i].length() == 0
					|| args[i].equals("null")) {
				throw new MissingValuesException(i
						+ "th value is null or empty");
			}
		}
		return false;
	}

	protected Useracc checkOwner(Authentication auth, Useracc compareTo) {
		Useracc user = checkForAuth(auth);
		if (compareTo == null || !compareTo.getId().equals(user.getId())) {
			throw new AccessDenied();
		}
		return user;
	}

	protected String getText(String type, String code, String lang) {
		return langHelper.getText(type, code, lang);
	}

	protected static String parseDomain() {
		if (System.getProperty("httpdomain") != null) {
			return System.getProperty("httpdomain");
		}
		return "unkown";
	}

	protected static String parseDomain(HttpServletRequest request) {
		if (System.getProperty("httpdomain") != null) {
			return System.getProperty("httpdomain");
		}
		int start = getNthPosition(request.getRequestURL(), "/", 3);
		if (start > -1) {
			return request.getRequestURL().substring(0, start);
		}
		return null;
	}

	protected static int getNthPosition(StringBuffer sb, String str, int c) {
		int start = 0;
		for (int i = 1; i <= c; i++) {
			start = sb.indexOf(str, start + 1);
			if (start == -1)
				break;
		}
		return start;
	}

	public void sendEmailAsync(final Locale locale, final String templateCode, final String adressee,final
			Map additionArgs) {
		executorService.execute(new Runnable(){
			@Override
			public void run() {
				sendEmail(locale, templateCode, adressee,additionArgs);
			}
		});
	}
	public void sendEmail(final Locale locale, final String templateCode, final String adressee,final
			Map additionArgs) {
		List userlist = new ArrayList();
		userlist.add(adressee);
		final Map arguments = buildBaseEmailArguments(locale);
		arguments.putAll(additionArgs);
		try {
			
			
			// merge subject
			String subjectVelo=getText("email", templateCode + "_subject", locale
					.getLanguage().toLowerCase());
			String subjectText = VelocityHelper.mergeString(subjectVelo, arguments);
			
			MailHelper.sendHtmlMail(
					MailHelper.DEFAULT_SENDER,
					userlist,
					subjectText,
					getText("email", templateCode, locale.getLanguage()
							.toLowerCase()), arguments);
		} catch (Exception e) {
			log.error("send mail error:", e);
			throw (RuntimeException) e;
		}
	}

	protected static Locale getLocale(HttpServletRequest request) {
		String language = request.getParameter("l");
		if (language == null) {
			language = "hu";
		}
		Locale l = Locale.forLanguageTag(language);
		request.setAttribute("locale", l);
		return l;
	}

	public Map buildBaseEmailArguments(Locale locale) {
		final Map arguments = new HashMap();
		DateTimeFormatterHelper dtf = new DateTimeFormatterHelper(locale);
		arguments.put("dtf", dtf);
		String domain = parseDomain();
		arguments.put("domain", domain);
		arguments.put("sendingTime", new Date());
		arguments.put("l", locale.getLanguage().toLowerCase());
		arguments.put("helper", helper);

		VelocityHelper th = new VelocityHelper(this.langHelper, locale,
				arguments);
		arguments.put("th", th);

		return arguments;
	}
	public void copyBeans(Object source,Object target, String [] ignoreProperties){
		Field [] fields=source.getClass().getDeclaredFields();
		Arrays.sort(ignoreProperties);
		for (int i = 0; i < fields.length; i++) {
			if(Arrays.binarySearch(ignoreProperties, fields[i].getName())<=-1){
				try {
					fields[i].setAccessible(true);
					org.apache.commons.beanutils.BeanUtils.copyProperty(target, fields[i].getName(), fields[i].get(source));
				} catch (Exception e) {
					log.error("error copy field ["+fields[i].getName()+"] ");
				}
			}
			
		}
		
	}
	protected abstract Class getClazz();
}

