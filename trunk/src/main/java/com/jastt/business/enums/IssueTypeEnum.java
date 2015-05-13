package com.jastt.business.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum IssueTypeEnum {
	
	BUG ("Bug"), 
	IMPROVEMENT ("Improvement"), 
	NEW_FEATURE ("New feature"), 
	TASK("Task");
	
	private String description;
	private static Map<String, IssueTypeEnum> typesMapping;
	
	public static void initMap(){
		typesMapping = new HashMap<String, IssueTypeEnum>();		
		for(IssueTypeEnum type : values()){
			typesMapping.put(type.description, type);
		}
	}
	
	public static IssueTypeEnum getType(String description){
		if(typesMapping == null){
			initMap();
		}
		if(typesMapping.containsKey(description)){
			return typesMapping.get(description);
		}
		return null;
	}
	
	private IssueTypeEnum(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Collection<IssueTypeEnum> getTypes(Collection<String> issueType) {
		Collection<IssueTypeEnum> result = new ArrayList<IssueTypeEnum>(issueType.size());
		for (String i : issueType) {
			result.add(getType(i));
		}
		return result;
	}

	public static Collection<String> getDescriptions(Collection<IssueTypeEnum> issueType) {
		Collection<String> result = new ArrayList<String>();
		for (IssueTypeEnum e : issueType) {
			result.add(e.getDescription());
		}
		return result;
	}
	
}
