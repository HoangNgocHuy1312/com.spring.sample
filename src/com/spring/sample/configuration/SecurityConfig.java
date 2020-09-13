package com.spring.sample.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.accept.ContentNegotiationStrategy;

import com.spring.sample.entity.Role;
import com.spring.sample.service.UserService;
import com.spring.sample.util.Constants.Expiration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("accessDeniedHandler")
    private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	@Qualifier("authenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	@Qualifier("authenticationFailureHandler")
    private AuthenticationFailureHandler authenticationFailureHandler;
	
	
//	@Autowired
//    private CustomWebAuthenticationDetailsSource authenticationDetailsSource;
	
//	@Autowired
//    private LogoutSuccessHandler myLogoutSuccessHandler;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	@Qualifier("userService")
    private UserService userService;
	
	@Override
	public void setContentNegotationStrategy(ContentNegotiationStrategy contentNegotiationStrategy) {
		// TODO Auto-generated method stub
		super.setContentNegotationStrategy(contentNegotiationStrategy);
	}
	
//	@Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
	
//	@Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authentication)
//            throws Exception
//    {
//        authentication.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder.encode("admin"))
//                .authorities("ROLE_USER");
//    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests()
	        .antMatchers("/resources/**").permitAll()
	        .antMatchers("/images/**").permitAll()
	        .antMatchers("/webjars/**").permitAll()
	        .antMatchers("/home").permitAll()
	        .antMatchers("/login").permitAll()
	        .antMatchers("/signup").permitAll()
	        .antMatchers(HttpMethod.POST, "/users").permitAll()
	        .antMatchers("/error").permitAll()
	        .antMatchers("/access_denied").permitAll()
	        .antMatchers("/invalid_session").permitAll()
	        .antMatchers(HttpMethod.GET, "/account_activations/**").permitAll()
	        .antMatchers("/password_resets/**").permitAll()
	        .antMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
	        .antMatchers(HttpMethod.GET, "/users").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
	        .anyRequest().permitAll();//.hasAnyAuthority(Role.USER.name(), Role.ADMIN.name());
		http.formLogin()
			.loginPage("/login").failureUrl("/login?error=true&code=0")
			.usernameParameter("email").passwordParameter("password")
			.successHandler(authenticationSuccessHandler)
			.failureHandler(authenticationFailureHandler)
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/")
			.permitAll();
		http.logout()
			.invalidateHttpSession(false)
			.clearAuthentication(true)
			.deleteCookies("JSESSIONID")
	        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	        .logoutSuccessUrl("/login?logout")
	        .permitAll();
		http.rememberMe()
			.rememberMeParameter("remember-me")
			.tokenValiditySeconds(Expiration.REMEMBER_TOKEN_EXPIRY.value)
			.useSecureCookie(true)
	        .tokenRepository(userService)
	        .userDetailsService(userService);
		http.sessionManagement()
        	.invalidSessionUrl("/invalid_session")
        	.maximumSessions(1)
        	.sessionRegistry(sessionRegistry())
        	.and().sessionFixation().none();
		http.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler);
		http.csrf();
		http.headers().contentSecurityPolicy("script-src 'self' https://trustedscripts.example.com; object-src https://trustedplugins.example.com; report-uri /csp-report-endpoint/");
//			http.httpBasic();
	}
	
//	@Bean
//	public AccessDeniedHandler accessDeniedHandler(){
//	    return new CustomAccessDeniedHandler();
//	}
	
//	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
	    return new SimpleUrlAuthenticationSuccessHandler();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/css/**", "/js/**", "/img/**", "/webjars/**", "/images/**");
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}
	
	@Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
	
//	@Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
	
//	@Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(userService);
//        auth.setPasswordEncoder(passwordEncoder);
//        return auth;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
	
}