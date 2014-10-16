package com.sample.business.domain.entities;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class User extends PersistentEntity<Integer> {

	private static final long serialVersionUID = 4971452245542430428L;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Date birthday;

	public User() {
		birthday = new Date();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getFullName() {

		if (!StringUtils.isEmpty(lastName) && !StringUtils.isEmpty(firstName)) {
			return String.format("%s, %s", lastName, firstName);
		} else {
			if (StringUtils.isEmpty(lastName))
				return firstName;
			else if (StringUtils.isEmpty(firstName))
				return lastName;

			return StringUtils.EMPTY;
		}
	}
}
