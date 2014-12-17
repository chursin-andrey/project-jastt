package com.jastt.business.enums;

import java.util.HashMap;
import java.util.Map;

public enum PredefinedDateEnum {
	
	ALL_TIME ("All Time"), 
	TODAY ("Today"), 
	YESTERDAY ("Yesterday"), 
	THIS_WEEK ("This week"), 
	LAST_WEEK ("Last week"), 
	LAST_SEVEN_DAYS ("Last seven days"), 
	THIS_MONTH ("This month"), 
	LAST_MONTH ("Last month"), 
	LAST_THIRTY_DAYS ("Last thirty days"), 
	THIS_YEAR ("This year");
	
	
	private String description;
	private static Map<String, PredefinedDateEnum> typesMapping;
	
	public static void initMap(){
		typesMapping = new HashMap<String, PredefinedDateEnum>();
		
		for(PredefinedDateEnum type : values()){
			typesMapping.put(type.description, type);
		}
	}
	
	public static PredefinedDateEnum getType(String description){
		if(typesMapping == null){
			initMap();
		}
		if(typesMapping.containsKey(description)){
			return typesMapping.get(description);
		}
		return null;
	}
	
	public String getDescription(){
		return description;
	}
	
	private PredefinedDateEnum(String description) {
		this.description = description;
	}
}
