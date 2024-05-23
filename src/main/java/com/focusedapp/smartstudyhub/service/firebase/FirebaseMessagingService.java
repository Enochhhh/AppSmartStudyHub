package com.focusedapp.smartstudyhub.service.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.exception.ISException;
import com.focusedapp.smartstudyhub.model.fcm.NotificationMessage;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirebaseMessagingService {

	@Autowired
	private FirebaseMessaging firebaseMessaging;
	
	public BatchResponse sendNotification(NotificationMessage notificationMessage) {
		List<String> registrationTokens = notificationMessage.getRegistrationTokens();
		Notification notification = Notification.builder()
				.setTitle(notificationMessage.getTitle())
				.setBody(notificationMessage.getBody())
				.setImage(notificationMessage.getImage())
				.build();
		
		MulticastMessage message = MulticastMessage.builder()
				.addAllTokens(registrationTokens)
				.setNotification(notification)
				.putAllData(notificationMessage.getData() == null ? new HashMap<>() : notificationMessage.getData())
				.build();
		
		BatchResponse batchResponse = null;
		try {
			batchResponse = firebaseMessaging.sendEachForMulticast(message);
		} catch (FirebaseMessagingException e) {
			throw new ISException(e);	
		}
		if (batchResponse.getFailureCount() > 0) {
            List<SendResponse> responses = batchResponse.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    failedTokens.add(registrationTokens.get(i));
                }
            }
            log.info("List of tokens that caused failures: " + failedTokens);
        }
		return batchResponse;
	}
	
}
