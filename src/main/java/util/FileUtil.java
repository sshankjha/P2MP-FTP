package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Base64;

import org.apache.log4j.Logger;

public class FileUtil {

	static Logger logger = Logger.getLogger(FileUtil.class);

	public static void saveToFile(String fromServer, String fileToWrite) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileToWrite))) {
			bw.write(new String(Base64.getDecoder().decode(fromServer)));
		} catch (Exception e) {
			logger.error(e);
		}
	}

	
}
