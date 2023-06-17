package com.r2s.demo.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "identification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "expired_date")
	private Date expiredDate;

	@OneToOne(mappedBy = "identification", cascade = CascadeType.ALL)
	@JsonBackReference
//	@JsonIgnore
	private User user;
}
