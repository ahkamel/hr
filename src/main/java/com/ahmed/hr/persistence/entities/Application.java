package com.ahmed.hr.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.ahmed.hr.persistence.enums.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "offer_id", "email" }))
@Entity
public class Application implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
	@Email
	@NotNull
	@NotEmpty
	private String email;
	@Lob
	private String resume;

	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Application [id=" + id + ", offer=" + offer + ", email=" + email + ", resume=" + resume + ", status="
				+ status + "]";
	}

}
