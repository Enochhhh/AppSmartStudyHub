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
		this.isTwoFactor = user.getIsTwoFactor();
		this.status = user.getStatus();
		this.totalDateDeletedOrBanned = 0L;
		if (user.getTimeAdminModified() != null) {
			LocalDateTime timeAdminModified = MethodUtils.convertoToLocalDateTime(user.getTimeAdminModified());
			LocalDateTime nowDate = MethodUtils.convertoToLocalDateTime(new Date());
			this.totalDateDeletedOrBanned = MethodUtils.distanceDaysBetweenTwoDate(timeAdminModified, nowDate, 
					ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()));
		}
		this.totalWorks = user.getTotalWorks();
		this.totalPomodoros = user.getTotalPomodoros();
		if (user.getTimeLastUse() != null) {
			this.timeLastUse = user.getTimeLastUse().getTime();
		}
		this.dueDatePremium = 0L;
		if (user.getDueDatePremium() != null) {
			LocalDateTime dueDateTimeZone = MethodUtils.convertoToLocalDateTime(user.getDueDatePremium());
			LocalDateTime nowDateTimeZone = MethodUtils.convertoToLocalDateTime(new Date());
			this.dueDatePremium = MethodUtils.distanceDaysBetweenTwoDate(nowDateTimeZone, dueDateTimeZone, 
					ZoneId.of(EnumZoneId.ASIA_HOCHIMINH.getNameZone()));
		}
	}
	
	public UserDTO(Integer rank, User user) {
		this.rank = rank;
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
		this.isTwoFactor = user.getIsTwoFactor();
		this.totalTimeFocus = user.getTotalTimeFocus();
		this.totalWorks = user.getTotalWorks();
	}
	
	private Integer rank;
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
	private Boolean isTwoFactor;
	private String status;
	private Long totalDateDeletedOrBanned;
	private String password;
	private Integer totalWorks;
	private Integer totalPomodoros;
	private Long timeLastUse;
	private Long dueDatePremium;
}
