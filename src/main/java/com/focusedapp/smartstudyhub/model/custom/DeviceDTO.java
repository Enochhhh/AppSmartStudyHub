package com.focusedapp.smartstudyhub.model.custom;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.Device;
import com.focusedapp.smartstudyhub.model.DeviceUser;
import com.focusedapp.smartstudyhub.model.User;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class DeviceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private Integer userId;
	private String registrationToken;
	private String deviceType;
	private String deviceName;
	private String location;
	private String ipAddress;
	private String macAddress;
	private String status;
	private Long timeLastLogin;
	
	public DeviceDTO(Device device, User user) {
		this.id = device.getId();
		this.userId = user.getId();
		this.registrationToken = device.getRegistrationToken();
		this.deviceName = device.getDeviceName();
		this.deviceType = device.getDeviceType();
		this.location = device.getLocation();
		this.ipAddress = device.getIpAddress();
		this.macAddress = device.getMacAddress();
		List<DeviceUser> deviceUsers = device.getUsersLogInDevice();
		if (!CollectionUtils.isEmpty(deviceUsers)) {
			Optional<DeviceUser> deviceUser = deviceUsers.stream()
					.filter(d -> d.getDevice().getId().equals(device.getId()) && d.getUser().getId().equals(user.getId()))
					.findFirst();
			if (deviceUser.isPresent()) {
				this.status = deviceUser.get().getStatus();
				if (deviceUser.get().getTimeLastLogin() != null) {
					this.timeLastLogin = deviceUser.get().getTimeLastLogin().getTime();
				}
			}
		}
		
	}
	
	public DeviceDTO(Device device) {
		this.id = device.getId();
		this.registrationToken = device.getRegistrationToken();
		this.deviceName = device.getDeviceName();
		this.deviceType = device.getDeviceType();
		this.location = device.getLocation();
		this.ipAddress = device.getIpAddress();
		this.macAddress = device.getMacAddress();		
	}
}
