package com.focusedapp.smartstudyhub.model.custom;


import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleOAuth2UserInfo  extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(OAuth2User oAuth2User, String provider) {
        super(oAuth2User, provider);
    }

    @Override
    public String getId() {
        return (String) oAuth2User.getAttributes().get("sub");
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
        return (String) oAuth2User.getAttributes().get("picture");
    }
    
    @Override
    public String getProvider() {
    	return provider;
    }
}
