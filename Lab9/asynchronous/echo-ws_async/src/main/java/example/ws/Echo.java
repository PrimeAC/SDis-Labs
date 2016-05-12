package example.ws;

import javax.jws.*;

@WebService
public interface Echo {

    String echo(String name) throws EchoException;
    String fastEcho(String name) throws EchoException;

}
