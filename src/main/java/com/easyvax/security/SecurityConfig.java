package com.easyvax.security;

import com.easyvax.security.filter.CustomAuthenticationFilter;
import com.easyvax.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticatioManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login","/token/refresh/**", "/utente/insertAdmin").permitAll();

        //UTENTE
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/utente/insertUtente").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/utente/updateUtente").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/utente/getDetails").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/centroVaccinale/findAll").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/centroVaccinale/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/centroVaccinale/findByCap").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/centroVaccinale/finByVaccino").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/centroVaccinale/findByProvincia").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/centroVaccinale/findByRegione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/pdf/generate").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/regione/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/provincia/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/provincia/findByCap").hasAnyAuthority("ROLE_USER");
        //http.authorizeRequests().antMatchers(HttpMethod.GET,"api/provincia/findByRegione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/somministrazione/getDetails").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/somministrazione/insertSomministrazione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/somministrazione/updateSomministrazione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/vaccino/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/vaccino/findByCasaFarmaceutica").hasAnyAuthority("ROLE_USER");



        //ADMIN
       // http.authorizeRequests().antMatchers(HttpMethod.POST,"api/utente/insertAdmin").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/utente/updateUtente").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"api/utente/deleteUtente").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/utente/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/centroVaccinale/insertCentro").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/centroVaccinale/updateCentoVaccinale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"api/centroVaccinale/deletecetroVaccinale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/centroVaccinale/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/pdf/generate").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/regione/insertRegione").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/regione/updateRegione").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"api/regione/deleteRegione").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/regione/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/provincia/insertProvincia").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/provincia/updateProvincia").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"api/provincia/deleteProvincia").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/provincia/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/personale/insertPersonale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/personale/updatePersonale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"api/personale/deletePersonale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/personale/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/vaccino/insertVaccino").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/vaccino/updateVaccino").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.DELETE,"api/vaccino/deleteVaccino").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/vaccino/**").hasAnyAuthority("ROLE_ADMIN");




        //PERSONALE
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/utente/updateUtente").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/somministrazione/getDetails").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"api/somministrazione/insertSomministrazione").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(HttpMethod.PUT,"api/somministrazione/updateSomministrazione").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/somministrazione/**").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"api/pdf/generate").hasAnyAuthority("ROLE_PERSONALE");




        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(customAuthenticationFilter);

        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticatioManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
