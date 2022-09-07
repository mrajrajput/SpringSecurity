package com.manjul.security.userDetails;

import com.manjul.entity.User;
import com.manjul.repository.UserRolesRepository;
import com.manjul.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRolesRepository userRolesRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}else{
			user.setRoles(userRolesRepository.getRolesByUserId(user.getId()));
		}
		
		return new MyUserDetails(user);
	}
}
