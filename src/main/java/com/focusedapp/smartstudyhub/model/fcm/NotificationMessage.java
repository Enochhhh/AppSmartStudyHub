package com.focusedapp.smartstudyhub.model.fcm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<String> registrationTokens;
	private String title;
	private String body;
	private String image;
	private Map<String, String> data;
}
