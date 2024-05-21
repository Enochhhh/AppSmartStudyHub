package com.focusedapp.smartstudyhub.model.fcm;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class NotificationMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String registrationToken;
	private String title;
	private String body;
	private String image;
	private Map<String, String> data;
}
