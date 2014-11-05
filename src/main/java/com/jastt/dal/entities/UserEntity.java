package com.jastt.dal.entities;

// Generated 05.11.2014 13:34:24 by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * UserEntity generated by hbm2java
 */
@Entity
@Table(name = "USER_", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = {
		@UniqueConstraint(columnNames = "LOGIN"),
		@UniqueConstraint(columnNames = "EMAIL"),
		@UniqueConstraint(columnNames = "PASSWORD") })
public class UserEntity extends GenericDalEntity<Integer> implements java.io.Serializable {

	private ServerEntity serverEntity;
	private String login;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private Date birthday;
	private Set<PermissionEntity> permissionEntities = new HashSet<PermissionEntity>(
			0);

	public UserEntity() {
	}

	public UserEntity(ServerEntity serverEntity, String login,
			String firstName, String lastName, String password, String email,
			Date birthday) {
		this.serverEntity = serverEntity;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.birthday = birthday;
	}

	public UserEntity(ServerEntity serverEntity, String login,
			String firstName, String lastName, String password, String email,
			Date birthday, Set<PermissionEntity> permissionEntities) {
		this.serverEntity = serverEntity;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.birthday = birthday;
		this.permissionEntities = permissionEntities;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVER_ID", nullable = false)
	public ServerEntity getServerEntity() {
		return this.serverEntity;
	}

	public void setServerEntity(ServerEntity serverEntity) {
		this.serverEntity = serverEntity;
	}

	@Column(name = "LOGIN", unique = true, nullable = false, length = 20)
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "FIRST_NAME", nullable = false, length = 128)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "LAST_NAME", nullable = false, length = 256)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "PASSWORD", unique = true, nullable = false, length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "EMAIL", unique = true, nullable = false, length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BIRTHDAY", nullable = false, length = 26)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
	public Set<PermissionEntity> getPermissionEntities() {
		return this.permissionEntities;
	}

	public void setPermissionEntities(Set<PermissionEntity> permissionEntities) {
		this.permissionEntities = permissionEntities;
	}

}
