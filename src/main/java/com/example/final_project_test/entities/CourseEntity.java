package com.example.final_project_test.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<TeacherCourseEntity> teacherCourse;
	
	@OneToMany(mappedBy = "course", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonBackReference
	private List<StudentCourseEntity> studentCourse;
	
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

	public List<TeacherCourseEntity> getTeacherCourse() {
		return teacherCourse;
	}

	public void setTeacherCourse(List<TeacherCourseEntity> teacherCourse) {
		this.teacherCourse = teacherCourse;
	}

	public List<StudentCourseEntity> getStudentCourse() {
		return studentCourse;
	}

	public void setStudentCourse(List<StudentCourseEntity> studentCourse) {
		this.studentCourse = studentCourse;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	

}
