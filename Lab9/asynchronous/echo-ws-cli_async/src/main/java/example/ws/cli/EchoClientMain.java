package example.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import example.ws.*;


public class EchoClientMain {
    static boolean finished = false;

    /**
     * @param args
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void main(String[] args) throws Exception {

        Echo port = new EchoImplService().getEchoImplPort();

        BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();

        if (args.length < 1) {
            // get default endpoint address
            Object url = requestContext.get(ENDPOINT_ADDRESS_PROPERTY);
            System.out.printf("Remote call to %s ...%n", url);

        } else {
            // set endpoint address
            String url = args[0];
            requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
            System.out.printf("Remote call to %s ...%n", url);
        }

        String name = "friend";

        // synchronous call
        try {
            String result = port.echo(name);
            System.out.print("Synchronous call result: ");
            System.out.println(result);
        } catch (Exception e) {
            System.out.print("Caught exception in synchronous call: ");
            System.out.println(e);
        }

        // asynchronous call with polling
        Response<EchoResponse> response = port.echoAsync(name);

        while (!response.isDone()) {
            Thread.sleep(100 /* milliseconds */);

            /* while waiting for response do other calls... */
            String result = port.fastEcho(name);
            System.out.print("Synchronous call result: ");
            System.out.println(result);
        }

        try {
            System.out.println("Asynchronous call result: " + response.get().getReturn());
        } catch (ExecutionException e) {
            System.out.println("Caught execution exception.");
            System.out.print("Cause: ");
            System.out.println(e.getCause());
        }


        // asynchronous call with callback
        port.echoAsync(name, new AsyncHandler<EchoResponse>() {
            @Override
            public void handleResponse(Response<EchoResponse> response) {
                try {
                    System.out.println();
                    System.out.print("Asynchronous call result arrived: ");
                    System.out.println(response.get().getReturn());
                    finished = true;
                } catch (InterruptedException e) {
                    System.out.println("Caught interrupted exception.");
                    System.out.print("Cause: ");
                    System.out.println(e.getCause());
                } catch (ExecutionException e) {
                    System.out.println("Caught execution exception.");
                    System.out.print("Cause: ");
                    System.out.println(e.getCause());
                }
            }
        });

        while (!finished) {
            Thread.sleep(100);
            System.out.print(".");
            System.out.flush();
        }

    }

}
