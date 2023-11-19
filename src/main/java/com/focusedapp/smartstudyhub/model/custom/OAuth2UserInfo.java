package com.focusedapp.smartstudyhub.model.custom;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public abstract class OAuth2UserInfo implements OAuth2User {
	protected OAuth2User oAuth2User;
	protected String provider;
	
	public OAuth2UserInfo(OAuth2User oAuth2User, String provider) {
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

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
    
    public abstract String getProvider();
    
}
