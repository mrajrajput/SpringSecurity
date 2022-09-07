#### Add this in command line during startup
```aidl
spring-boot:run -f pom.xml
```

## What actually worked for me?
```
@PreAuthorize("hasAuthority('User')")
where 'User' is the role name in Role table as 'name' column.

Notes:
1) We don't have to add ==> .antMatchers("/list").hasAnyRole("User") <==
in configure method and only addition of role name without(ROLE_) as is
mentioned in DB in Role table.

	@RequestMapping(value = "/list")
	@PreAuthorize("hasAuthority('User')")
	public String viewHomePage(Model model) {
		List<Product> listProducts = service.listAll();
		model.addAttribute("listProducts", listProducts);
		
		return "products";
	}
```

### You need at-least 1 role in Role table to start with.
```
You would need to run these queries - during table creation for fist time.

INSERT INTO role (pk_roles_id, name) VALUES (1, 'User');
INSERT INTO role (pk_roles_id, name) VALUES (2, 'Admin');

INSERT INTO public.users(pk_users_id, enabled, password, provider, username)
VALUES (2, true, 'Password123', 'form', 'test@gmail.com');
	
	
INSERT INTO public.users_roles(pk_users_roles, role_id, user_id)
VALUES (2, 2, 2);
```
## We need to configure
#### 1. AuthenticationManagerBuilder bean
#### 2. HttpSecurity bean
### 1. How to configure 'AuthenticationManagerBuilder'
```
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
}

authenticationProvider: can be DAO/LDAP/OAuth we are using DB
this provider needs 2 things:
1. UserDetailsService
2. PasswordEncoder


For 1)
We override these methods(a & b):

(a)CustomOAuth2UserService.java
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) { }
&& 
(b)UserDetailsServiceImpl.java
    @Override
    public UserDetails loadUserByUsername(String username) { }

&&
We have to load `getAuthorities()` using  UserDetailsServiceImpl for both.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { }

Note: 
For OAuth providers, we have to get information from OAuth first, and later
we can some information from DB(for example Roles), and than we can use that later 
in bean 'oauthUserService' which gets set in userService(here).

.oauth2Login()
.loginPage("/login")
.userInfoEndpoint()
.userService(oauthUserService)

```

### 2. How to configure HttpSecurity
```
@Override
protected void configure(HttpSecurity http) throws Exception { }

```
