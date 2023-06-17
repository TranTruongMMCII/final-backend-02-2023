package com.r2s.demo.DTO;

import java.util.Date;

import org.springframework.util.ObjectUtils;

import com.r2s.demo.model.Education;
import com.r2s.demo.model.Subject;
import com.r2s.demo.model.User;

import lombok.Data;

@Data
public class EducationDTOResponse {
	private Long id;
	private Date date;
	private double mark;
	private String semester;
	private Long userId;
	private String userName;
	private Long subjectId;
	private String subjectName;

	public EducationDTOResponse(Education education) {
		this.id = education.getId();
		this.date = education.getDate();
		this.mark = education.getMark();
		this.semester = education.getSemester();

		if (!ObjectUtils.isEmpty(education.getUser())) {
			User user = education.getUser();
			this.userId = user.getId();
			this.userName = user.getName();
		}

		if (!ObjectUtils.isEmpty(education.getSubject())) {
			Subject subject = education.getSubject();
			this.subjectId = subject.getId();
			this.subjectName = subject.getName();
		}
	}
}
