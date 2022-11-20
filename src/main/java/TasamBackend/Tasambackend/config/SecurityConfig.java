package TasamBackend.Tasambackend.config;

import TasamBackend.Tasambackend.filter.CustomAuthenticationEntryPoint;
import TasamBackend.Tasambackend.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider){this.jwtTokenProvider = jwtTokenProvider;}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 X
                .and()
                .authorizeRequests()
                .antMatchers("/user/signUp", "/user/signIn","/user/checkUnique").permitAll()  // 이 api를 허락한다
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable() // httpBaic 로그인 방식 끄기
                .formLogin().disable() // 폼 로그인 방식 끄기
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())  //토큰 인증이 실패하거나 인증 헤더를 정상적으로 받지 못했을때 핸들링하는 AuthenticationEntryPoint를 CustomAuthenticationEntryPoint()구현
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다

        return http.build();
    }
}
