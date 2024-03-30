package com.blog.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.blog.app.security.CustomUserDetailsService;
import com.blog.app.security.JwtAuthenticationEntryPoint;
import com.blog.app.security.JwtAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableWebMvc
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

//InMemoryAllocation
//	@Bean
//	UserDetailsService userDetailsService(PasswordEncoder encoder) {
//
//		UserDetails user1 = User.withUsername("molua").password("molua").passwordEncoder(p -> encoder.encode(p))
//				.roles("ADMIN").build();
//
//		UserDetails user2 = User.withUsername("moluu").password("moluu").passwordEncoder(p -> encoder.encode(p))
//				.roles("USER").build();
//
//		return new InMemoryUserDetailsManager(user1, user2);
//	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(c -> c.disable()).authorizeHttpRequests((httpz) -> {
			try {
				httpz.requestMatchers(HttpMethod.POST, "api/v1/auth/login").permitAll()
						.requestMatchers(HttpMethod.GET, "api/categories/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "api/categories/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PATCH, "api/categories/**").hasRole("ADMIN").anyRequest()
						.authenticated().and()
						.exceptionHandling(
								handling -> handling.authenticationEntryPoint(this.jwtAuthenticationEntryPoint))
						.sessionManagement(
								management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).httpBasic(Customizer.withDefaults());

		http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
		http.authenticationProvider(daoAuthenticationProviderBean());
		DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();

		return defaultSecurityFilterChain;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authConfig) throws Exception {

		return authConfig.getAuthenticationManager();
	}

	@Bean
	DaoAuthenticationProvider daoAuthenticationProviderBean() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

}
