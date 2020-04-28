package com.teamdevsolution.soapserver.interceptor;

import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AuthentificationInterceptor extends SoapHeaderInterceptor {

    private final  String username_server;

    private final String password_server;

    public AuthentificationInterceptor(String username, String password) {
        this.username_server = username;
        this.password_server = password;
    }

    @Override
    public void handleMessage(Message m) throws Fault {
        AuthorizationPolicy policy = m.get(AuthorizationPolicy.class);


        // If the policy is not set, the user did not specify credentials.
        // 401 is sent to the client to indicate that authentication is required.
        if (policy == null) {
            sendErrorResponse(m, HttpURLConnection.HTTP_UNAUTHORIZED);
            return;
        }

        String username = policy.getUserName();
        String password = policy.getPassword();

        // CHECK USERNAME AND PASSWORD
        if (!checkLogin(username,password)) {
            sendErrorResponse(m, HttpURLConnection.HTTP_UNAUTHORIZED);
        }
    }

    private boolean checkLogin(String uName, String pWord) {
        return uName.equals(username_server) && pWord.equals(password_server);
    }

    private void sendErrorResponse(Message message, int responseCode) {
        Message outMessage = getOutMessage(message);
        outMessage.put(Message.RESPONSE_CODE, responseCode);

        // Set the response headers
        @SuppressWarnings("unchecked")
        Map<String, List<String>> responseHeaders =  (Map<String, List<String>>)    message.get(Message.PROTOCOL_HEADERS);

        if (responseHeaders != null) {
            responseHeaders.put("WWW-Authenticate", Collections.singletonList("Basic realm=realm"));
            responseHeaders.put("Content-Length", Collections.singletonList("0"));
        }
        message.getInterceptorChain().abort();
        try {
            getConduit(message).prepare(outMessage);
            close(outMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Message getOutMessage(Message inMessage) {
        Exchange exchange = inMessage.getExchange();
        Message outMessage = exchange.getOutMessage();
        if (outMessage == null) {
            Endpoint endpoint = exchange.get(Endpoint.class);
            outMessage = endpoint.getBinding().createMessage();
            exchange.setOutMessage(outMessage);
        }
        outMessage.putAll(inMessage);
        return outMessage;
    }

    private Conduit getConduit(Message inMessage) throws IOException {
        Exchange exchange = inMessage.getExchange();
        Conduit conduit = exchange.getDestination().getBackChannel(inMessage);
        exchange.setConduit(conduit);
        return conduit;
    }

    private void close(Message outMessage) throws IOException {
        OutputStream os = outMessage.getContent(OutputStream.class);
        os.flush();
        os.close();
    }
}
