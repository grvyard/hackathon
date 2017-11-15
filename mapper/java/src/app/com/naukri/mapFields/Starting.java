package app.com.naukri.mapFields;
import java.util.ArrayList;

import org.json.simple.JSONObject;

class readConfigFile {
	JSONObject configJSON = null;
	String mappedFileContent = "";
	String projectDirectoryPath = System.getProperty("user.dir") + "/";
	
	readConfigFile(String configPath) {
		Utility utilObj = new Utility();
		String configFileContent = utilObj.readEntireFile(configPath);
		ArrayList<ArrayList<String>> tabSeparatedData = utilObj.splitContentOnTab(configFileContent, "\t");
		//projectDirectoryPath = projectDirectoryPath.replaceAll("^(.*/).*", "$1");
		
		for (ArrayList<String> data: tabSeparatedData) {
			String filePath = data.get(1);
			filePath = projectDirectoryPath + filePath;
			if (data.get(0).equals("inputFile")) {
				try {
					configJSON = utilObj.readJsonFile(filePath);
				} catch(Exception e) {
					
				}
			} else if (data.get(0).equals("mappedDataFile")) {
				mappedFileContent = utilObj.readEntireFile(filePath);
			}
		}
	}
}

public class Starting {
	
	protected static String configPath;
	protected enum ArgumentType {
	    PARAM_CONF, DEFAULT 
    }
	
	protected static boolean parseCommandLine(String[] args) {
	    ArgumentType nextArgType = ArgumentType.DEFAULT;
	    
	    for (String param : args) {
	      if (param.equals("--config")) {
	        nextArgType = ArgumentType.PARAM_CONF;
	        continue;
	      }
	      
	      switch (nextArgType) {
		      case PARAM_CONF: configPath = param;
		        break;
		      default:
		        break;
	      }
	    }
	    
	    if (configPath == null) {
	      return false;
	    }
	    return true;
	 }
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			if (!parseCommandLine(args)) {
				System.out.println("Usage: crawler --config <config-file-path>");
				System.exit(0);
			} else {
				readConfigFile data = new readConfigFile(configPath);
				JSONObject configJSON = data.configJSON;
				String mappedFileContent = data.mappedFileContent;
				DataMapper dmpObj = new DataMapper();
				dmpObj.readData(configJSON, mappedFileContent);
			}
		} catch (Exception e) {
	      e.printStackTrace();
	      System.exit(1);
	    }
	}
}
