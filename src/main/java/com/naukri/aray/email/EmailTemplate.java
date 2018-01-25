package com.naukri.aray.email;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import com.naukri.aray.constants.Constants;

public class EmailTemplate {

	private String templateId;

	private String template;

	private Map<String, String> replacementParams;

	public EmailTemplate(String templateId) {
		this.templateId = templateId;
		try {
			this.template = loadTemplate(templateId);
		} catch (Exception e) {
			this.template = Constants.BLANK;
		}
	}

	private String loadTemplate(String templateId) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("//home/gaurav.srivastava/email-templates/" + templateId).getFile());
		String content = Constants.BLANK;
		try {
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			throw new Exception("Could not read template with ID = " + templateId);
		}
		return content;
	}

	public String getTemplate(Map<String, Integer> replacements) {
		String cTemplate = this.getTemplate();

		if (cTemplate != null) {
			for (Map.Entry<String, Integer> entry : replacements.entrySet()) {
				cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", String.valueOf(entry.getValue()));
			}
		}
		
		return cTemplate;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Map<String, String> getReplacementParams() {
		return replacementParams;
	}

	public void setReplacementParams(Map<String, String> replacementParams) {
		this.replacementParams = replacementParams;
	}
	
	
}
