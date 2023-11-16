package com.focusedapp.smartstudyhub.model.custom;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
	public FacebookOAuth2UserInfo(OAuth2User oAuth2User, String provider) {
		super(oAuth2User, provider);
	}

	@Override
	public String getId() {
		return (String) oAuth2User.getAttributes().get("id");
	}

	@Override
	public String getName() {
		return (String) oAuth2User.getAttributes().get("name");
	}

	@Override
	public String getEmail() {
		return (String) oAuth2User.getAttributes().get("email");
	}

	@Override
	public String getImageUrl() {
		if (oAuth2User.getAttributes().containsKey("picture")) {
			Map<String, Object> pictureObj = (Map<String, Object>) oAuth2User.getAttributes().get("picture");
			if (pictureObj.containsKey("data")) {
				Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
				if (dataObj.containsKey("url")) {
					return (String) dataObj.get("url");
				}
			}
		}
		return null;
	}
	
	 @Override
	    public String getProvider() {
	    	return provider;
	    }
}
