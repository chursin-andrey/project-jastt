package com.jastt.business.enums;

import java.util.HashMap;
import java.util.Map;

public enum IssueStatusEnum {
	
	OPEN ("Open"), 
	IN_PROGRESS ("In progress"), 
	RESOLVED ("Resolved"), 
	REOPENED ("Reopened"), 
	CLOSED ("Closed");
	
	private String description;
	private static Map<String, IssueStatusEnum> typesMapping;
	
	public static void initMap(){
		typesMapping = new HashMap<String, IssueStatusEnum>();		
		for(IssueStatusEnum type : values()){
			typesMapping.put(type.description, type);
		}
	}
	
	public static IssueStatusEnum getType(String description){
		if(typesMapping == null){
			initMap();
		}
		if(typesMapping.containsKey(description)){
			return typesMapping.get(description);
		}
		return null;
	}
	
	private IssueStatusEnum(String description) {
		this.description = description;
	}
}
