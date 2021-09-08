package com.tunts.jean.entities;

import java.util.Objects;

public class Student {

	private Long enrollmentId;
	private String name;
	private Integer absence;
	private Double testGrade1;
	private Double testGrade2;
	private Double testGrade3;
	private String situation = "";
	private Double gradeFinalApproval = 0.0;

	public Student() {
	}

	public Student(Long enrollmentId, String name, Integer absence, Double testGrade1, Double testGrade2,
			Double testGrade3) {
		this.enrollmentId = enrollmentId;
		this.name = name;
		this.absence = absence;
		this.testGrade1 = testGrade1;
		this.testGrade2 = testGrade2;
		this.testGrade3 = testGrade3;
		failedByAverageGradeOrAbsence();
	}
	
	//Method that calculates the student's situation according to grade and class attendance
	private void failedByAverageGradeOrAbsence() {
		if (failedByAbsence()) {
			this.situation = "Reprovado por Falta";
		}
		
		if (getGradeAverage() >= 7 && !failedByAbsence()) {
			this.situation = "Aprovado";
		}
		
		if (getGradeAverage() < 7 &&  getGradeAverage() >= 5 && !failedByAbsence()) {
			this.situation = "Exame Final";
			this.gradeFinalApproval = 10 - getGradeAverage();
		}
		
		if (getGradeAverage() < 5 && !failedByAbsence()) {
			this.situation = "Reprovado por Nota";
		}
	}
	
	//Method returns true or false if the student doesn't have the minimum frequency for approval
	private boolean failedByAbsence() {
		if (absence > (60 * 0.25)) {
			return true;
		}
		return false;
	}
	
	//Method for calculating the student's average
	private Double getGradeAverage() {
		return Math.ceil(((testGrade1 + testGrade2 + testGrade3) / 10) / 3); 
	}


	public Long getEnrollment() {
		return enrollmentId;
	}

	public void setEnrollment(Long enrollment) {
		this.enrollmentId = enrollment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAbsence() {
		return absence;
	}

	public void setAbsence(Integer absence) {
		this.absence = absence;
	}

	public Double getTestGrade1() {
		return testGrade1;
	}

	public void setTestGrade1(Double testGrade1) {
		this.testGrade1 = testGrade1;
	}

	public Double getTestGrade2() {
		return testGrade2;
	}

	public void setTestGrade2(Double testGrade2) {
		this.testGrade2 = testGrade2;
	}

	public Double getTestGrade3() {
		return testGrade3;
	}

	public void setTestGrade3(Double testGrade3) {
		this.testGrade3 = testGrade3;
	}

	public String getSituation() {
		return situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	public Double getGradeFinalApproval() {
		return gradeFinalApproval;
	}

	public void setGradeFinalApproval(Double gradeFinalApproval) {
		this.gradeFinalApproval = gradeFinalApproval;
	}

	@Override
	public int hashCode() {
		return Objects.hash(enrollmentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(enrollmentId, other.enrollmentId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Student [enrollmentId=");
		builder.append(enrollmentId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", absence=");
		builder.append(absence);
		builder.append(", testGrade1=");
		builder.append(testGrade1);
		builder.append(", testGrade2=");
		builder.append(testGrade2);
		builder.append(", testGrade3=");
		builder.append(testGrade3);
		builder.append(", situation=");
		builder.append(situation);
		builder.append(", gradeFinalApproval=");
		builder.append(gradeFinalApproval);
		builder.append("]");
		return builder.toString();
	}

}
