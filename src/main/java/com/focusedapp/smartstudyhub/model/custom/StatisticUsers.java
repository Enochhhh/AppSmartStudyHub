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
public class StatisticUsers implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long date;
	private Long dateFrom;
	private Long dateTo;
	private Long totalUsers;
	
	private Integer totalUsersGuest;
	private Integer totalUserRegisterAccount;
	private Integer totalUserRegisterPremium;
	
	public StatisticUsers(Long date, Long totalUsers) {
		this.date = date;
		this.totalUsers = totalUsers;
	}
	
	public StatisticUsers(Long dateFrom, Long dateTo, Long totalUsers) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.totalUsers = totalUsers;
	}
	
	public StatisticUsers(Integer totalUsersGuest, Integer totalUserRegisterAccount, Integer totalUserRegisterPremium) {
		this.totalUsersGuest = totalUsersGuest;
		this.totalUserRegisterAccount = totalUserRegisterAccount;
		this.totalUserRegisterPremium = totalUserRegisterPremium;
	}
}
