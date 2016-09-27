package org.mfr.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String context=System.getProperty("context","");
	@Autowired
	private MfrAuthenticationProvider mfrAuthenticationProvider;
	@Autowired
	private MfrSecurityContextStore mfrSessionLoaderFilter;
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(mfrAuthenticationProvider);
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**"); // #3
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.securityContext().securityContextRepository(mfrSessionLoaderFilter).and()
				.authorizeRequests()
				
				//qumla.com
				
				.antMatchers(HttpMethod.GET,"/question").permitAll()
				
				// common rules
				.antMatchers(HttpMethod.POST,"/error-notification").permitAll()
				.antMatchers(HttpMethod.GET,context+"/common/**").permitAll()
				.antMatchers(HttpMethod.GET,context+"/public/**").permitAll()
				.antMatchers(HttpMethod.POST,context+"/common/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT,context+"/common/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE,context+"/common/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/file").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT,"/file").hasRole("ADMIN")
				
				// authentication login and sing
				.antMatchers(context+"/user").hasRole("USER")
				.antMatchers(context+"/token").permitAll()
				.antMatchers(context+"/signup").permitAll()
				
				.antMatchers(HttpMethod.POST,context+"/googlelogin").permitAll()
				.antMatchers(HttpMethod.GET,context+"/oauth2callback").permitAll()
				.antMatchers(HttpMethod.POST,context+"/oauth2tokencheck").permitAll()
				
				.antMatchers(HttpMethod.POST,context+"/facebooklogin").permitAll()
				.antMatchers(HttpMethod.GET,context+"/foauth2callback").permitAll()
				.antMatchers(HttpMethod.POST,context+"/foauth2tokencheck").permitAll()
				
				.antMatchers(context+"/activate/**").permitAll()
				.antMatchers(context+"/uploadprofileimage").hasRole("USER")
				.antMatchers(context+"/profileimage").permitAll()
				.antMatchers(context+"/forgot").permitAll()
				.antMatchers(context+"/forgotChange").permitAll()
				.antMatchers(context+"/changepassword").hasRole("USER")
				
				
				// xpert detail and profession
				.antMatchers(HttpMethod.GET,"/xprtDetail").permitAll()
				.antMatchers(HttpMethod.GET,"/xprtProfession").permitAll()
				
				// comment & rank
				.antMatchers(HttpMethod.GET,"/comment").permitAll()
				.antMatchers(HttpMethod.POST,"/comment").hasRole("USER")
				.antMatchers(HttpMethod.DELETE,"/comment").hasRole("USER")
				
				.antMatchers(HttpMethod.GET,"/rank").permitAll()
				.antMatchers(HttpMethod.POST,"/rank").hasRole("USER")
				.antMatchers(HttpMethod.DELETE,"/rank").hasRole("USER")
				
				// profession
				.antMatchers(HttpMethod.GET,"/profession").permitAll()
				.antMatchers(HttpMethod.POST,"/profession").permitAll()
				.antMatchers(HttpMethod.PUT,"/profession").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE,"/profession").hasRole("ADMIN")
				
				// location data
				.antMatchers(HttpMethod.GET,"/country").permitAll()
				.antMatchers(HttpMethod.GET,"/city").permitAll()
				.antMatchers(HttpMethod.GET,"/ip").permitAll()
				
				// holiday
				.antMatchers(HttpMethod.GET,"/nationalHoliday").permitAll()
				.antMatchers(HttpMethod.GET,"/nationalHoliday/*").permitAll()
				.antMatchers(HttpMethod.PUT,"/nationalHoliday/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST,"/nationalHoliday/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE,"/nationalHoliday/*").hasRole("ADMIN")
				
				// other
				// MAIN FEATURES  
				.antMatchers(context+"/search").permitAll()
				.antMatchers(context+"/offer").permitAll()
				
				// setup
				.antMatchers(HttpMethod.GET,"/setup").permitAll()
				.antMatchers(HttpMethod.PUT,"/setup").hasRole("USER")
				
				// event
				.antMatchers(HttpMethod.GET,"/event").permitAll()
				//
				.antMatchers(HttpMethod.GET,"/language").permitAll()
				
				
				.antMatchers(HttpMethod.POST,"/**").hasRole("USER")
				.antMatchers(HttpMethod.DELETE,"/**").hasRole("USER")
				.antMatchers(HttpMethod.PUT,"/**").hasRole("USER").anyRequest()
				.authenticated();
	}
}