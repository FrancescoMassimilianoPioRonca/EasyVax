package com.easyvax.security;


import com.easyvax.security.filter.CustomAuthenticationFilter;
import com.easyvax.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().configurationSource(r -> getCorsConfiguration());
        http.authorizeRequests().antMatchers("/login").permitAll();
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login", "/token/refresh/**", "/utente/insertAdmin").permitAll();
        http.authorizeRequests().antMatchers(POST, "/utente/insertAdmin").permitAll();
        http.
                authorizeRequests()
                .antMatchers("/","/public/**", "/resources/**","/resources/public/**")
                .permitAll();


        //UTENTE
        http.authorizeRequests().antMatchers(POST, "api/utente/insertUtente").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "api/utente/updateUtente").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/utente/getDetails").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/centroVaccinale/findAll").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/centroVaccinale/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/centroVaccinale/findByCap").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/centroVaccinale/finByVaccino").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/centroVaccinale/findByProvincia").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/centroVaccinale/findByRegione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/pdf/generate").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/regione/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/provincia/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/provincia/findByCap").hasAnyAuthority("ROLE_USER");
        //http.authorizeRequests().antMatchers(GET,"api/provincia/findByRegione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/somministrazione/getDetails").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "api/somministrazione/insertSomministrazione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "api/richieste/insertRichieste").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "api/somministrazione/updateSomministrazione").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/vaccino/findByNome").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/vaccino/findByCasaFarmaceutica").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET, "api/richieste/getRichiesteUtente").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "api/richieste/insertRichiesta").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");


        //ADMIN
        // http.authorizeRequests().antMatchers(POST,"api/utente/insertAdmin").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "api/utente/updateUtente").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "api/utente/deleteUtente").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "api/utente/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "api/centroVaccinale/insertCentro").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "api/centroVaccinale/updateCentoVaccinale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "api/centroVaccinale/deletecetroVaccinale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "api/centroVaccinale/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "api/pdf/generate").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "api/regione/insertRegione").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "api/regione/updateRegione").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "api/regione/deleteRegione").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "api/regione/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "api/provincia/insertProvincia").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "api/provincia/updateProvincia").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "api/provincia/deleteProvincia").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "api/provincia/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "api/personale/insertPersonale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "api/personale/updatePersonale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "api/personale/deletePersonale").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "api/personale/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "api/vaccino/insertVaccino").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "api/vaccino/updateVaccino").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "api/vaccino/deleteVaccino").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "api/vaccino/**").hasAnyAuthority("ROLE_ADMIN");



        //PERSONALE
        http.authorizeRequests().antMatchers(PUT, "api/utente/updateUtente").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(GET, "api/somministrazione/getDetails").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(POST, "api/somministrazione/insertSomministrazione").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(PUT, "api/somministrazione/updateSomministrazione").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(GET, "api/somministrazione/**").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(GET, "api/pdf/generate").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(GET, "api/richieste/getRichiestePersonale").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(PUT, "api/richieste/accettaRichiesta").hasAnyAuthority("ROLE_PERSONALE");
        http.authorizeRequests().antMatchers(PUT, "api/richieste/rejectRichiesta").hasAnyAuthority("ROLE_PERSONALE");


        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);

        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * In questo metodo vengono gestiti i CORS, per maggiore informazioni guardare la documentazione
     *
     * @return CorsConfiguration
     */
    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type", "Accept", "Authorization"
                , "Origin, Accept", "X-Requesed-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"
                , "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        return corsConfiguration;
    }

}
