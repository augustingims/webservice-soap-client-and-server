package com.teamdevsolution.soapserver.config;

import com.teamdevsolution.soapserver.interceptor.AuthentificationInterceptor;
import com.teamdevsolution.soapserver.web.impl.IServiceTestImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

import javax.xml.ws.Endpoint;

@Configuration
public class WsConfig {

    @Value("${soap.server.username}")
    private String username;

    @Value("${soap.server.password}")
    private String password;

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        SpringBus springBus = new SpringBus();
        LoggingFeature logFeature = new LoggingFeature();
        logFeature.setPrettyLogging(true);
        logFeature.initialize(springBus);
        springBus.getFeatures().add(logFeature);
        return springBus;
    }

    @Bean
    public Endpoint exposedServiceTest(SpringBus cxf, @Qualifier("serviceTest") IServiceTestImpl iServiceTest) {
        EndpointImpl endpoint = new EndpointImpl(cxf, iServiceTest);
        endpoint.getInInterceptors().add(new AuthentificationInterceptor(username, password));
        endpoint.publish("/IServiceTest");
        return endpoint;
    }
}
