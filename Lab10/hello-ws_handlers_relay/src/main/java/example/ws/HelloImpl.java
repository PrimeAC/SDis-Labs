package example.ws;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import example.ws.handler.RelayServerHandler;

/**
 * This is the server class of the Relay example.
 *
 * #6 The server class receives data from the server handler (via message
 * context). #7 The server class passes data to the server handler (via message
 * context).
 *
 * *** GO BACK TO server handler to see what happens next! ***
 */
@WebService(endpointInterface = "example.ws.Hello")
@HandlerChain(file = "/hello_handler-chain.xml")
public class HelloImpl implements Hello {

	public static final String CLASS_NAME = HelloImpl.class.getSimpleName();
	public static final String TOKEN = "server";

	@Resource
	private WebServiceContext webServiceContext;

	public String sayHello(String name) {
		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		// *** #6 ***
		// get token from message context
		String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
		System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, propertyValue);

		// server processing
		String result = String.format("Hello %s!", name);
		System.out.printf("Result: %s%n", result);

		// *** #7 ***
		// put token in message context
		String newValue = propertyValue + "," + TOKEN;
		System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
		messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, newValue);

		return result;
	}

}
