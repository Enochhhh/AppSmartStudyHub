package com.focusedapp.smartstudyhub.service;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.focusedapp.smartstudyhub.model.custom.CustomOAuth2User;
import com.focusedapp.smartstudyhub.util.enumerate.Provider;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        return getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), user);  
    }
	
	public OAuth2User getOAuth2UserInfo(String registrationId, OAuth2User oAuth2User) {
		if(registrationId.equalsIgnoreCase(Provider.GOOGLE.toString())) {
            return new CustomOAuth2User(oAuth2User, registrationId);
        } else {
            throw new InternalAuthenticationServiceException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
	}
	
}
