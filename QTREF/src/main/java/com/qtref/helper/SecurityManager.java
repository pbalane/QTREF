package com.qtref.helper;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This is used as security manager
 */
@Configuration
public class SecurityManager extends WebSecurityConfigurerAdapter {

    /**
     * This method sets the user name, password and the alloted roles
     *
     * @param auth AuthenticationManagerBuilder Object
     * @throws Exception
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

    }


    /**
     * This method holds the end point and its role.
     *
     * @param http HttpSecurity Object
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/getTransactionList").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/getTransactionListForType**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/getTransactionValueForType**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
