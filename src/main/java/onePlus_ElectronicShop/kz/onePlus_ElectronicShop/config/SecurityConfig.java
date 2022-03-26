package onePlus_ElectronicShop.kz.onePlus_ElectronicShop.config;


import onePlus_ElectronicShop.kz.onePlus_ElectronicShop.services.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true,securedEnabled = true  )
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public AuthUserService userServise;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userServise);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/accesserror");

        http.authorizeRequests()
                .antMatchers("/css/**","/js/**").permitAll();
        http.formLogin().loginProcessingUrl("/toenter").permitAll()
                .loginPage("/signin").permitAll()
                .usernameParameter("user_email")
                .passwordParameter("user_password")
                .defaultSuccessUrl("/profile")
                .failureUrl("/signin?error");

        http.logout()
                .logoutSuccessUrl("/signin")
                .logoutUrl("/toexit");

        http.csrf().disable();


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }


}
