package com.teamdevsolution.soapserver.web.impl;

import com.teamdevsolution.soapserver.web.IServiceTest;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

@WebService(serviceName = "IServiceTest", endpointInterface = "com.teamdevsolution.soapserver.web.IServiceTest", portName = "serviceTestPort")
@Service("serviceTest")
public class IServiceTestImpl implements IServiceTest {
    @Override
    public String showName(String name) {
        return "Data : "+name;
    }
}
