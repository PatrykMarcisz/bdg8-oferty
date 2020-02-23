package com.example.customer_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity      // klasa implementująca metody configure dla http, jdbc, oauth2
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()        // autoryzuj poniższe żądania http
//                .antMatchers("/**").hasAnyRole("ADMIN")
                                                // tu wprowadzamy żądania wymagające logowania
                .anyRequest()
                .permitAll()                    // wszystkie pozostałe żądania nie wymagają autoryzacji
                    .and()
                .formLogin()                    // domyślny formularz logowania
                    .and()
                .httpBasic();                    // domyślna metoda logowania
    }
}
