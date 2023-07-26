package qetaa.service.payment.filters;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class LoggingFilter implements ClientRequestFilter{
 
	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
	} 

}
