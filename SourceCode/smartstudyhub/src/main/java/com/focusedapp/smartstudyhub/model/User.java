package com.focusedapp.smartstudyhub.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@JsonInclude(value = Include.NON_NULL)
@Entity
public class User {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(name = "email")
	private String name;
	
	@Column(name = "passwords")
	private String password;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "date_of_birth")
	private String dateOfBirth;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "roles")
	private String role;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "otp_code")
	private String otpCode;
	
	@Column(name = "otp_time_expiration")
	@Temporal(TemporalType.TIMESTAMP)
	private Date otpTimeExpiration;
	
	@Column(name = "total_time_focus")
	private Integer totalTimeFocus;
	
	@Column(name = "status")
	private String status;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	public Date getOtpTimeExpiration() {
		return otpTimeExpiration;
	}

	public void setOtpTimeExpiration(Date otpTimeExpiration) {
		this.otpTimeExpiration = otpTimeExpiration;
	}

	public Integer getTotalTimeFocus() {
		return totalTimeFocus;
	}

	public void setTotalTimeFocus(Integer totalTimeFocus) {
		this.totalTimeFocus = totalTimeFocus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
		
}
