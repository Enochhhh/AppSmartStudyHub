package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.util.MethodUtils;
import com.focusedapp.smartstudyhub.util.enumerate.EnumZoneId;

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
	private Long totalDateDeletedOrBanned;
	
	public UserAdminCreatedDTO(User user) {
		this.id = user.getId();
		this.userName = user.getUserName();
		this.password = null;
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.phoneNumber = user.getPhoneNumber();
		this.address = user.getAddress();
		this.dateOfBirth = user.getDateOfBirth() != null ? user.getDateOfBirth().getTime() : null;
		this.country = user.getCountry();
		this.imageUrl = user.getImageUrl();
		this.totalTimeFocus = user.getTotalTimeFocus();
		this.role = user.getRole();
		this.provider = user.getProvider();
		this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().getTime() : null;
		this.status = user.getStatus();
		this.totalDateDeletedOrBanned = 0L;
		if (user.getTimeAdminModified() != null) {
			LocalDateTime timeAdminModified = MethodUtils.convertoToLocalDateTime(user.getTimeAdminModified());
			LocalDateTime nowDate = MethodUtils.convertoToLocalDateTime(new Date());
			this.totalDateDeletedOrBanned = MethodUtils.distanceDaysBetweenTwoDate(timeAdminModified, nowDate, 
					ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()));
		}
	}

}
