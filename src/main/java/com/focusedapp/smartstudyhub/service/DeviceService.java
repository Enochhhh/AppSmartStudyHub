package com.focusedapp.smartstudyhub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.dao.DeviceDAO;
import com.focusedapp.smartstudyhub.dao.DeviceUserDAO;
import com.focusedapp.smartstudyhub.model.Device;
import com.focusedapp.smartstudyhub.model.DeviceUser;
import com.focusedapp.smartstudyhub.model.User;
import com.focusedapp.smartstudyhub.model.compositekey.DeviceUserKey;
import com.focusedapp.smartstudyhub.model.custom.DeviceDTO;
import com.focusedapp.smartstudyhub.util.enumerate.EnumStatus;
import com.nimbusds.oauth2.sdk.util.CollectionUtils;

@Service
public class DeviceService {
	
	@Autowired DeviceDAO deviceDAO;
	@Autowired DeviceUserDAO deviceUserDAO;
	@Autowired UserService userService;
	
	/**
	 * Create and update device
	 * 
	 * @param dataDevice
	 * @param user
	 * @return
	 */
	public DeviceDTO createUpdateDevice(DeviceDTO dataDevice, User user) {
		Device deviceDatabase = new Device();
		if (StringUtils.isNotBlank(dataDevice.getId())) {
			Optional<Device> deviceDatabaseOpt = deviceDAO.findById(dataDevice.getId());
			if (!deviceDatabaseOpt.isEmpty()) {
				deviceDatabase = deviceDatabaseOpt.get();
			}
		}
		deviceDatabase.setId(dataDevice.getId());
		deviceDatabase.setDeviceName(dataDevice.getDeviceName());
		deviceDatabase.setDeviceType(dataDevice.getDeviceType());
		deviceDatabase.setIpAddress(dataDevice.getIpAddress());
		deviceDatabase.setLocation(dataDevice.getLocation());
		deviceDatabase.setMacAddress(dataDevice.getMacAddress());
		deviceDatabase.setRegistrationToken(dataDevice.getRegistrationToken());
		
		List<DeviceUser> deviceUsers = deviceDatabase.getUsersLogInDevice();
		Boolean isExist = false;
		if (!CollectionUtils.isEmpty(deviceUsers)) {
			for (DeviceUser device : deviceUsers) {
				if (device.getDeviceUserKey().getDeviceId().equals(deviceDatabase.getId()) 
						&& device.getDeviceUserKey().getUserId().equals(user.getId())) {
					device.setStatus(dataDevice.getStatus());
					if (dataDevice.getStatus().equals(EnumStatus.LOGIN.getValue())) {
						device.setTimeLastLogin(new Date());
					}
					isExist = true;
				}
			}
		} else {
			deviceUsers = new ArrayList<>();
		}
		if (!isExist) {
			DeviceUserKey deviceUserKey = new DeviceUserKey();
			deviceUserKey.setDeviceId(deviceDatabase.getId());
			deviceUserKey.setUserId(user.getId());
			DeviceUser deviceUser = new DeviceUser();
			deviceUser.setDeviceUserKey(deviceUserKey);
			deviceUser.setDevice(deviceDatabase);
			deviceUser.setUser(user);
			deviceUser.setStatus(dataDevice.getStatus());
			if (dataDevice.getStatus().equals(EnumStatus.LOGIN.getValue())) {
				deviceUser.setTimeLastLogin(new Date());
			}
			deviceUsers.add(deviceUser);
		}

		deviceDatabase.setUsersLogInDevice(deviceUsers);
		deviceDAO.save(deviceDatabase);
		return new DeviceDTO(deviceDatabase, user);
	}
	
	/**
	 * Refresh Registration Token Of Device
	 * 
	 * @param data
	 * @return
	 */
	public DeviceDTO refreshRegistrationTokenOfDevice(DeviceDTO data) {
		Optional<Device> device = deviceDAO.findById(data.getId());
		if (device.isEmpty()) {
			return null;
		}
		Device deviceDatabase = device.get();
		deviceDatabase.setRegistrationToken(data.getRegistrationToken());
		deviceDAO.save(deviceDatabase);
		return new DeviceDTO(deviceDatabase);
	}
	
	/**
	 * Logout Devices Selected
	 * 
	 * @param ids
	 * @return
	 */
	public List<DeviceDTO> logOutDevicesSelected(List<DeviceDTO> devicesSelected, User user) {
		List<String> ids = devicesSelected.stream()
				.map(d -> d.getId())
				.collect(Collectors.toList());
		List<DeviceUser> deviceUsers = deviceUserDAO.findByDeviceIdInAndUserId(ids, user.getId());
		if (CollectionUtils.isEmpty(deviceUsers)) {
			return null;
		}
		deviceUsers = deviceUsers.stream()
				.map(d -> {
					d.setStatus(EnumStatus.LOGOUT.getValue());
					return d;
				})
				.collect(Collectors.toList());
		deviceUserDAO.saveAll(deviceUsers);
		return deviceUsers.stream()
				.map(d -> new DeviceDTO(d.getDevice(), d.getUser()))
				.collect(Collectors.toList());
	}
	
	/**
	 * Delete Devices Selected
	 * 
	 * @param ids
	 * @return
	 */
	public Boolean deleteDevicesSelected(List<DeviceDTO> devicesSelected, User user) {
		List<String> ids = devicesSelected.stream()
				.map(d -> d.getId())
				.collect(Collectors.toList());
		List<DeviceUser> deviceUsers = deviceUserDAO.findByDeviceIdInAndUserId(ids, user.getId());
		if (CollectionUtils.isEmpty(deviceUsers)) {
			return false;
		}
		deviceUserDAO.deleteAll(deviceUsers);
		return true;
	}
	
	
	/**
	 * Get Devices of User
	 * 
	 * @return
	 */
	public List<DeviceDTO> getDevicesOfUser(User user) {
		List<DeviceUser> deviceUsers = deviceUserDAO.findByUserId(user.getId());
		if (CollectionUtils.isEmpty(deviceUsers)) {
			return new ArrayList<>();
		}
		return deviceUsers.stream()
				.map(d -> new DeviceDTO(d.getDevice(), d.getUser()))
				.collect(Collectors.toList());
	}
	
	public List<DeviceUser> findDeviceUserByUserId(Integer userId) {
		return deviceUserDAO.findByUserId(userId);
	}
	
	/**
	 * Get Specific Device of User
	 * 
	 * @return
	 */
	public DeviceDTO getSpecificDeviceOfUser(String deviceId, User user) {
		DeviceUser deviceUser = deviceUserDAO.findByDeviceIdAndUserId(deviceId, user.getId());
		if (deviceUser == null) {
			return null;
		}
		return new DeviceDTO(deviceUser.getDevice(), deviceUser.getUser());
	}
	
}
