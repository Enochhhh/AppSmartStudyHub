package com.focusedapp.smartstudyhub.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.focusedapp.smartstudyhub.model.compositekey.DeviceUserKey;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "device_user")
public class DeviceUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private DeviceUserKey deviceUserKey;
	
	@ManyToOne
	@MapsId("deviceId")
	@JoinColumn(name = "device_id", nullable = false)
	private Device device;
	
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "status")
	private String status;
	
	@Column(name = "time_last_login")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeLastLogin;
	
}
