package com.focusedapp.smartstudyhub.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focusedapp.smartstudyhub.model.Device;

@Repository
public interface DeviceDAO extends JpaRepository<Device, String> {

	List<Device> findByIdIn(List<String> ids);
}
