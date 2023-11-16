package com.focusedapp.smartstudyhub.model.custom;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

	private OAuth2User oAuth2User;
	private String provider;

	public CustomOAuth2User(OAuth2User oAuth2User, String provider) {
		this.oAuth2User = oAuth2User;
		this.provider = provider;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return oAuth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return oAuth2User.getAuthorities();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return oAuth2User.getAttribute("name");
	}
	
	public String getEmail() {
		return oAuth2User.<String>getAttribute("email");  
	}
	
	public String getId() {
		return oAuth2User.<String>getAttribute("sub");
	}
	
	public String getImageUrl() {
        return oAuth2User.<String>getAttribute("picture");
    }
	
	public String getProvider() {
		return this.provider;
	}
}
