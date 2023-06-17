package com.r2s.demo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "education")
public class Education {
//	@EmbeddedId
//	private UserSubjectKey id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
//	@MapsId("user_id")
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	@ManyToOne
//	@MapsId("subject_id")
	@JoinColumn(name = "subject_id")
	@JsonBackReference
	private Subject subject;

	private Date date;

	private double mark;

	private String semester;

	@Column(columnDefinition = " boolean default false")
	private boolean deleted;

//	@Embeddable
//	@Data
//	@AllArgsConstructor
//	@NoArgsConstructor
//	class UserSubjectKey {
//		@Column(name = "user_id")
//		private Long userId;
//
//		@Column(name = "subject_id")
//		private Long subjectId;
//
//		private Date date;
//	}
}
