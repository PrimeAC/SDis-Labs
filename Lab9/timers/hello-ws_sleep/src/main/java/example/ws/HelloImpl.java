package example.ws;

import javax.jws.WebService;

@WebService(endpointInterface="example.ws.Hello")
public class HelloImpl implements Hello {

    public String greeting = "Hello";

    public String sayHello(String name) {
        // print current thread and object instance executing the operation
        System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
        System.out.printf("sayHello(%s)%n", name);

        // sleep
        nap(1);
        // execute operation
        String result = greeting + " " + name + "!";

        System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
        System.out.printf("result=%s%n", result);
        return result;
    }

    private void nap(int seconds) {
        try {
            System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
            System.out.printf("Sleeping for %d seconds...%n", seconds);

            Thread.sleep(seconds*1000);

            System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
            System.out.printf("Woke up!%n");

        } catch(InterruptedException e) {
            System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
            System.out.printf("Caught exception: %s%n", e);
        }
    }

}
