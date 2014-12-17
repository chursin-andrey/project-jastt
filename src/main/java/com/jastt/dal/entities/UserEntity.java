package com.jastt.dal.entities;

// Generated 07.12.2014 16:34:26 by Hibernate Tools 4.3.1

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
import javax.persistence.UniqueConstraint;



/**
 * UserEntity generated by hbm2java
 */
@Entity
@Table(name = "USER_", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = @UniqueConstraint(columnNames = "LOGIN"))
public class UserEntity extends GenericDalEntity<Integer> implements java.io.Serializable {

	private ServerEntity serverEntity;
	private String login;
	private String name;
	private String password;
	private String email;
	private String userRole;
	private Set<PermissionEntity> permissionEntities = new HashSet<PermissionEntity>(
			0);

	public UserEntity() {
	}

	public UserEntity(String login, String userRole) {
		this.login = login;
		this.userRole = userRole;
	}

	public UserEntity(ServerEntity serverEntity, String login, String name,
			String password, String email, String userRole,
			Set<PermissionEntity> permissionEntities) {
		this.serverEntity = serverEntity;
		this.login = login;
		this.name = name;
		this.password = password;
		this.email = email;
		this.userRole = userRole;
		this.permissionEntities = permissionEntities;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVER_ID")
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

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PASSWORD", length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "EMAIL", length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "USER_ROLE", nullable = false, length = 20)
	public String getUserRole() {
		return this.userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
	public Set<PermissionEntity> getPermissionEntities() {
		return this.permissionEntities;
	}

	public void setPermissionEntities(Set<PermissionEntity> permissionEntities) {
		this.permissionEntities = permissionEntities;
	}

}