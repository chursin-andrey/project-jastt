package com.sample.dal.entities;

// Generated Oct 30, 2013 9:27:58 AM by Hibernate Tools 3.6.0

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TeacherEntity generated by hbm2java
 */
@Entity
@Table(name = "TEACHER")
public class TeacherEntity implements java.io.Serializable {

	private Integer id;
	private UserEntity userEntity;
	private Integer experience;
	private Set<TeacherCourseEntity> teacherCourseEntities = new HashSet<TeacherCourseEntity>(0);
	private Set<TeacherReviewEntity> teacherReviewEntities = new HashSet<TeacherReviewEntity>(0);

	public TeacherEntity() {
	}

	public TeacherEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public TeacherEntity(UserEntity userEntity, Integer experience, Set<TeacherCourseEntity> teacherCourseEntities,
			Set<TeacherReviewEntity> teacherReviewEntities) {
		this.userEntity = userEntity;
		this.experience = experience;
		this.teacherCourseEntities = teacherCourseEntities;
		this.teacherReviewEntities = teacherReviewEntities;
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
	@JoinColumn(name = "USER_ID", nullable = false)
	public UserEntity getUserEntity() {
		return this.userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	@Column(name = "EXPERIENCE")
	public Integer getExperience() {
		return this.experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teacherEntity")
	public Set<TeacherCourseEntity> getTeacherCourseEntities() {
		return this.teacherCourseEntities;
	}

	public void setTeacherCourseEntities(Set<TeacherCourseEntity> teacherCourseEntities) {
		this.teacherCourseEntities = teacherCourseEntities;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "teacherEntity")
	public Set<TeacherReviewEntity> getTeacherReviewEntities() {
		return this.teacherReviewEntities;
	}

	public void setTeacherReviewEntities(Set<TeacherReviewEntity> teacherReviewEntities) {
		this.teacherReviewEntities = teacherReviewEntities;
	}

}