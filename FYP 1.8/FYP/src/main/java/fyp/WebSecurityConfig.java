
package fyp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public MemberDetailsService memberDetailsService() {
		return new MemberDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(memberDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
          .antMatchers("/categories/edit/*", "/categories/save", "/categories/delete/*")
          .hasRole("ADMIN").antMatchers("/items/add", "/items/edit/*", "/items/save", "/items/delete/*", "/categories/add")
          .hasAnyRole("ADMIN", "SELLER").antMatchers("/member/edit/*", "/member/save", "/member/delete/*")
          .hasRole("ADMIN")
          .antMatchers("/bootstrap/*/*", "/index", "/aboutuspage", "/tnc", "/member/add", "/member/add2",
              "/member/save", "/member/save2", "/images/*", "/uploads/*/*/*", "/forgot_password_form",
              "/forgot_password", "/reset_password_form", "/reset_password", "/faq", 
              "/view_enquiry", "/enquiry/add", "/enquiry/save", "/enquiry/edit/*", "/enquiry/delete/*")
          .permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login")
          .usernameParameter("username").defaultSuccessUrl("/index", true).permitAll().and().rememberMe().and()
          .logout().logoutUrl("/logout").permitAll().and().exceptionHandling().accessDeniedPage("/403");
    }
	
	
}
