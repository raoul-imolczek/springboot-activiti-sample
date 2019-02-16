package com.imolczek.school.banking.activiti;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        
		http
            .authorizeRequests()
            
            	.antMatchers(
            			"/approve/**", 
            			"/deny/**"
            			)
            		.hasRole("agent")
            	
                .antMatchers(
                		"/", 
                		"/js/**", 
                		"/error**", 
                		"/create-lead", 
                		"/submit-customer-information/**", 
                		"/submit-credit-proposition/**", 
                		"/status/**")
                	.permitAll()
                	
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                
            .csrf().disable();
            
    }

}
