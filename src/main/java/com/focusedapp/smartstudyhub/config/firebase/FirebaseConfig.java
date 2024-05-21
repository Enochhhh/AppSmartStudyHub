package com.focusedapp.smartstudyhub.config.firebase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.focusedapp.smartstudyhub.exception.EnvironmentVariableNotFoundException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {
	
	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		
		String base64ServiceAccount = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY_FIRST");
		base64ServiceAccount = base64ServiceAccount.concat(System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY_SECOND"));
		if (StringUtils.isBlank(base64ServiceAccount)) {
            throw new EnvironmentVariableNotFoundException("Environment variable FIREBASE_SERVICE_ACCOUNT_KEY not found.");
        }
		byte[] decodedBytes = Base64.getDecoder().decode(base64ServiceAccount);
        ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(decodedBytes);
        
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccountStream);
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
		return FirebaseMessaging.getInstance(app);
	}
}
