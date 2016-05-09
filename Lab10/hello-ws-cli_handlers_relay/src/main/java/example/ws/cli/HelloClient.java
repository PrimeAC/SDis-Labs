package example.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import example.ws.Hello;
import example.ws.HelloImplService;
import example.ws.handler.RelayClientHandler;


/**
 * This is the client class of the Relay example.
 *
 * The purpose of the example program is to relay a data token through all web
 * service processing levels: applications classes and handlers.
 *
 * Each one adds something to the token. The final token value should be:
 * client,client-handler,server-handler,server,server-handler,client-handler
 *
 * The property and header names are defined as constants in the handler
 * classes.
 *
 * *** Relay starts HERE! Follow the number sequence! ***
 *
 * #1 The initial data is passed from the client to the client handler (via
 * request message context)
 *
 * *** GO TO client handler to see what happens next! ***
 *
 * #12 The final data is received by the client from the client handler (via
 * response message context).
 *
 * *** THE END ***
 */
public class HelloClient {

	public static final String CLASS_NAME = HelloClient.class.getSimpleName();
	public static final String TOKEN = "client";

	public static void main(String[] args) {
		// Check arguments
		if (args.length < 1) {
			System.out.println("Argument(s) missing!");
			System.out.printf("Usage: java %s url%n", HelloClient.class.getName());
			return;
		}

		String url = args[0];

		// create stub
		HelloImplService service = new HelloImplService();
		Hello port = service.getHelloImplPort();

		// access request context
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();

		// *** #1 ***
		// put token in request context
		String initialValue = TOKEN;
		System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
		requestContext.put(RelayClientHandler.REQUEST_PROPERTY, initialValue);

		// set endpoint address
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

		// make remote call
		System.out.printf("Remote call to %s ...%n", url);
		String result = port.sayHello("friend");
		System.out.printf("Result: %s%n", result);

		// access response context
		Map<String, Object> responseContext = bindingProvider.getResponseContext();

		// *** #12 ***
		// get token from response context
		String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
		System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue);
	}

}
