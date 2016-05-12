package example.ws;

import javax.jws.*;

@WebService(endpointInterface="example.ws.Echo")
public class EchoImpl implements Echo {

    public String echo(String name) throws EchoException {
        // print current thread and object instance executing the operation
        System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
        System.out.printf("echo(%s)%n", name);

        // simulate a complicated task that takes some time...
        nap(3);

        if (name.length() == 0) {
            throw new EchoException("Name is too short!");
        } else {
            return "Echo " + name;
        }
    }

    public String fastEcho(String name) throws EchoException {
        // print current thread and object instance executing the operation
        System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
        System.out.printf("fastEcho(%s)%n", name);

        if (name.length() == 0) {
            throw new EchoException("Name is too short!");
        } else {
            return "Fast echo " + name;
        }
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
