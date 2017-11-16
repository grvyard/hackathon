package app.com.naukri.mapFields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;

@SuppressWarnings({ "serial", "unchecked" })
public class DataMapper {
	
	public void readData(JSONObject configJSON, String mappedFileContent) throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject mappedJson = new JSONObject();
		JSONObject questionaireJson = new JSONObject();
		
		JSONArray usefulJson = (JSONArray)(configJSON.get("fields"));
		ArrayList<JSONObject> results = findDataForFields(usefulJson, mappedFileContent);
		
		mappedJson = results.get(0);
		questionaireJson.put("questionaireData", results.get(1));
		
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mappedJson));
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(questionaireJson));
	}
	
	public ArrayList<JSONObject> findDataForFields(JSONArray usefulJson, String mappedFileContent) {
		ArrayList<String> acceptableAttributes = new ArrayList<String>(Arrays.asList("text", "email"));
		ArrayList<JSONObject> results = new ArrayList<JSONObject>();
		
		JSONObject mappedJson = new JSONObject() {{
			put("email", "*getEmail");
			put("jobUrl", "*getClientJobURL");
			put("resume", "#base64encode|||*getFile");
			put("resumeType", "*getFileExtension");
			put("applyData", new JSONObject() {{
				put("key1", "value1");
			}});
		}};
		
		JSONObject questionaireJson = new JSONObject();
		JSONObject naukriJson = new JSONObject();
				
		Utility utilObj = new Utility();
		FieldAssigner fAsgnObj = new FieldAssigner(); 
		ArrayList<ArrayList<String>> tabSeparatedMappedFileContent = utilObj.splitContentOnTab(mappedFileContent, "\t");
		
		for(int i = 0; i < usefulJson.size(); i++) {
			JSONObject field = (JSONObject)(usefulJson.get(i));
			ArrayList<String> fields = new ArrayList<String>();
			String key = (String) field.keySet().iterator().next();
			String nameField = (String) ((JSONObject)field.get(key)).get("name");
			String fid = (String) ((JSONObject)field.get(key)).get("Id");
			boolean isMandatoryOnSite = (boolean) ((JSONObject)field.get(key)).get("isMandatory");
			boolean flag = false;
			
			String attributeType = (String)(((JSONObject)((JSONObject)field.get(key)).get("attributes")).get("type"));
			
			if(acceptableAttributes.contains(attributeType)) {
				String naukriFieldFunctionName = "";
				boolean isMandatoryOnNaukri = false;
				String naukriFieldName = "";
		
				for(ArrayList<String> data: tabSeparatedMappedFileContent) {
					fields = mapFieldsWithNaukri(data, nameField);
					naukriFieldFunctionName = fields.get(0);
					
					if(naukriFieldFunctionName.trim().length() > 0) {
						isMandatoryOnNaukri = Boolean.parseBoolean(fields.get(1));
						try {
							Method m = FieldAssigner.class.getMethod(naukriFieldFunctionName, String.class, boolean.class, boolean.class);
							naukriFieldName = (String) m.invoke(fAsgnObj, naukriFieldFunctionName, isMandatoryOnNaukri, isMandatoryOnSite);
							flag = true;
						} catch(Exception e) {
							System.out.println(e);
						}
						break;
					}
				}
				
				if(flag) {
					naukriJson.put(fid, naukriFieldName);
				} else {
					questionaireJson.put(fid, "*getQuesFieldValue");
				}
			} else {
				questionaireJson.put(fid, "*getQuesFieldValue");
			}
		}
		
		mappedJson.put("userData", naukriJson);
		
		results.add(mappedJson);
		results.add(questionaireJson);
		return results;
	}
	
	public static ArrayList<String> mapFieldsWithNaukri(ArrayList<String> data, String fieldData) {
		String fieldName = data.get(0);
		String pattern = data.get(1);
		String isMandatoryOnNaukri = data.get(2);
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(fieldData);
		
		String naukriFieldName = "";
		ArrayList<String> fields = new ArrayList<String>();
		
		if (m.find()) {
			naukriFieldName = fieldName;
		}
		
		fields.add(naukriFieldName);
		fields.add(isMandatoryOnNaukri);
		return fields;
	}
}