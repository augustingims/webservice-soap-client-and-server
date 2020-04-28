package com.teamdevsolution.soapclient.config;

import com.teamdevsolution.soapclient.web.impl.IServiceTestImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WsConfig {

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
    public Endpoint exposedServiceTest(SpringBus cxf, @Qualifier("exposedserviceTest") IServiceTestImpl serviceTest) {
        EndpointImpl endpoint = new EndpointImpl(cxf, serviceTest);
        endpoint.publish("/IServiceClientTest");
        return endpoint;
    }
}
