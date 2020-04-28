package com.teamdevsolution.soapclient.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.BindingType;

@WebService(name = "IServiceTest", targetNamespace = "http://localhost:8081/remoting/IServiceTest")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public interface IServiceTest {

    @WebMethod
    public String showName(@WebParam(name = "name") String name);
}
