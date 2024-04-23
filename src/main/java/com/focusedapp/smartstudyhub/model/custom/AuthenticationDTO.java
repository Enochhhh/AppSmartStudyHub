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
public class AuthenticationDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public AuthenticationDTO(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role = user.getRole();
		this.createdAt = user.getCreatedAt().getTime();
		this.imageUrl = user.getImageUrl();
		this.isTwoFactor = user.getIsTwoFactor();
	}
	
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String role;
	private Long createdAt;
	private String otpCode;
	private String token;
	private Long otpTimeExpiration;
	private String imageUrl;
	private Integer guestId;
	private Boolean isTwoFactor;
	
}
