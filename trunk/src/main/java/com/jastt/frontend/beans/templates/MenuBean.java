package com.jastt.frontend.beans.templates;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jastt.business.enums.UserRoleEnum;



@Component
@Scope("request")
public class MenuBean {
	
	private boolean visibleForAuthenticated;
	private boolean visibleForAdmin;
	

	@PostConstruct
	public void init(){
		Subject subject = SecurityUtils.getSubject();
		if(subject.isAuthenticated()){
			visibleForAuthenticated = true;
		}
		String userRole = UserRoleEnum.ADMIN.getMark();
		if(subject.hasRole(userRole)){
			visibleForAdmin = true;
		}
	}


	public boolean isVisibleForAuthenticated() {
		return visibleForAuthenticated;
	}

	public void setVisibleForAuthenticated(boolean visible) {
		this.visibleForAuthenticated = visible;
	}
	
	public boolean isVisibleForAdmin() {
		return visibleForAdmin;
	}

	public void setVisibleForAdmin(boolean visibleForAdmin) {
		this.visibleForAdmin = visibleForAdmin;
	}
}
