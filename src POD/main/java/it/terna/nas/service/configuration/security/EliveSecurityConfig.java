package it.terna.nas.service.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(2)
public class EliveSecurityConfig extends WebSecurityConfigurerAdapter 
{
    private static final String PATTERN_URI = "/eam/elive/**";

    @Value("${http.settings.azureUri}")
    private String azureUri;

    @Value("${http.settings.jwtEndpoint}")
    private String jwtEndpoint;
    
    @Value("${http.settings.securityByPass:true}")
    private boolean securityByPass;
    
    @Value("${http.settings.oAuth2Enable:false}")
    private boolean oAuth2Enable;
    
    @Autowired(required = false)
    private DefaultOAuth2UserService oauth2UserService;
    
    @Autowired(required = false)
    private OidcUserService oidcUserService;

    @Autowired
    private BearerTokenAuthFilter bearerTokenAuthFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.ignoringAntMatchers(PATTERN_URI));
        
        if ((!securityByPass) && oAuth2Enable) 
        {
            http.antMatcher(PATTERN_URI)
                .authorizeRequests(requests -> requests.anyRequest().authenticated())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(r -> r.userInfoEndpoint(i -> i.userService(oauth2UserService).oidcUserService(oidcUserService)))
                .oauth2ResourceServer(server -> server.jwt())                
                .addFilterAfter(bearerTokenAuthFilter, OAuth2LoginAuthenticationFilter.class);
        	
        } else {
            http.antMatcher(PATTERN_URI).authorizeRequests(requests -> requests.anyRequest().permitAll());
        }
    }   
}
