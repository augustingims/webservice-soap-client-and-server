package com.teamdevsolution.soapclient.config;

import com.teamdevsolution.soapclient.web.IServiceTest;
import org.apache.cxf.clustering.FailoverFeature;
import org.apache.cxf.clustering.RetryStrategy;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

@Configuration
public class WsClientConfig {

    private static final String AUTHORIZATION_TYPE = "Basic";

    @Value("${soap.server.url}")
    private String soapServerUrl;

    @Value("${soap.server.username}")
    private String username;

    @Value("${soap.server.password}")
    private String password;

    @Bean
    @Scope("prototype")
    public JaxWsProxyFactoryBean jaxWsProxyFactoryBean() {
        JaxWsProxyFactoryBean proxyFactoryBean = new JaxWsProxyFactoryBean();

        LoggingFeature logFeature = new LoggingFeature();
        logFeature.setPrettyLogging(true);

        RetryStrategy retryStrategy = new RetryStrategy();
        retryStrategy.setMaxNumberOfRetries(9);
        retryStrategy.setDelayBetweenRetries(100);
        FailoverFeature failoverFeature = new FailoverFeature();
        failoverFeature.setStrategy(retryStrategy);

        proxyFactoryBean.setFeatures(Arrays.asList(logFeature, failoverFeature));

        return proxyFactoryBean;
    }

    @Bean(name = "serviceTestServer")
    public IServiceTest serviceTest(JaxWsProxyFactoryBean jaxWsProxyFactoryBean) {
        jaxWsProxyFactoryBean.setAddress(soapServerUrl);
        jaxWsProxyFactoryBean.setServiceClass(IServiceTest.class);
        IServiceTest serviceTest = jaxWsProxyFactoryBean.create(IServiceTest.class);
        final HTTPConduit httpConduit = (HTTPConduit) ClientProxy.getClient(serviceTest).getConduit();
        httpConduit.getAuthorization().setUserName(username);
        httpConduit.getAuthorization().setPassword(password);
        httpConduit.getAuthorization().setAuthorizationType(AUTHORIZATION_TYPE);
        return serviceTest;
    }

//    @Bean(name = "serviceTestServer")
//    public IServiceTest serviceTestProxy() {
//        JaxWsProxyFactoryBean jaxWsProxyFactoryBean =
//                new JaxWsProxyFactoryBean();
//        jaxWsProxyFactoryBean.setServiceClass(IServiceTest.class);
//        jaxWsProxyFactoryBean.setAddress(soapServerUrl);
//
//        return (IServiceTest) jaxWsProxyFactoryBean.create();
//    }

//    @Bean
//    public Client clientProxy() {
//        //return ClientProxy.getClient(serviceTestProxy());
//        return null;
//    }
//
//    @Bean
//    public HTTPConduit clientConduit() {
//        HTTPConduit httpConduit =
//                (HTTPConduit) clientProxy().getConduit();
//        httpConduit.setAuthorization(basicAuthorization());
//
//        return httpConduit;
//    }
//
//    @Bean
//    public AuthorizationPolicy basicAuthorization() {
//        AuthorizationPolicy authorizationPolicy =
//                new AuthorizationPolicy();
//        authorizationPolicy.setUserName(username);
//        authorizationPolicy.setPassword(password);
//        authorizationPolicy.setAuthorizationType(AUTHORIZATION_TYPE);
//        return authorizationPolicy;
//    }
}
