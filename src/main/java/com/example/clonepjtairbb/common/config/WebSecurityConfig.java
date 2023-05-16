package com.example.clonepjtairbb.common.config;

import com.example.clonepjtairbb.common.security.JwtAuthenticationFilter;
import com.example.clonepjtairbb.common.security.UserDetailsServiceImpl;
import com.example.clonepjtairbb.common.utils.JwtUtil;
import com.example.clonepjtairbb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // (debug = true) // 스프링 Security 지원을 가능하게 함
//@EnableGlobalMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig{
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // h2-console 사용 및 resources 접근 허용 설정
//        return (web) -> web.ignoring()
////                .requestMatchers(PathRequest.toH2Console())
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
    @Bean
    UserDetailsServiceImpl userDetailsServiceimpl(){
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
//        http.cors().configurationSource(corsConfig.corsConfigurationSource());
        http.csrf().disable();
        //세션 사용 안함
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
//                .antMatchers(HttpMethod.GET, "/api/article").permitAll()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/css/**").permitAll()
//                .antMatchers("/js/**").permitAll()
//                .antMatchers("/images/**").permitAll()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/api/user/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**/*").permitAll()
                .requestMatchers(HttpMethod.GET,"api/stay").permitAll()
                .requestMatchers(HttpMethod.GET, "api/stay/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsServiceimpl()), UsernamePasswordAuthenticationFilter.class);

        // 로그인 사용
//        http.formLogin().loginPage("/api/user/login-page")
//                .successHandler(new LoginSuccessHandler(jwtUtil))
//                .permitAll();
//        http.formLogin().loginProcessingUrl("/api/user/login");

        return http.build();
    }
}
