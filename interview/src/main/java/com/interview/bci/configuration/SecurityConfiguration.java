package com.interview.bci.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHttpMethods(Arrays.asList("HEAD", "DELETE", "POST", "GET", "OPTIONS", "PATCH", "PUT"));
        return firewall;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(
                request -> request.antMatchers(HttpMethod.POST,"/sign-up").permitAll()
        );
    }
}
