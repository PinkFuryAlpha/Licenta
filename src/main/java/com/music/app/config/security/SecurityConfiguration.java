package com.music.app.config.security;

import com.music.app.config.JwtAuthenticationEntryPoint;
import com.music.app.config.JwtFilter;
import com.music.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String BASIC_USER_ROLE = "BASIC_USER";
    private static final String ARTIST = "ARTIST";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/register", "/users/authenticate", "/users/forgot-password", "/users/reset-password", "/songs", "/users/update-to-artist").permitAll()
                .antMatchers(HttpMethod.GET, "/register/confirm-register", "/songs", "/media/**").permitAll()
                .antMatchers(HttpMethod.GET, "/hello", "/users/save-profile-image").hasAnyRole(ADMIN_ROLE, BASIC_USER_ROLE)
                .antMatchers(HttpMethod.POST, "/songs/**").hasAnyRole(ARTIST)
                .antMatchers(HttpMethod.POST, "/songs/**").hasAnyRole(ADMIN_ROLE, BASIC_USER_ROLE, ARTIST)
                .antMatchers(HttpMethod.GET, "/songs/**").hasAnyRole(ADMIN_ROLE, BASIC_USER_ROLE, ARTIST)
                .antMatchers(HttpMethod.DELETE, "/songs/delete").hasAnyRole(ARTIST)
                .antMatchers(HttpMethod.POST, "/playlist/**").hasAnyRole(ADMIN_ROLE, BASIC_USER_ROLE, ARTIST)
                .antMatchers(HttpMethod.GET, "/playlist/**").hasAnyRole(ADMIN_ROLE, BASIC_USER_ROLE, ARTIST)

                .anyRequest()
                .authenticated()
                .and()
                .formLogin().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);


    }

}
