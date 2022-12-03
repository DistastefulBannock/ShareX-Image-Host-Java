/**
 * 
 */
package best.yiff.host.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.repo.RepoAccounts;

/**
 * @author DistastefulBannock
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	
	public static String[] ROLES = new String[] {"ROLE_USER", "ROLE_ADMIN"};
	
	@Autowired
	private RepoAccounts repoAccounts;
	
	@Bean
	public UserDetailsService userDetailsService() {
		try {
			if (repoAccounts.findByUsernameIgnoreCase("Admin") == null) {
				ModelAccount admin = new ModelAccount("Admin", "Admin@localhost", passwordEncoder().encode("Admin"), false);
				admin.getRoles().add("ROLE_ADMIN");
				repoAccounts.saveAndFlush(admin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UserDetailsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
		return auth.authenticationProvider(authenticationProvider()).getOrBuild();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(auth -> {
			try {
				auth.antMatchers("/robots.txt", "/css/**", "/assets/**", "/login?**", "/resetPass", 
						"/register", "/register?**", "/pregister", "/", "/api/v1/**").permitAll()
					.anyRequest().authenticated().and()
						.formLogin().loginPage("/login").loginProcessingUrl("/plogin").usernameParameter("email").passwordParameter("password")
						.failureUrl("/login?error=Invalid%20email%20or%20password").defaultSuccessUrl("/user/", true).permitAll()
					.and()
						.rememberMe().tokenValiditySeconds(60*60*24*7)
					.and()
						.logout().logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
					.and()
						.exceptionHandling().accessDeniedPage("/403")
					.and()
						.csrf().ignoringAntMatchers("/api/**");
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Failed to setup security correctly");
			}
		});
		
		return http.build();
	}
	
}
