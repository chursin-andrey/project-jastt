package com.jastt.utils;

import org.dozer.loader.api.BeanMappingBuilder;


public class MapperBuilder extends BeanMappingBuilder {

	@Override
	protected void configure() {
//		mapping(UserEntity.class, User.class);
//		mapping(SomeEntity.class, Some.class).fields("userEntity", "user").fields("hotelEntity", "hotel");
	}
}
