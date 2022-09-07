package com.manjul.security.oauth;

import com.manjul.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomOAuth2User implements OAuth2User {

	private OAuth2User oauth2User;

	@Autowired
	private UserService userService;
	
	public CustomOAuth2User(OAuth2User oauth2User, UserService userService) {
		this.oauth2User = oauth2User;
		this.userService = userService;
	}

//	public CustomOAuth2User(OAuth2User oauth2User) {
//		this.oauth2User = oauth2User;
//	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauth2User.getAttributes();
	}

//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		User user = userService.processOAuthPostLogin(getEmail());
//		Set<Role> roles = user.getRoles();
//		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//		for (Role role: user.getRoles()) {
//			System.out.println("CustomOAuth2User Role: "+role.getName() + " id: "+role.getId());
//			authorities.add(new SimpleGrantedAuthority(role.getName()));
//			System.out.println("CustomOAuth2User SimpleGrantedAuthority: "+new SimpleGrantedAuthority(role.getName()).getAuthority() );
//		}
//
//		return authorities;
//	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		User user = userService.processOAuthPostLogin(getEmail());
		return user.getRoles().stream().map(a->new SimpleGrantedAuthority(a)).collect(Collectors.toList());

//		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//		for (String role: user.getRoles()) {
//			System.out.println("CustomOAuth2User Role: "+role);
////			System.out.println("CustomOAuth2User Role: "+role.getName() + " id: "+role.getId());
//			authorities.add(new SimpleGrantedAuthority(role));
////			System.out.println("CustomOAuth2User SimpleGrantedAuthority: "+new SimpleGrantedAuthority(role.getName()).getAuthority() );
//		}
//
//		return authorities;
	}

	@Override
	public String getName() {	
		return oauth2User.getAttribute("name");
	}

	public String getEmail() {
		return oauth2User.<String>getAttribute("email");		
	}
}
