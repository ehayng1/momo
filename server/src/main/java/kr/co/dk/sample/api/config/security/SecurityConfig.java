package kr.co.dk.sample.api.config.security;

import com.google.common.collect.ImmutableList;
import kr.co.dk.sample.api.config.security.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${profile.activate}")
    private String profileActivated;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    SecurityConfig(JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        System.out.println(profileActivated);
        System.out.println(profileActivated);
        System.out.println(profileActivated);

        web.ignoring().antMatchers("/v3/api-docs/**");
        web.ignoring().antMatchers("/swagger-ui/**");
        web.ignoring().antMatchers("/swagger-ui.html");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(false);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Headers"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.headers().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
        http.authorizeRequests()
                .antMatchers("/api/**/**/open/**").permitAll()
                .antMatchers("/api/**/company/**").permitAll()
                .antMatchers("/api/**/site/**").permitAll()
                .antMatchers("/api/**/push/**").permitAll()
                .antMatchers("/api/**/alarm/**").permitAll()
                .antMatchers("/api/**/search/**").permitAll()
                .antMatchers("/api/**/labor/**").permitAll()
                .antMatchers("/api/**/job/**").permitAll()
                .antMatchers("/api/**/equip/**").permitAll()
                .antMatchers("/api/**/like/**").permitAll()
                .antMatchers("/api/**/board/**").permitAll()
                .antMatchers("/api/**/auth/**").permitAll()
                .antMatchers("/api/**/terms/**").permitAll()
                .antMatchers("/api/**/chat/**").permitAll()
                .antMatchers("/api/**/stomp/**").permitAll()
                .antMatchers("/api/**/report/**").permitAll()
                .antMatchers("/pub/**").permitAll()
                .antMatchers("/sub/**").permitAll()
                .antMatchers("/api/**/upload/**").permitAll()
                .antMatchers("/api/**/openapi/**/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public FilterRegistrationBean JwtAuthenticationFilterRegistration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(jwtAuthenticationFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImp);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        };
    }

//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper mapper = converter.getObjectMapper();
//        Hibernate5Module hibernate5Module = new Hibernate5Module();
//        mapper.registerModule(hibernate5Module);
//        mapper.setTimeZone(TimeZone.getDefault());
//        return converter;
//    }

}
