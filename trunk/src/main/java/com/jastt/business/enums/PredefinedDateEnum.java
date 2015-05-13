package com.jastt.business.enums;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public enum PredefinedDateEnum {
	
	ALL_TIME ("All Time"), 
	TODAY ("Today"), 
	YESTERDAY ("Yesterday"), 
	THIS_WEEK ("This week"), 
	LAST_WEEK ("Last week"), 
	LAST_SEVEN_DAYS ("Last seven days"), 
	LAST_FOUR_WEEKS ("Last four weeks"), 
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
	
	public Pair<Date, Date> getPeriod() {
		MutablePair<Date, Date> pairOfDates = new MutablePair<Date, Date>();
		
		Calendar calendar = Calendar.getInstance();
		Calendar baseDate = DateUtils.truncate(calendar, Calendar.DATE);
		Calendar baseMonth = DateUtils.truncate(calendar, Calendar.MONTH);
		Calendar baseYear = DateUtils.truncate(calendar, Calendar.YEAR);
		pairOfDates.setRight(calendar.getTime());
		
		switch (this) {
			case ALL_TIME:{
				pairOfDates.setRight(null);
				break;
			}
			case TODAY:{
				pairOfDates.setLeft(baseDate.getTime());
				break;
			}
			case YESTERDAY:{
				pairOfDates.setRight(baseDate.getTime());
				baseDate.add(Calendar.DATE, -1);
				pairOfDates.setLeft(baseDate.getTime());
				break;
			}
			case THIS_WEEK:{
				baseDate.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				pairOfDates.setLeft(baseDate.getTime());
				break;
			}
			case LAST_WEEK:{
				baseDate.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
				pairOfDates.setRight(baseDate.getTime());
				baseDate.add(Calendar.DATE, -7);
				pairOfDates.setLeft(baseDate.getTime());
				break;
			}
			case LAST_SEVEN_DAYS:{
				baseDate.add(Calendar.DATE, -7);
				pairOfDates.setLeft(baseDate.getTime());
				break;
			}
			case LAST_FOUR_WEEKS:{
				baseDate.add(Calendar.DATE, - 28);
				pairOfDates.setLeft(baseDate.getTime());
				break;
			}
			case THIS_MONTH:{
				pairOfDates.setLeft(baseMonth.getTime());
				break;
			}
			case LAST_MONTH:{
				pairOfDates.setRight(baseMonth.getTime());
				baseMonth.add(Calendar.MONTH, -1);
				pairOfDates.setLeft(baseMonth.getTime());
				break;
			}
			case LAST_THIRTY_DAYS:{
				baseDate.add(Calendar.DATE, -30);
				pairOfDates.setLeft(baseDate.getTime());
				break;
			}
			case THIS_YEAR:{
				pairOfDates.setLeft(baseYear.getTime());
				break;
			}
			default: break;
		}
		
		return pairOfDates;
	}
	
	private PredefinedDateEnum(String description) {
		this.description = description;
	}
}
