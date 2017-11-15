package app.com.naukri.mapFields;

public class FieldAssigner {
	
	public String getFirstNameAlone(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		return constructedFieldName;
	}
	
	public String getLastNameWithMidName(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		return constructedFieldName;
	}
	
	public String getName(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		return constructedFieldName;
	}
	
	public String getEmail(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		return constructedFieldName;	
	}
	
	public String getMobileNo(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		return constructedFieldName;	
	}
		
	public String getFlatCtcInLacs(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		
		if (isMandatoryOnSite) {
			constructedFieldName = "#execTernaryCond|||" + constructedFieldName + ";0";
		}
		
		return constructedFieldName;
	}
	
	public String getCityLabel(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);		
		return constructedFieldName;
	}

	public String getPrevWorkExp(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		if (isMandatoryOnSite) {
			constructedFieldName = "#execTernaryCond|||" + constructedFieldName + ";0";
		}
		return constructedFieldName;
	}

	public String getExpInMonths(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		if (isMandatoryOnSite) {
			constructedFieldName = "#execTernaryCond|||" + constructedFieldName + ";0";
		}
		return constructedFieldName;
	}
	
	public String getTotalExperience(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		if (isMandatoryOnSite) {
			constructedFieldName = "#execTernaryCond|||" + constructedFieldName + ";0";
		}
		return constructedFieldName;
	}
	
	public String getStateLabel(String naukriFieldName, boolean isMandatoryOnNaukri, boolean isMandatoryOnSite) {
		String constructedFieldName = "*" + naukriFieldName;
		constructedFieldName = mapNaukriMandatoryField(constructedFieldName, isMandatoryOnNaukri);
		if (isMandatoryOnSite) {
			constructedFieldName = "#execTernaryCond|||" + constructedFieldName + ";NotMentioned";
		}
		return constructedFieldName;
	}
	
	private static String mapNaukriMandatoryField(String constructedFieldName, boolean isMandatoryOnNaukri) {
		if (isMandatoryOnNaukri) {
			constructedFieldName = "#execTernaryCond|||" + constructedFieldName + ";NotMentioned";
		}
		return constructedFieldName;
	}
}