package com.jastt.dal.entities;

// Generated 05.11.2014 13:34:24 by Hibernate Tools 4.0.0

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
 * ProjectEntity generated by hbm2java
 */
@Entity
@Table(name = "PROJECT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class ProjectEntity implements java.io.Serializable {

	private Integer id;
	private ServerEntity serverEntity;
	private String key;
	private String name;
	private Set<PermissionEntity> permissionEntities = new HashSet<PermissionEntity>(
			0);
	private Set<IssueEntity> issueEntities = new HashSet<IssueEntity>(0);

	public ProjectEntity() {
	}

	public ProjectEntity(ServerEntity serverEntity, String key, String name) {
		this.serverEntity = serverEntity;
		this.key = key;
		this.name = name;
	}

	public ProjectEntity(ServerEntity serverEntity, String key, String name,
			Set<PermissionEntity> permissionEntities,
			Set<IssueEntity> issueEntities) {
		this.serverEntity = serverEntity;
		this.key = key;
		this.name = name;
		this.permissionEntities = permissionEntities;
		this.issueEntities = issueEntities;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVER_ID", nullable = false)
	public ServerEntity getServerEntity() {
		return this.serverEntity;
	}

	public void setServerEntity(ServerEntity serverEntity) {
		this.serverEntity = serverEntity;
	}

	@Column(name = "KEY", nullable = false, length = 20)
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectEntity")
	public Set<PermissionEntity> getPermissionEntities() {
		return this.permissionEntities;
	}

	public void setPermissionEntities(Set<PermissionEntity> permissionEntities) {
		this.permissionEntities = permissionEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectEntity")
	public Set<IssueEntity> getIssueEntities() {
		return this.issueEntities;
	}

	public void setIssueEntities(Set<IssueEntity> issueEntities) {
		this.issueEntities = issueEntities;
	}

}