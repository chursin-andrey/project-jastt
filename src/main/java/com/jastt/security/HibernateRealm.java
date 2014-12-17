package com.jastt.security;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.dal.providers.UserDataProvider;

/**
 * 
 * This is custom implementation of {@link Realm} which uses custom
 * {@link UsersDataProvider} for authentication of users. See
 * spring-shiro-security.xml for details <br>
 * <br>
 * Current implementation supports only {@link UsernamePasswordToken}
 * 
 * @author tillias
 * 
 */
public class HibernateRealm extends AuthorizingRealm {

	@Autowired
	private UserDataProvider dataProvider;

	public HibernateRealm() {
		super();
	}
	

	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken utoken = (UsernamePasswordToken) token;
		String login = utoken.getUsername();
		
		User user = dataProvider.getUserByLogin(login);
		if (user == null) {
			throw new UnknownAccountException(String.format("Can't find account info for =%s", login));
		} else {
			String password = new String(utoken.getPassword());
			String userRole = UserRoleEnum.USER.getMark();																														
			if(user.getUserRole().equals(userRole)){																																		
				SimpleAccount account = new SimpleAccount(login, password, getName());	
				return account;
			}
			
			if (ObjectUtils.compare(user.getPassword(), password) == 0) {
				SimpleAccount account = new SimpleAccount(login, password, getName());				
				return account;
				
			} else {
				throw new IncorrectCredentialsException();
			}
		}
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if(principals == null) {
		    throw new AuthorizationException("PrincipalCollection can not be null");
		}
		
		if(principals.isEmpty()) {
		    throw new AuthorizationException("PrincipalCollection can not be empty");
		}
		
		String login = (String)principals.getPrimaryPrincipal();
		User user = dataProvider.getUserByLogin(login);
		if (user == null) {
		    throw new AuthorizationException("Could not find user with specified login");
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole(user.getUserRole());
		return info;
	}
		
	
}
