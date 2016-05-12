
package example.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the example.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FastEcho_QNAME = new QName("http://ws.example/", "fastEcho");
    private final static QName _Echo_QNAME = new QName("http://ws.example/", "echo");
    private final static QName _FastEchoResponse_QNAME = new QName("http://ws.example/", "fastEchoResponse");
    private final static QName _EchoException_QNAME = new QName("http://ws.example/", "EchoException");
    private final static QName _EchoResponse_QNAME = new QName("http://ws.example/", "echoResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: example.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FastEcho }
     * 
     */
    public FastEcho createFastEcho() {
        return new FastEcho();
    }

    /**
     * Create an instance of {@link Echo_Type }
     * 
     */
    public Echo_Type createEcho_Type() {
        return new Echo_Type();
    }

    /**
     * Create an instance of {@link FastEchoResponse }
     * 
     */
    public FastEchoResponse createFastEchoResponse() {
        return new FastEchoResponse();
    }

    /**
     * Create an instance of {@link EchoException }
     * 
     */
    public EchoException createEchoException() {
        return new EchoException();
    }

    /**
     * Create an instance of {@link EchoResponse }
     * 
     */
    public EchoResponse createEchoResponse() {
        return new EchoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FastEcho }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.example/", name = "fastEcho")
    public JAXBElement<FastEcho> createFastEcho(FastEcho value) {
        return new JAXBElement<FastEcho>(_FastEcho_QNAME, FastEcho.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Echo_Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.example/", name = "echo")
    public JAXBElement<Echo_Type> createEcho(Echo_Type value) {
        return new JAXBElement<Echo_Type>(_Echo_QNAME, Echo_Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FastEchoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.example/", name = "fastEchoResponse")
    public JAXBElement<FastEchoResponse> createFastEchoResponse(FastEchoResponse value) {
        return new JAXBElement<FastEchoResponse>(_FastEchoResponse_QNAME, FastEchoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EchoException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.example/", name = "EchoException")
    public JAXBElement<EchoException> createEchoException(EchoException value) {
        return new JAXBElement<EchoException>(_EchoException_QNAME, EchoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EchoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.example/", name = "echoResponse")
    public JAXBElement<EchoResponse> createEchoResponse(EchoResponse value) {
        return new JAXBElement<EchoResponse>(_EchoResponse_QNAME, EchoResponse.class, null, value);
    }

}
