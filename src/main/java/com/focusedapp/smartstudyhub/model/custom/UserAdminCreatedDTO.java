package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class UserAdminCreatedDTO implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String userName;
	private String password;
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
	private String provider;
	private Long createdAt;
	private String status;
	private Integer totalDateDeletedOrBanned;

}
