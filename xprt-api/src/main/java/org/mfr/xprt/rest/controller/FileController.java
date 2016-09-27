package org.mfr.xprt.rest.controller;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.mfr.data.XLangtext;
import org.mfr.xprt.rest.exception.AccessDenied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
@Controller
public class FileController extends AbstractBaseController<XLangtext>{
	private static Logger log = LoggerFactory.getLogger(FileController.class);
	private static String rootPath=System.getProperty("root.path");
	static{
		if(rootPath==null){
			rootPath=System.getProperty("servlet.path");
		}
	}
	@RequestMapping(value="/file",method = RequestMethod.GET)
	@ResponseBody
	public Object getFile(Authentication authentication,
			HttpServletRequest request){
		checkForAuth(authentication);
		String file=request.getParameter("file");
		if(file.indexOf("..")>-1){
			throw new AccessDenied();
		}
		Path sourceP=Paths.get(rootPath+file);
		try {
			if(sourceP.toFile().isFile()){
				Reader fis=new InputStreamReader(new FileInputStream(sourceP.toFile()),"UTF-8");
				char []buffer=new char[1000];
				StringBuffer sb=new StringBuffer();
				int count=0;
				while((count=fis.read(buffer))>-1){
					sb.append(new String(buffer,0,count));
				}
				return sb.toString();
			}else{
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			log.error("write file",e);
		}
		return "ERROR";
	}	
	
	@RequestMapping(value="/file",method = RequestMethod.PUT)
	@ResponseBody
	public Object updateFile(@RequestBody JsonNode json,Authentication authentication,
			HttpServletRequest request){
		checkForAuth(authentication);
		String file=request.getParameter("file");
		if(file.indexOf("..")>-1){
			throw new AccessDenied();
		}
		Path sourceP=Paths.get(rootPath+file);
		Path targetBackUp=Paths.get(rootPath+file+".back");
		try {
			if(sourceP.toFile().isFile()){
				String content=request.getParameter("content");
				OutputStream os = Files.newOutputStream(sourceP, StandardOpenOption.TRUNCATE_EXISTING);
				os.write(content.getBytes());
				os.flush();
				os.close();
			}else{
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}

		} catch (IOException e) {
			log.error("write file",e);
		}
		return "ok";
	}
	@Override
	protected Class getClazz() {
		// TODO Auto-generated method stub
		return null;
	}

}
