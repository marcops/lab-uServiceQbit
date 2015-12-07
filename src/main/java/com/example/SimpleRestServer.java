package com.example;

import io.advantageous.qbit.http.HttpTransport;
import io.advantageous.qbit.http.request.HttpBinaryResponse;
import io.advantageous.qbit.http.request.HttpRequest;
import io.advantageous.qbit.http.request.HttpResponseBuilder;
import io.advantageous.qbit.http.request.HttpTextResponse;
import io.advantageous.qbit.server.EndpointServerBuilder;
import io.advantageous.qbit.server.ServiceEndpointServer;
import io.advantageous.qbit.system.QBitSystemManager;
import io.advantageous.qbit.util.MultiMap;
import io.advantageous.qbit.http.request.HttpResponseDecorator;
import io.advantageous.qbit.http.server.HttpServerBuilder;
import static io.advantageous.qbit.server.EndpointServerBuilder.endpointServerBuilder;

/**
 * Created by Marco on 12/04/15.
 */
public class SimpleRestServer {
	
	private static boolean checkAuth(final HttpRequest httpRequest) {

        /* Only check uri's that start with /root/hello. */
        if (httpRequest.getUri().startsWith("/root/hello")) {

            final String x_security_token = httpRequest.headers().getFirst("X-SECURITY-TOKEN");

            /* If the security token is set to "shibboleth" then continue processing the request. */
            if ("shibboleth".equals(x_security_token)) {
                return true;
            } else {
                /* Security token was not what we expected so send a 401 auth failed. */
                httpRequest.getReceiver().response(401, "application/json", "\"shove off\"");
                return false;
            }
        }
        return true;
    }
	
	public static void main(String... args) throws Exception {
		
	    QBitSystemManager systemManager = new QBitSystemManager();

	    EndpointServerBuilder  endpointServerBuilder= endpointServerBuilder().setSystemManager(systemManager).setPort(6060);
        
                
        
		final ServiceEndpointServer serviceEndpointServer;
		//.build()
		serviceEndpointServer = endpointServerBuilder.build();
		
		
		 final HttpTransport httpServerBuilder = endpointServerBuilder.getHttpServer();
		 
		 
		 httpServerBuilder.setShouldContinueHttpRequest(SimpleRestServer::checkAuth);
		 
		 
		 final HttpServerBuilder httpServerBuilder2 = endpointServerBuilder.getHttpServerBuilder();	        
		 httpServerBuilder2.addResponseDecorator(new HttpResponseDecorator() {
			
			@Override
			public boolean decorateTextResponse(HttpTextResponse[] responseHolder,
					String requestPath, int code, String contentType, String payload,
					MultiMap<String, String> responseHeaders,
					MultiMap<String, String> requestHeaders,
					MultiMap<String, String> requestParams) {

				
				responseHolder[0] = (HttpTextResponse) HttpResponseBuilder.httpResponseBuilder().addHeader("foo", "*").build();
		                 
				return true;
			}
			
			@Override
			public boolean decorateBinaryResponse(HttpBinaryResponse[] responseHolder,
					String requestPath, int code, String contentType, byte[] payload,
					MultiMap<String, String> responseHeaders,
					MultiMap<String, String> requestHeaders,
					MultiMap<String, String> requestParams) {
				// TODO Auto-generated method stub
				return false;
			}
		} );
		 
		/*  HttpServerBuilder httpServerBuilder = endpointServerBuilder.getHttpServerBuilder();
		  
		          httpServerBuilder.addResponseDecorator(new HttpResponseDecorator() {
		              @Override
		              public boolean decorateTextResponse(HttpTextResponse[] responseHolder, String requestPath,
		                                                  int code, String contentType, String payload,
		                                                  MultiMap<String, String> responseHeaders,
		                                                  MultiMap<String, String> requestHeaders,
		                                                  MultiMap<String, String> requestParams) {
		  
		                  responseHolder[0] = (HttpTextResponse) HttpResponseBuilder.httpResponseBuilder()
		                          .setCode(999).setContentType("foo/bar").addHeader("foo", "bar").setBody("DECORATED" + payload).build();
		                  return true;
		                  } );
		  
		  */
		serviceEndpointServer.initServices(new MyService());
		serviceEndpointServer.startServer();
		System.out.println("servidor iniciado na porta 6060" );
		// Sys.sleep(1_000_000_000);
	}
}
