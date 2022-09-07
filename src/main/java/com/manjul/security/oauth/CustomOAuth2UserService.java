package com.manjul.security.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {

	@Autowired
	private UserService userService;

	/*
		I'm not sure - when it will get called and who is calling it.
		Delete it, if not getting used.
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user =  super.loadUser(userRequest);
		for (GrantedAuthority auth: user.getAuthorities()) {
			System.out.println("CustomOAuth2UserService.GrantedAuthority : " + auth.getAuthority());
		}
		System.out.println("CustomOAuth2UserService invoked");
		return new CustomOAuth2User(user, userService);
	}

}
