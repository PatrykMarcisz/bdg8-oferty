package com.example.customer_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity      // klasa implementująca metody configure dla http, jdbc, oauth2
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final SecurityConfig passwdEncoder;

    public SecurityConfiguration(DataSource dataSource, SecurityConfig passwdEncoder) {
        this.dataSource = dataSource;
        this.passwdEncoder = passwdEncoder;
    }

    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()        // autoryzuj poniższe żądania http
                .antMatchers("/addTask").hasAnyAuthority("ROLE_ADMIN","ROLE_COMPANY") //mozna uzywac HAS_ANY_ROLE
                .antMatchers("/task*").hasAnyAuthority("ROLE_ADMIN", "ROLE_COMPANY", "ROLE_USER")
                .antMatchers("/delete_task*").hasAnyAuthority("ROLE_ADMIN", "ROLE_COMPANY")
                .antMatchers("/userProfile").hasAnyAuthority("ROLE_ADMIN", "ROLE_COMPANY", "ROLE_USER")
                                                // tu wprowadzamy żądania wymagające logowania
                .anyRequest()
                .permitAll()                    // wszystkie pozostałe żądania nie wymagają autoryzacji
                    .and()
                .csrf().disable()
                .headers()
                    .frameOptions()
                    .disable()
                    .and()
                .formLogin()
                    .loginPage("/login")                // adres zwracający stronę logowania
                        .usernameParameter("email")         // nazwa pola dla loginu -> th:name
                        .passwordParameter("password")      // nazwa pola dla hasła -> th:name
                        .loginProcessingUrl("/login-process") // wskazuje adres gdzie są przekazywane te wartości -> nie trzeba mapować w kontrolerze
                        .defaultSuccessUrl("/") // przekierowanie po poprawnym logowaniu
                        .failureUrl("/login_error")
                        .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
                .usersByUsernameQuery("SELECT u.email, u.password, status FROM user u WHERE u.email = ?") // wyszukanie użytkownika po adresie email
                .authoritiesByUsernameQuery(
                        "SELECT u.email, r.role_name FROM user u JOIN user_role ur ON (u.user_id = ur.user_id) " +
                                "JOIN role r ON (r.role_id = ur.role_id) WHERE u.email = ?")
                .dataSource(dataSource)
                .passwordEncoder(passwdEncoder.getPasswordEncoder());       // matoda zwracająca algorytm BCryptPasswordEncoder
    }
}
