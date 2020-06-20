package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import project.services.UserService;
import project.utils.CustomAccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.cors().and()
			.csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/deny").permitAll()
				.antMatchers(HttpMethod.GET, "/api/rooms/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/equipments/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/types/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/photos/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/events/**").permitAll()
				.antMatchers(HttpMethod.POST, "/api/users/login").permitAll()
				.antMatchers(HttpMethod.POST, "/api/users").permitAll()
				.regexMatchers(HttpMethod.GET, "/api/users\\?.+").permitAll()
				.antMatchers("/api/admin/**").hasAuthority("ADMIN")
				.anyRequest().authenticated()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
				.authenticationEntryPoint(new CustomAccessDeniedHandler());
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
	    return new CustomAccessDeniedHandler();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
