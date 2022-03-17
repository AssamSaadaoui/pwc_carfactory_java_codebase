package be.kdg.java2.carfactory_application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@off
        http
                .httpBasic()
                     .authenticationEntryPoint(httpStatusEntryPoint())
                     .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET,"/", "/cars","/engineers","/register")
                        .permitAll()
                .antMatchers(HttpMethod.POST,"/register").permitAll()
                    .regexMatchers(HttpMethod.GET,".+/\\d+","/imgs/.+",".+sort.+",".+lookup.+")
                        .permitAll()
                    .regexMatchers(HttpMethod.GET, ".+\\.(css|js|map|woff2?|jpg|png|jpeg)(\\?.*)?")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                .and()
                .csrf().and()
                .formLogin().successForwardUrl("/login_success_handler")
                    .loginPage("/login")
                        .permitAll()
                    .and()
                    .logout()
                        .permitAll();
        //@on
//         .antMatchers(HttpMethod.GET, "/q")
//                .permitAll()
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private HttpStatusEntryPoint httpStatusEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }

}
