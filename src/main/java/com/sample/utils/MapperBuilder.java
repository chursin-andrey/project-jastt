package com.sample.utils;

import org.dozer.loader.api.BeanMappingBuilder;

import com.sample.business.domain.entities.User;
import com.sample.dal.entities.UserEntity;

public class MapperBuilder extends BeanMappingBuilder {

	@Override
	protected void configure() {
		mapping(UserEntity.class, User.class);
	}
}
