import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Test {
	private static String exp = "^.*?<img.*?src=\"(.*?)\"";

	public static void main(String[] args) {
		Locale l=Locale.forLanguageTag("en");
		DateTimeFormatter dtf=DateTimeFormat.shortTime().withLocale(l);
		DateTime dt=new DateTime();
		System.out.println(dt.toString(dtf));
		
		
	}

	private static void downloadImages(Map<String, String> fileMap) {
		for (Map.Entry<String, String> entry : fileMap.entrySet()) {
			URL url;
			try {
				url = new URL(entry.getKey());
				InputStream in = new BufferedInputStream(url.openStream());
				FileOutputStream out = new FileOutputStream(new File(entry.getValue()));
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
