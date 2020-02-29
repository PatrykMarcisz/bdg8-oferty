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
                .antMatchers("/addTask").hasAnyAuthority("ROLE_ADMIN","ROLE_COMPANY")
                .antMatchers("/task*").hasAnyAuthority("ROLE_ADMIN", "ROLE_COMPANY", "ROLE_USER")
                                                // tu wprowadzamy żądania wymagające logowania
                .anyRequest()
                .permitAll()                    // wszystkie pozostałe żądania nie wymagają autoryzacji
                    .and()
                .csrf().disable()
                .formLogin()
                    .loginPage("/login")                // adres zwracający stronę logowania
                    .usernameParameter("email")         // nazwa pola dla loginu -> th:name
                    .passwordParameter("password")      // nazwa pola dla hasła -> th:name
                    .loginProcessingUrl("login-process") // wskazuje adres gdzie są przekazywane te wartości -> nie trzeba mapować w kontrolerze
                    .defaultSuccessUrl("/") // przekierowanie po poprawnym logowaniu
                    .failureUrl("/login_error")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");

    }
}
