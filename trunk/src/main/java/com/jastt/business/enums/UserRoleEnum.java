package com.jastt.business.enums;

import java.util.HashMap;
import java.util.Map;


public enum UserRoleEnum {
	ADMIN ("admin","Admin"), 
	USER ("user","User");
	
	private String mark;
	private String description;
	private static  Map<String, UserRoleEnum> typesMapping;
	
		
	public static void initMap(){
		typesMapping = new HashMap<String, UserRoleEnum>();
		for(UserRoleEnum userRole : UserRoleEnum.values()){
			typesMapping.put(userRole.mark, userRole);
		}
	}
	
	public static UserRoleEnum getType(String mark){
		if(typesMapping == null){
			initMap();
		}
		if(typesMapping.containsKey(mark)){
			return typesMapping.get(mark);
		}
		return null;
	}
	
	
	public String getMark(){
		return mark;
	}
	
	public String getDescription(){
		return description;
	}
	
	
	private UserRoleEnum(String type, String description) {
		this.mark = type;
		this.description = description;
	}
	
	
}
