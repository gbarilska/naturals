package com.natural.configuration;

import com.natural.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/*").hasRole("ADMIN")
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .csrf()
//                .disable()
////                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////                .and()
//                .authorizeRequests()
//                .antMatchers("/index").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
////                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
////                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
//                .exceptionHandling().accessDeniedHandler(getAccessDeniedHandler())
//                .authenticationEntryPoint(authenticationEntryPoint())
//                .and()
//                .logout();
////                .deleteCookies(JWT_COOKIE_NAME);
//    }
//
//    @Bean
//    public AccessDeniedHandler getAccessDeniedHandler() {
//        return new AccessDeniedHandlerImpl();
//    }
//
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
//    }


    @Bean
    public PasswordEncoder setPasswordEncoder() {
//        Don't do this in a real app. Here it's set just because Spring Security expects it
        return NoOpPasswordEncoder.getInstance();
    }
//    @Bean
//    public PasswordEncoder setPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
