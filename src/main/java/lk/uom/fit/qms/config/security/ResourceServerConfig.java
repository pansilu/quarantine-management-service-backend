package lk.uom.fit.qms.config.security;

import lk.uom.fit.qms.util.Constant;
import lk.uom.fit.qms.util.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.config.
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.jwt.sign.key}")
    private String jwtSignKey;

    @Value("${swagger.api.enable:false}")
    private boolean isSwaggerApiEnable;

    private String[] allowedUiPrefixArray = {"/public/**", "/index.html", "/static/**","/assets/**", "/*", "/"};

    private final DefaultUserAuthenticationConverter customUserAuthenticationConverter;

    @Autowired
    public ResourceServerConfig(DefaultUserAuthenticationConverter customUserAuthenticationConverter) {
        this.customUserAuthenticationConverter = customUserAuthenticationConverter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/api/user/authenticate").permitAll()
                .antMatchers("/api/user/quarantine/authenticate").permitAll()
                .antMatchers("/api/graph","/api/graph/**", "/api/location", "/api/location/**", "/api/misc", "/api/misc/**").hasAnyAuthority(RoleType.ROOT.name(), RoleType.ADMIN.name())
                .antMatchers("/api/user/mobile").hasAnyAuthority(RoleType.ROOT.name(), RoleType.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/api/user/quarantine/point", "/api/user/quarantine/submitWelfareReport", "/submitWelfareReport").hasAnyAuthority(RoleType.Q_USER.name(), RoleType.GUARDIAN.name(), RoleType.ADMIN.name(), RoleType.ROOT.name())
                .antMatchers("/api/user/quarantine").hasAnyAuthority(RoleType.ROOT.name(), RoleType.ADMIN.name())
                .antMatchers("/api/user/quarantine/**").hasAnyAuthority(RoleType.ROOT.name(), RoleType.ADMIN.name())
                .antMatchers("/api/user/admin").hasAnyAuthority(RoleType.ROOT.name(), Constant.USER_CREATE_PERMISSION)
                .antMatchers("/api/user/admin/location").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.ROOT.name())
                .antMatchers("/api/user/admin/filter").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.ROOT.name())
                .antMatchers("/api/user/admin/**").hasAnyAuthority(RoleType.ROOT.name(), RoleType.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/api/user/admin/{id}").hasAnyAuthority(Constant.USER_CREATE_PERMISSION);

        http
                .authorizeRequests()
                .antMatchers(allowedUiPrefixArray).permitAll();

        if (isSwaggerApiEnable) {
            http
                    .authorizeRequests()
                    .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll()
                    .antMatchers("/favicon.ico").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/csrf").permitAll();
        }

        http.authorizeRequests().anyRequest().fullyAuthenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {

        config.authenticationEntryPoint(customAuthEntryPoint());
        config.accessDeniedHandler(customAccessDeniedHandler());
        config.tokenServices(resourceServerTokenServices());
    }

    @Bean
    public TokenStore resourceServerTokenStore() {
        return new JwtTokenStore(resourceServerAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter resourceServerAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();

        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);

        jwtAccessTokenConverter.setAccessTokenConverter(accessTokenConverter);
        jwtAccessTokenConverter.setSigningKey(jwtSignKey);
        return jwtAccessTokenConverter;
    }

    @Bean
    public DefaultTokenServices resourceServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(resourceServerTokenStore());
        return defaultTokenServices;
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint customAuthEntryPoint() {
        return new CustomAuthenticationFailureHandler();
    }
}
