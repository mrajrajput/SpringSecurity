package com.manjul.security.oauth;

import com.manjul.entity.User;
import com.manjul.entity.UsersRoles;
import com.manjul.enums.Provider;
import com.manjul.enums.Role;
import com.manjul.repository.RoleRepository;
import com.manjul.repository.UserRepository;
import com.manjul.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private UserRolesRepository userRolesRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	public User processOAuthPostLogin(String username) {
		User existUser = repo.getUserByUsername(username);
		
		if (existUser == null) {

			User newUser = new User();
			newUser.setUsername(username);
			newUser.setProvider(Provider.GOOGLE);
			newUser.setEnabled(true);
			repo.save(newUser);
			
			System.out.println("Created new user: " + username);

			UsersRoles usersRoles = new UsersRoles();
			int id = roleRepository.getRoleIdByRoleName(Role.USER.toString());
			usersRoles.setRole_id(id);
			usersRoles.setUser_id(newUser.getId());
			userRolesRepository.save(usersRoles);

			existUser = newUser;
			existUser.setRoles(Collections.singleton(Role.USER.toString()));
		}else{
			Set<String> roleNames = new HashSet<>();
			Set<String> roleIds = userRolesRepository.getRolesByUserId(existUser.getId());
			for (String roleId: roleIds) {
				roleNames.add(roleRepository.getRoleNameByRoleId(Integer.parseInt(roleId)));
			}
			existUser.setRoles(roleNames);
		}
		return existUser;
	}
}
