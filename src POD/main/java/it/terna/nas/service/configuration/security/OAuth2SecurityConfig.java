package it.terna.nas.service.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@Order(1)
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter 
{
	private static final String [] OAUTH2_URI 			= {"/eam/ws/oauth2/WS_EAM_ODL_Consuntivazione_Callback_1.0.wsdl",
															"/eam/ws/oauth2/notificaIndisponibilita.wsdl"};
	
	private static final String PATTERN_URI 			= "/eam/ws/oauth2/**";

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
	
	@Autowired(required = false)
	private RestTemplate oauth2RestTemplate;

    @Autowired
    private BearerTokenAuthFilter bearerTokenAuthFilter;
    
    @Override
	protected void configure(HttpSecurity http) throws Exception
	{
        http.csrf(csrf -> csrf.ignoringAntMatchers(OAUTH2_URI));
        
        if ((!securityByPass) && oAuth2Enable) 
        {
	        
        	http.authorizeHttpRequests().antMatchers(HttpMethod.GET, OAUTH2_URI).permitAll();

        	http.antMatcher(PATTERN_URI)
        		.authorizeRequests(requests -> requests.antMatchers(HttpMethod.POST, OAUTH2_URI)
        		.authenticated()).sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(r -> r.userInfoEndpoint(i -> i.userService(oauth2UserService).oidcUserService(oidcUserService)))
                .oauth2ResourceServer(server -> server.jwt())
                .addFilterAfter(bearerTokenAuthFilter, OAuth2LoginAuthenticationFilter.class);
        	
        } else {
          	http.antMatcher(PATTERN_URI).authorizeRequests(requests -> requests.anyRequest().permitAll());
        }
	}

    
	@Bean
	public JwtDecoder jwtDecoder()
	{
		return NimbusJwtDecoder.withJwkSetUri(azureUri + jwtEndpoint).restOperations(oauth2RestTemplate).build();
	}	
}
