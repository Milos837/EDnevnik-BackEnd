package com.example.final_project_test.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.example.final_project_test.entities.enums.ECourseSemester;
import com.example.final_project_test.entities.enums.ESchoolYear;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "course")
public class CourseEntity {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private Integer weeklyHours;
	
	@Column
	private ESchoolYear year;
	
	@Column
	private ECourseSemester semester;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "teacher_course", joinColumns =
		{@JoinColumn(name = "teacher_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "course_id",
		nullable = false, updatable = false) })
	private List<TeacherEntity> teachers;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "class_course", joinColumns =
		{@JoinColumn(name = "class_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "course_id",
		nullable = false, updatable = false) })
	private List<ClassEntity> classes;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<GradeEntity> grades;
	
	@Version
	private Integer version;
	
	public CourseEntity() {
		super();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeeklyHours() {
		return weeklyHours;
	}

	public void setWeeklyHours(Integer weeklyHours) {
		this.weeklyHours = weeklyHours;
	}

	public ESchoolYear getYear() {
		return year;
	}

	public void setYear(ESchoolYear year) {
		this.year = year;
	}

	public ECourseSemester getSemester() {
		return semester;
	}

	public void setSemester(ECourseSemester semester) {
		this.semester = semester;
	}

	public List<TeacherEntity> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherEntity> teachers) {
		this.teachers = teachers;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
		this.classes = classes;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	

}
