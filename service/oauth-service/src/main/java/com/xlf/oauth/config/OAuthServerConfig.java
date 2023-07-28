package com.xlf.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

@Configuration
@EnableAuthorizationServer // 处理用户身份鉴定
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Resource
    AuthenticationManager authenticationManager;

    //  Client(接入方) ,定义grant type
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client-a")
                .secret("{bcrypt}" + new BCryptPasswordEncoder().encode("XLF"))
                .authorizedGrantTypes("client_credentials", "refresh_token", "password", "authorization_code")
                .scopes("all")
                .authorities("oauth2")
                .redirectUris("http://www.baidu.com");

        // 配置多个client
//                .and().withClient("client-b")
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(new RedisTokenStore(redisConnectionFactory))
                .authenticationManager(authenticationManager);
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST,HttpMethod.DELETE)
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security.allowFormAuthenticationForClients()
                    .tokenKeyAccess("permitAll()")  // 运行所有人访问
                    .checkTokenAccess("permitAll()");
    }

}
