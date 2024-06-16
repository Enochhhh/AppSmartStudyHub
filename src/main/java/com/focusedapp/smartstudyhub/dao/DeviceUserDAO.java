package com.focusedapp.smartstudyhub.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.DeviceUser;

@Repository
public interface DeviceUserDAO extends JpaRepository<DeviceUser, Integer> {
	List<DeviceUser> findByDeviceIdInAndUserId(List<String> deviceIds, Integer userId);
	List<DeviceUser> findByUserId(Integer userId);
	DeviceUser findByDeviceIdAndUserId(String deviceId, Integer userId);
}
