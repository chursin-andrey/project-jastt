package com.jastt.frontend.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="authBean")
@Scope("session")
public class LoginBean {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginBean.class);
	
	public String doLogin() {
		
		
		return "/protected/main.xhtml";
		
	}
}
