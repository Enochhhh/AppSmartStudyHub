package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.role = user.getRole();
		this.createdAt = user.getCreatedAt().getTime();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.dateOfBirth = user.getDateOfBirth() != null ? user.getDateOfBirth().getTime() : null;
		this.country = user.getCountry();
		this.imageUrl = user.getImageUrl();
		
		this.totalTimeFocus = user.getTotalTimeFocus();
	}
	
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String address;
	private Long dateOfBirth;
	private String country;
	private String imageUrl;
	private Integer totalTimeFocus;
	private String role;
	private Long createdAt;
	private String token;

}
