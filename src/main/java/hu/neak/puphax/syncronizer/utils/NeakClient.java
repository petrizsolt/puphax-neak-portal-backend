package hu.neak.puphax.syncronizer.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.oracle.xmlns.orawsv.puphax.puphaxws.PUPHAXWSPortType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.PUPHAXWSService;

import hu.neak.puphax.syncronizer.mapper.SoapReceiveEncodingUtf8Interceptor;


@Component
public class NeakClient {
	@Value("${server.port}")
	private String serverPort;

	@Value("${application.name:none}")
	private String applicationName;

	private PUPHAXWSPortType savedClient;

	public PUPHAXWSPortType getNeakClient() {
    	if(savedClient != null) {
    		return savedClient;
    	}
		PUPHAXWSService s;

		try {
			URL url;
			if ("none".equalsIgnoreCase(applicationName)) {
				url = new URL("http://localhost:" + serverPort + "/neak/puphax.wsdl");
			} else {
				url = new URL("http://localhost:" + serverPort + "/" + applicationName + "/neak/puphax.wsdl");
			}

			s = new PUPHAXWSService(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Wrong url!");
		}
		PUPHAXWSPortType service = s.getPUPHAXWSPort();
		Client client = ClientProxy.getClient(service);
	
		Map<String, List<String>> headers = new HashMap<>();
		headers.put("Accept-Charset", Arrays.asList("utf-8"));
		headers.put("Content-Type", Arrays.asList("text/xml; charset=utf-8"));

		client.getRequestContext().put(Message.PROTOCOL_HEADERS, headers); 

		client.getInInterceptors().add(new SoapReceiveEncodingUtf8Interceptor());
		
		HTTPConduit conduit = (HTTPConduit) client.getConduit();
		AuthorizationPolicy auth = new AuthorizationPolicy();
		auth.setUserName("PUPHAX");
		auth.setPassword("puphax");
		auth.setAuthorizationType("Digest");
		conduit.setAuthorization(auth);

		savedClient = service;
		return service;
	}


}
