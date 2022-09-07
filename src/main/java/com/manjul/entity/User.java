package com.manjul.entity;

import com.manjul.enums.Provider;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "pkUsersId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String username;
	private String password;
	private boolean enabled;
	
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Transient
	private Set<String> roles = new HashSet<>();

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//	private Set<Role> roles = new HashSet<>();
//
//	public Set<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}




//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(
//			name = "users_roles",
//			joinColumns = @JoinColumn(name = "user_id"),
//			inverseJoinColumns = @JoinColumn(name = "role_id")
//			)
//	private Set<Role> roles = new HashSet<>();


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

//	public Set<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	
}
