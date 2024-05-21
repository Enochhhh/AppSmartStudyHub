package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@Entity
@Table(name = "device")
public class Device implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length = 1000)
	private String id;
	
	@Column(name = "registration_token")
	private String registrationToken;
	
	@Column(name = "device_type")
	private String deviceType;
	
	@Column(name = "device_name", length = 100)
	private String deviceName;
	
	@Column(name = "location", length = 300)
	private String location;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "mac_address")
	private String macAddress;
	
	@OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<DeviceUser> usersLogInDevice;
	
}
