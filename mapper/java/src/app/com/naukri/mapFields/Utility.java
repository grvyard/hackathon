package app.com.naukri.mapFields;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Utility {
	
	JSONObject readJsonFile(String pathOfFile) throws ParseException {
		JSONObject json = null;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(pathOfFile));
			json = (JSONObject) obj;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	String readEntireFile(String pathOfFile) {
		BufferedReader br = null;
		String file = "";
		
		try {
			br = new BufferedReader(new FileReader(pathOfFile));
			String line;
			while ((line = br.readLine()) != null) {
				file += line + "\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}
	
	ArrayList<ArrayList<String>> splitContentOnTab(String fileContent, String separator) {
		String lines[] = fileContent.split("\n");
		ArrayList<ArrayList<String>> tabSplittedContent = new ArrayList<ArrayList<String>>();
		
		for (String line: lines) {
			if (line.trim().length() > 0) {
				ArrayList<String> inner = new ArrayList<String>();
				String splittedData[] = line.split(separator);
				
				for(String x: splittedData) {
					inner.add(x);
				}
				
				tabSplittedContent.add(inner);
			}
		}
		
		return tabSplittedContent;
	}
}