package com.manjul.security.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.manjul.security.oauth.CustomOAuth2User;
import com.manjul.security.oauth.CustomOAuth2UserService;
import com.manjul.security.userDetails.UserDetailsServiceImpl;
import com.manjul.security.oauth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

//So in Spring security 3 @PreAuthorize("hasRole('ROLE_XYZ')")
//is the same as @PreAuthorize("hasAuthority('ROLE_XYZ')")
//and in Spring security 4
//
//in Spring Security 4 expects you to have the ROLE_ prefix whereas the hasAuthority('xyz')
// does not expect the prefix and evaluates exactly what is passed in.
//
//@Secured("User") on class will not work for methods covering with annotation.

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private CustomOAuth2UserService oauthUserService;

	@Autowired
	private UserService userService;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();

	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/login", "/oauth/**").permitAll()
//				.antMatchers("/list").hasAnyAuthority("User", "Admin")
//				.antMatchers("/list").hasAnyRole("User")
			.anyRequest().authenticated()

			.and()
			.formLogin().permitAll()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("pass")

				.defaultSuccessUrl("/list")
//				.and().sessionManagement().maximumSessions(1)
//				.and()
//				.successHandler(new AuthenticationSuccessHandler() {
//
//					@Override
//					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//														Authentication authentication) throws IOException, ServletException {
//						System.out.println("AuthenticationSuccessHandler FORM invoked");
//						System.out.println("Authentication FORM name: " + authentication.getName());
////						UsernamePasswordAuthenticationToken oauthUser = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
//						UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(authentication.getName(), "pass");
//
//						User user = userService.processFORMPostLogin(authentication.getName());
//						if(user != null && user.getId() > 0){
//							response.sendRedirect("/list");
//						}else{
//							try {
//								throw new Exception();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				})
			.and()
			.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
					.userService(oauthUserService)
				.and()
				.successHandler(new AuthenticationSuccessHandler() {

					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						System.out.println("AuthenticationSuccessHandler invoked");
						System.out.println("Authentication name: " + authentication.getName());
						for (GrantedAuthority auth: authentication.getAuthorities()) {
							System.out.println("GrantedAuthority : " + auth.getAuthority());
						}

						CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

						//We are loading Roles for Google/FB during login here
						userService.processOAuthPostLogin(oauthUser.getEmail());

						response.sendRedirect("/list");
					}
				})
//
				.and()
				.logout().logoutSuccessUrl("/").permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/403")


//			.and()
//			.logout().deleteCookies("JSESSIONID").logoutSuccessUrl("/").permitAll()
//			.and()
//			.exceptionHandling().accessDeniedPage("/403")
//
//			.and()
//			.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(3)
			;
	}

//	@Bean
//	@Order(1)
//	public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
//		http
//		.authorizeHttpRequests(authorize -> authorize
//				.antMatchers("/login","/").permitAll()
//				.anyRequest().authenticated()
//		)
//		.formLogin(a->a
////				.permitAll()
//				.loginPage("/login").permitAll()
//				.usernameParameter("email")
//				.passwordParameter("pass")
//				.defaultSuccessUrl("/list"))
//
//		.logout().logoutSuccessUrl("/").permitAll()
//		.and()
//		.exceptionHandling().accessDeniedPage("/403");
//
//		return http.build();
//	}
//
//	@Bean
////	@Order(2)
//	public SecurityFilterChain oauth2LoginFilterChain(HttpSecurity http) throws Exception {
//		http
//		.authorizeHttpRequests(authorize -> authorize
//				.antMatchers("/oauth/**","/login").permitAll()
//				.anyRequest().authenticated()
//		)
//		.oauth2Login(a->a
//				.loginPage("/login")
//				.userInfoEndpoint()
//				.userService(oauthUserService)
//				.and()
//				.successHandler(new AuthenticationSuccessHandler() {
//
//
//					@Override
//					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//														Authentication authentication) throws IOException, ServletException {
//						System.out.println("AuthenticationSuccessHandler invoked");
//						System.out.println("Authentication name: " + authentication.getName());
//						CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//
//						userService.processOAuthPostLogin(oauthUser.getEmail());
//
//						response.sendRedirect("/list");
//					}
//				}))
//
//				.logout().logoutSuccessUrl("/").permitAll()
//				.and()
//				.exceptionHandling().accessDeniedPage("/403");
//
//		return http.build();
//	}


}
