package org.mfr.rest.image;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.im4java.core.ConvertCmd;
import org.im4java.core.ETOperation;
import org.im4java.core.ExiftoolCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListOutputConsumer;
import org.mfr.data.Constants;
import org.mfr.data.ExifData;
import org.mfr.rest.util.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ImageTools {
	
	private static final Logger logger=LoggerFactory.getLogger(ImageTools.class);
	private static String jpegoptim = "jpegoptim --strip-all "; 
	static String PNG = ".PNG";
	
	public static Map<String,ExifDataEnum> tagsMap=new HashMap<String,ExifDataEnum>(){
		{
		put("File Name",ExifDataEnum.Filename);
		put("Image Width",ExifDataEnum.ImageWidth);
		put("Image Height",ExifDataEnum.ImageHeight);
		put("F Number",ExifDataEnum.FNumber);
		put("Exposure Time",ExifDataEnum.ExposureTime);
		put("ISO",ExifDataEnum.Iso);
		put("Focal Length",ExifDataEnum.FocalLength);
		put("Camera Model Name",ExifDataEnum.Model);
		put("Exposure Program",ExifDataEnum.ExposureProgram);
		put("Date/Time Original",ExifDataEnum.DateTimeOriginal);
		put("Exposure Compensation",ExifDataEnum.ExposureCompensation);
		
		put("Metering Mode",ExifDataEnum.MeteringMode);
		put("White Balance",ExifDataEnum.WhiteBalance);
		put("Lens Info",ExifDataEnum.LensInfo);
		put("Lens Model",ExifDataEnum.LensModel);
		put("Lens Make",ExifDataEnum.LensMake);
		put("X Resolution",ExifDataEnum.XResolution);
		put("Y Resolution",ExifDataEnum.YResolution);
		put("Resolution Unit",ExifDataEnum.ResolutionUnit);
		put("Orientation",ExifDataEnum.Orientation);
		}
	};


	public static ExifData exifTool(String imagepath) throws IOException, InterruptedException, IM4JavaException{
		ETOperation op = new ETOperation();
	    op.getTags(ExifDataEnum.getKeyArrays());
	    op.addImage();
	    // setup command and execute it (capture output)
	    ArrayListOutputConsumer output = new ArrayListOutputConsumer();
	    ExiftoolCmd et = new ExiftoolCmd();
	    et.setSearchPath(Constants.SEARCHPATH);
	    et.setOutputConsumer(output);
	    et.run(op,imagepath);
	    // dump output
	    ArrayList<String> cmdOutput = output.getOutput();
	    ExifData exifDataObj=new ExifData(); 
	    for (String line:cmdOutput) {
	    	logger.debug("command line["+line+"]");
	    	int start=line.indexOf(':');
	    	if(start!=-1){
	    	  String [] cols={line.substring(0,start),line.substring(start+1,line.length()).trim()};
	    	  String keyName=tagsMap.get(cols[0].trim()).toString();
	    	  if(ExifDataEnum.DateTimeOriginal.toString().equals(keyName)){
	    		  exifDataObj.setDateTimeOriginal(cols[1].trim());
	    	  }else if(ExifDataEnum.ExposureCompensation.toString().equals(keyName)){
	    		  exifDataObj.setExposureCompenstation(cols[1].trim());
	    	  }else if(ExifDataEnum.ExposureProgram.toString().equals(keyName)){
	    		  exifDataObj.setExposureProgram(cols[1].trim());
	    	  }else if(ExifDataEnum.ExposureTime.toString().equals(keyName)){
	    			exifDataObj.setExposureTime(cols[1].trim());
	    	  }else if(ExifDataEnum.FocalLength.toString().equals(keyName)){
	    			exifDataObj.setFocalLength(cols[1].trim());
	    	  }else if(ExifDataEnum.FNumber.toString().equals(keyName)){
	    		  try{
	    			exifDataObj.setFnumber(cols[1].trim()!=null?Double.parseDouble(cols[1].trim()):null);
	    		  }catch(Exception e){}
	    	  }else if(ExifDataEnum.ImageHeight.toString().equals(keyName)){
	    		  try{
	    			exifDataObj.setImageHeight(cols[1].trim()!=null?Integer.parseInt(cols[1].trim()):null);
	    		  }catch(Exception e){}
	    	  }else if(ExifDataEnum.ImageWidth.toString().equals(keyName)){
	    		  try{
	    			exifDataObj.setImageWidth(cols[1].trim()!=null?Integer.parseInt(cols[1].trim()):null);
	    		  }catch(Exception e){}
	    	  }else if(ExifDataEnum.Iso.toString().equals(keyName)){
	    		  try{
	    			exifDataObj.setIso(cols[1]!=null?Integer.parseInt(cols[1].trim()):null);
	    		  }catch(Exception e){}
	    	  }else if(ExifDataEnum.LensInfo.toString().equals(keyName)){
	    			exifDataObj.setLensInfo(cols[1]);
	    	  }else if(ExifDataEnum.LensMake.toString().equals(keyName)){
	    			exifDataObj.setLensMake(cols[1]);
	    	  }else if(ExifDataEnum.LensModel.toString().equals(keyName)){
	    			exifDataObj.setLensModel(cols[1]);
	    	  }else if(ExifDataEnum.Model.toString().equals(keyName)){
	    		  exifDataObj.setModel(cols[1]);
	    	  }else if(ExifDataEnum.MeteringMode.toString().equals(keyName)){
	    		  exifDataObj.setMeteringMode(cols[1]);
	    	  }else if(ExifDataEnum.WhiteBalance.toString().equals(keyName)){
	    		  exifDataObj.setWhiteBalance(cols[1]);
	    	  }else if(ExifDataEnum.XResolution.toString().equals(keyName)){
	    		  try{
	    			  exifDataObj.setXresolution(Integer.parseInt(cols[1].trim()));
	    		  }catch(Exception e){}
	    	  }else if(ExifDataEnum.YResolution.toString().equals(keyName)){
	    		  try{
	    			exifDataObj.setYresolution(Integer.parseInt(cols[1].trim()));
	    		  }catch(Exception e){}
	    	  }else if(ExifDataEnum.ResolutionUnit.toString().equals(keyName)){
	    		  exifDataObj.setResolutionUnit(cols[1]);
	    	  }else if(ExifDataEnum.Orientation.toString().equals(keyName)){
	    		  try{
	    			  exifDataObj.setOrientationDesc(cols[1]);
	    		  }catch (Exception e){}
	    	  }
	      }
	    } 
	    return exifDataObj;
	}
	
	public static void resizeImages(ImageConfig config,File destDir,boolean removeProfile, String... pImageNames) throws IOException,
			InterruptedException, IM4JavaException {
		// create command
		ConvertCmd cmd = createBaseConvertCmd();
		// create the operation, add images and operators/options
		IMOperation op = createBaseImageOperation(config);
		op.addImage();
		
		
		resizeConfig(config, op);
		
		if(!removeProfile){
			op.p_profile("\"*\"");
		}
		
//		op.define("jpeg:optimize-coding=true");
		op.addImage();
		
		for (String srcImage : pImageNames) {
			File checkDir=destDir;
			if(!checkDir.isDirectory()){
				checkDir=destDir.getParentFile();
			}
			if (!checkDir.exists() && !checkDir.mkdirs()) {
				logger.error("ERROR CREATE DIRECTORY ["+destDir+"]");
				continue;
			}
			String dstImage = destinationImage(destDir, srcImage);
			try{
				logger.debug("resize and optimize: "+srcImage + " -> " + dstImage);				
				cmd.run(op, srcImage, dstImage);
				// optimize
				try{
					Runtime.getRuntime().exec(jpegoptim+" "+dstImage);
				}catch(Exception e){
					logger.warn("JPEG optim error:"+e.getMessage());
				}
			}catch(Exception e){
				List<String> errorTextList=cmd.getErrorText();
				if(errorTextList!=null){
					for (String string : errorTextList) {
						logger.error("resise error ["+string+"]");
					}
				}
				logger.debug("resise image  ["+srcImage+"] exception: ",e);
				
			}
		}
	}

	public static void resizeConfig(ImageConfig config, IMOperation op) {
		op.resize();
		if (config.width == null) {
			//op.resize(config.height);
			op.addRawArgs("x"+config.height+">");
		} else if(config.height == null){
			op.addRawArgs(config.width+">");
		}else{
			//op.resize(config.width, config.height,">");
			//op.resize(config.height);
			//op.addRawArgs("-resize "+config.width+"x"+config.height+">");
			op.addRawArgs(config.width+"x"+config.height+">");
		}
	}
	private static ConvertCmd createBaseConvertCmd() {
		ConvertCmd cmd = new ConvertCmd();
		cmd.setSearchPath(Constants.SEARCHPATH);
		return cmd;
	}
	private static IMOperation createBaseImageOperation(ImageConfig cfg) {
		IMOperation io=new IMOperation();
		io.quality(cfg.quality);
		return io;
	}
	private static String destinationImage(File destDir, String srcImage) {
		if(!HttpHelper.isFileImageExt(destDir.getAbsolutePath())){
			final int lastSlash = srcImage.lastIndexOf(File.separatorChar);
			String dstImage = destDir + (lastSlash>-1?srcImage.substring(lastSlash, srcImage.length()):srcImage);
			return dstImage;
		}else{
			return destDir.getAbsolutePath();
		}
	}	
	public static String convertFileExtension(String filePath,String targetExt){
		int s=filePath.lastIndexOf('.');
		String resultFile=filePath;
		if(s>-1){
			resultFile=filePath.substring(0,s)+targetExt;
		}
		return resultFile;
	}

	public static File[]  normalizeImage(ImageConfig dstCfg,ImageConfig src,String... relativeImageNames) throws IOException{
		ConvertCmd cmd = createBaseConvertCmd();
		IMOperation io=new IMOperation();
		io.normalize();
		io.enhance();
		io.addImage();
		io.addImage();
		File[] result=new File[relativeImageNames.length];
		for (int i=0;i<relativeImageNames.length;i++) {
			File source=new File(src.getPath(relativeImageNames[i]));
			File dstImage=new File(dstCfg.getPath(relativeImageNames[i]));
			if(!checkDestination(dstImage)){
				continue;
			}
			try{
				logger.debug("normalize: "+source + " -> " + dstImage);				
				cmd.run(io, source.getAbsolutePath(), dstImage.getAbsolutePath());
				result[i]=dstImage;
			}catch(Exception e){
				List<String> errorTextList=cmd.getErrorText();
				if(errorTextList!=null){
					for (String string : errorTextList) {
						logger.error("normalize error ["+string+"]");
					}
				}
				logger.debug("normalize ["+dstImage.getAbsolutePath()+"] exception: ",e);
			}
		}
		return result;
	}
	
	public static boolean checkDestination(File dstImage){
		if (!dstImage.getParentFile().exists() && !dstImage.getParentFile().mkdirs()) {
			logger.error("ERROR CREATE DIRECTORY ["+dstImage.getParentFile()+"]");
			return false;
		}
		return true;
	}
	
	
	public static void rotateSingleImage(ImageConfig cfg,double degree, String absolute) throws IOException{		
		ConvertCmd cmd = createBaseConvertCmd();
		IMOperation io=createBaseImageOperation(cfg);
		io.rotate(degree);
		io.addImage();
		io.addImage();
		File dstImage=new File(absolute);
		if(!dstImage.exists()){
			logger.error("file not found:"+absolute);
			return;
		}
		try{
			logger.debug("rotate: "+absolute + " -> " + dstImage);				
			cmd.run(io, dstImage.getAbsolutePath(), dstImage.getAbsolutePath());
		}catch(Exception e){
			List<String> errorTextList=cmd.getErrorText();
			if(errorTextList!=null){
				for (String string : errorTextList) {
					logger.error("rotate error ["+string+"]");
				}
			}
			
			logger.debug("rotate ["+dstImage.getAbsolutePath()+"] exception: ",e);
		}
	}

	public static String calcNewFileName(String targetDirectory, String filenameTmp) {
		int subindx=1;
		String dest=targetDirectory+filenameTmp;
		String filename=filenameTmp;
		while(new File(dest).exists()){
			filename=filenameTmp;
			int dotIdx=filename.indexOf(".");
			if(dotIdx>-1){
				filename=filename.substring(0,dotIdx)+"_"+subindx+++filename.substring(dotIdx,filename.length());
			}
			dest=targetDirectory+filename;
		}
		return filename;
	}
}

