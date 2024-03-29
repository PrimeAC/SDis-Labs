package example.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

// classes generated by wsimport from WSDL
import example.ws.Hello;
import example.ws.HelloImplService;

public class HelloClient {

	public static void main(String[] args) {
		// create stub
		HelloImplService service = new HelloImplService();
		Hello port = service.getHelloImplPort();

		// get/set endpoint address
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();

		Object url = null;
		if (args.length < 1) {
			url = requestContext.get(ENDPOINT_ADDRESS_PROPERTY);
		} else {
			url = args[0];
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
		}
		System.out.printf("Remote call to %s ...%n", url);

		String result = port.sayHello("friend");
		System.out.println(result);
	}

}
