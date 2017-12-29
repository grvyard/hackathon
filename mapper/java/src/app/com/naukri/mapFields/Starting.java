package app.com.naukri.mapFields;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import app.com.naukri.dbQuery.DBQueries;

class readConfigFile {
	JSONObject configJSON = null;
	String mappedFileContent = "";
	String projectDirectoryPath = System.getProperty("user.dir") + "/";
	
	readConfigFile(String configPath, String readFromDB, String compID) throws SQLException {
		Utility utilObj = new Utility();
		String configFileContent = utilObj.readEntireFile(configPath);
		ArrayList<ArrayList<String>> tabSeparatedData = utilObj.splitContentOnTab(configFileContent, "\t");
		DBQueries dbObj = new DBQueries();
		Connection conn = null;
		
		for (ArrayList<String> data: tabSeparatedData) {
			String filePath = data.get(1);
			filePath = projectDirectoryPath + filePath;
			if (readFromDB.equals("y") && data.get(0).equals("dbConnection")) {
				conn = dbObj.intitalizeConnectionObject(data.subList(1, data.size()));
				configJSON = dbObj.getConfigFromDB(conn, compID);
			} else if (data.get(0).equals("inputFile") && !readFromDB.equals("y")) {
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
	protected static String readFromDB = "y";
	protected static String compID = "";
	protected enum ArgumentType {
	    PARAM_CONF, PARAM_FILE, PARAM_COMPID, DEFAULT 
    }
	
	protected static boolean parseCommandLine(String[] args) {
	    ArgumentType nextArgType = ArgumentType.DEFAULT;
	    
	    for (String param: args) {
	      if (param.equals("--config")) {
	        nextArgType = ArgumentType.PARAM_CONF;
	        continue;
	      } else if (param.equals("--file")) {
	        nextArgType = ArgumentType.PARAM_FILE;
	        continue;
		  } else if (param.equals("--compid")) {
			  nextArgType = ArgumentType.PARAM_COMPID;
		      continue;
		  }
	      
	      switch (nextArgType) {
		      case PARAM_CONF: configPath = param;
		        break;
		      case PARAM_FILE: readFromDB = param;
		    	break;
		      case PARAM_COMPID: compID = param;
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
				readConfigFile data = new readConfigFile(configPath, readFromDB, compID);
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
