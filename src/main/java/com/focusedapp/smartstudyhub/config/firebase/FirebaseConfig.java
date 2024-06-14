package com.focusedapp.smartstudyhub.config.firebase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {
	
	@Value("${firebase.service-account-key}")
	private String base64ServiceAccount;
	
	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(base64ServiceAccount);
        ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(decodedBytes);

		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccountStream);
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "my-app");
		return FirebaseMessaging.getInstance(app);
	}
	
}
