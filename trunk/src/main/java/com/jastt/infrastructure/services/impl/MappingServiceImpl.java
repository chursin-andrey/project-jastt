package com.jastt.infrastructure.services.impl;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import com.jastt.infrastructure.services.MappingService;
import com.jastt.utils.MapperBuilder;

@Service
public class MappingServiceImpl implements MappingService {

	private DozerBeanMapper mapper;
	
	@Override
	public <T> T map(Object sourceObject, Class<T> targetClass){
		if (mapper == null){
			mapper = new DozerBeanMapper();
			mapper.addMapping(new MapperBuilder());
		}
		
		return mapper.map(sourceObject, targetClass);
	}
}
