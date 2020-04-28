package com.teamdevsolution.soapclient.web.impl;

import com.teamdevsolution.soapclient.web.IServiceClientTest;
import com.teamdevsolution.soapclient.web.IServiceTest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.WebService;

@WebService(serviceName = "IServiceClientTest", endpointInterface = "com.teamdevsolution.soapclient.web.IServiceClientTest", portName = "serviceClientTestPort")
@Service("exposedserviceTest")
public class IServiceTestImpl implements IServiceClientTest {

    @Resource(name = "serviceTestServer")
    private IServiceTest serviceTest;

    @Override
    public String showName(String name) {
        return serviceTest.showName(name);
    }
}
