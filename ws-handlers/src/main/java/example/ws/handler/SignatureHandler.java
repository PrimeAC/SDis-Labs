package example.ws.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static javax.xml.bind.DatatypeConverter.printHexBinary;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import example.ws.cli.CaClient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;



/**
 *  This SOAPHandler shows how to set/get values from headers in
 *  inbound/outbound SOAP messages.
 *
 *  A header is created in an outbound message and is read on an
 *  inbound message.
 *
 *  The value that is read from the header
 *  is placed in a SOAP message context property
 *  that can be accessed by other handlers or by the application.
 */
public class SignatureHandler implements SOAPHandler<SOAPMessageContext> {
	
	final static String CERTIFICATE_FILE = "example.cer";
	final static String CA_CERTIFICATE_FILE = "keys/ca/ca-certificate.pem.txt";
	final static String KEYSTORE_FILE = "keystore.jks";
	final static String BROKER_KEYSTORE_FILE = "keys/UpaBroker.jks";
	final static String T1_KEYSTORE_FILE = "keys/UpaTransporter1.jks";
	final static String T2_KEYSTORE_FILE = "keys/UpaTransporter2.jks";
	final static String KEYSTORE_PASSWORD = "ins3cur3";

	final static String KEY_ALIAS = "example";
	final static String KEY_PASSWORD = "1nsecure";
	
	public static String CONTEXT_PROPERTY;
	private Map<String, Certificate> certificates = new HashMap<>();
    
	public static final String REQUEST_HEADER = "myRequestHeader";
	public static final String REQUEST_NS = "urn:example";
	
	private List<String> ids = new ArrayList<>();

    //
    // Handler interface methods
    //
    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        System.out.println("AddHeaderHandler: Handling message.");

        Boolean outboundElement = (Boolean) smc
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        
        try {
            if (outboundElement.booleanValue()) { //Outbound message
            	
            	UUID id = UUID.randomUUID();
            	
            	String propertyValue = CONTEXT_PROPERTY;
            	SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				 
				SOAPBody mb = se.getBody();
				if (mb == null) {
					System.out.println("Body not found.");
					return true;
				}
				
				final byte[] idBytes = id.toString().getBytes();
				
				// make digital signature
                System.out.println("Signing ...");
                byte[] digitalSignature1 = makeDigitalSignature(idBytes, getPrivateKeyFromKeystore(propertyValue+".jks",
                        KEYSTORE_PASSWORD.toCharArray(), propertyValue, KEY_PASSWORD.toCharArray()));
				
                final String messageBody = mb.toString();
                final byte[] plainBytes = messageBody.getBytes();

                System.out.println("Text:");
                System.out.println(messageBody);

                System.out.println("Bytes:");
                System.out.println(printHexBinary(plainBytes));
                
              //add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();
                
             // add header element (name, namespace prefix, namespace)
                Name name1 = se.createName(REQUEST_HEADER, "e", REQUEST_NS);
				SOAPHeaderElement element1 = sh.addHeaderElement(name1);
				element1.addTextNode(propertyValue);
				System.out.println("Escrevi na header "+propertyValue);
                
                // make digital signature
                System.out.println("Signing ...");
                byte[] digitalSignature = makeDigitalSignature(plainBytes, getPrivateKeyFromKeystore(propertyValue+".jks",
                        KEYSTORE_PASSWORD.toCharArray(), propertyValue, KEY_PASSWORD.toCharArray()));

                System.out.println("Signature Bytes:");
                System.out.println(printHexBinary(digitalSignature));

                // add header element (name, namespace prefix, namespace)
                Name name = se.createName("myHeader", "d", "http://demo");
                SOAPHeaderElement element = sh.addHeaderElement(name);
                String valueString = printBase64Binary(digitalSignature);
                element.addTextNode(valueString);
                
                // add header element (name, namespace prefix, namespace)
                Name name2 = se.createName("myid", "a", "http://id");
				SOAPHeaderElement element2 = sh.addHeaderElement(name2);
				element2.addTextNode(id.toString());
				
			
				// add header element (name, namespace prefix, namespace)
                Name name4 = se.createName("myDigId", "r", "http://digId");
                SOAPHeaderElement element4 = sh.addHeaderElement(name4);
                String valueString4 = printBase64Binary(digitalSignature1);
                element4.addTextNode(valueString4);
                
				
            } else { //Inbound message
            	
            	String myValue = CONTEXT_PROPERTY;
            	SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				 
				SOAPBody mb = se.getBody();
				if (mb == null) {
					System.out.println("Body not found.");
					return true;
				}
				
				SOAPHeader sh = se.getHeader();

                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }
                
                Name name = se.createName(REQUEST_HEADER, "e", REQUEST_NS);
				Iterator it = sh.getChildElements(name);
				if (!it.hasNext()) {
					System.out.printf("Header element %s not found.%n", REQUEST_HEADER);
					return true;
				}
				SOAPElement element = (SOAPElement) it.next();

				// get header element value
				String propertyValue = element.getValue();
                
                name = se.createName("myHeader", "d", "http://demo");
                it = sh.getChildElements(name);
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                
                element = (SOAPElement) it.next();
				String valueString = element.getValue();
				
				name = se.createName("myid", "a", "http://id");
                it = sh.getChildElements(name);
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                
				element =(SOAPElement) it.next();
				//get header element value
				String id = element.getValue();
				
				
				Name name4 = se.createName("myDigId", "r", "http://digId");
                it = sh.getChildElements(name4);
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                
                element = (SOAPElement) it.next();
                // get header element value
				String valueString1 = element.getValue();
				
				
				byte[] digitalSignature = parseBase64Binary(valueString);
				
				byte[] digitalSignatureId= parseBase64Binary(valueString1);
				
				final byte[] idBytes =id.getBytes();
				
                final String messageBody = mb.toString();
                final byte[] plainBytes = messageBody.getBytes();

                System.out.println("Text:");
                System.out.println(messageBody);

                System.out.println("Bytes:");
                System.out.println(printHexBinary(plainBytes));
            	//get source certificate and verify it with CA certificate
            	File f = new File(".");
        		System.out.println(f.getAbsolutePath());
        		
        		Certificate certificate;
        		if(certificates.containsKey(propertyValue)){
        			certificate = certificates.get(propertyValue);
        		}
        		else{
        			CaClient ca = new CaClient();
            		System.out.println("passei aqui");
            		System.out.println(ca.sayHello("Ola"));
            		
            		ByteArrayInputStream bis = new ByteArrayInputStream(ca.getCertificates(propertyValue));
            		ObjectInput in = null;
            		in = new ObjectInputStream(bis);
            		certificate = (Certificate) in.readObject();
            		//System.out.println(certificate);
            		KeyStore keystore = readKeystoreFile(myValue + ".jks", KEYSTORE_PASSWORD.toCharArray());
            		Certificate caCertificate = keystore.getCertificate(myValue);
            		PublicKey caPublicKey = caCertificate.getPublicKey();

            		if (verifySignedCertificate(certificate, caPublicKey)) {
            			certificates.put(propertyValue, certificate);
            			System.out.println("The signed certificate is valid");
            		} else {
            			System.err.println("The signed certificate is not valid");
            		}
        		}
        		
                PublicKey publicKey = certificate.getPublicKey();

                // verify the digital signature
                System.out.println("Verifying ...");
               
                boolean isValid = verifyDigitalSignature(digitalSignature, plainBytes, publicKey);
                
                boolean isIdValid = verifyDigitalSignature(digitalSignatureId, idBytes, publicKey);

                if (isValid) {
                    System.out.println("The digital signature is valid");
                } else {
                    System.out.println("The digital signature is NOT valid");
                }
                
                if (isIdValid) {
                    System.out.println("The digital signature ID is valid");
                    if(ids.contains(id)) {
    					return true;
    				}
    				else {
    					ids.add(id);
    				}
                } else {
                    System.out.println("The digital signature ID is NOT valid");
                }
                
            }
        } catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            System.out.println("Continue normal processing...");
        }

        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        System.out.println("Ignoring fault message...");
        return true;
    }

    public void close(MessageContext messageContext) {
    }

    /**
     * Reads a certificate from a file
     * 
     * @return
     * @throws Exception
     */
    public static Certificate readCertificateFile(String certificateFilePath) throws Exception {
        FileInputStream fis;

        try {
            fis = new FileInputStream(certificateFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Certificate file <" + certificateFilePath + "> not found.");
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        if (bis.available() > 0) {
            Certificate cert = cf.generateCertificate(bis);
            return cert;
            // It is possible to print the content of the certificate file:
            // System.out.println(cert.toString());
        }
        bis.close();
        fis.close();
        return null;
    }

    /**
     * Verifica se um certificado foi devidamente assinado pela CA
     * 
     * @param certificate
     *            certificado a ser verificado
     * @param caPublicKey
     *            certificado da CA
     * @return true se foi devidamente assinado
     */
    public static boolean verifySignedCertificate(Certificate certificate, PublicKey caPublicKey) {
        try {
            certificate.verify(caPublicKey);
        } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
                | SignatureException e) {
            // O método Certifecate.verify() não retorna qualquer valor (void).
            // Quando um certificado é inválido, isto é, não foi devidamente
            // assinado pela CA
            // é lançada uma excepção: java.security.SignatureException:
            // Signature does not match.
            // também são lançadas excepções caso o certificado esteja num
            // formato incorrecto ou tenha uma
            // chave inválida.

            return false;
        }
        return true;
    }

    /**
     * Returns the public key from a certificate
     * 
     * @param certificate
     * @return
     */
    public static PublicKey getPublicKeyFromCertificate(Certificate certificate) {
        return certificate.getPublicKey();
    }

    /**
     * Reads a collections of certificates from a file
     * 
     * @return
     * @throws Exception
     */
    public static Collection<Certificate> readCertificateList(String certificateFilePath) throws Exception {
        FileInputStream fis;

        try {
            fis = new FileInputStream(certificateFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
            return null;
        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        @SuppressWarnings("unchecked")
        Collection<Certificate> c = (Collection<Certificate>) cf.generateCertificates(fis);
        fis.close();
        return c;

    }

    /**
     * Reads a PrivateKey from a key-store
     * 
     * @return The PrivateKey
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromKeystore(String keyStoreFilePath, char[] keyStorePassword,
            String keyAlias, char[] keyPassword) throws Exception {

        KeyStore keystore = readKeystoreFile(keyStoreFilePath, keyStorePassword);
        PrivateKey key = (PrivateKey) keystore.getKey(keyAlias, keyPassword);

        return key;
    }

    /**
     * Reads a KeyStore from a file
     * 
     * @return The read KeyStore
     * @throws Exception
     */
    public static KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
        FileInputStream fis;
        try {
            fis = new FileInputStream(keyStoreFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Keystore file <" + keyStoreFilePath + "> not fount.");
            return null;
        }
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(fis, keyStorePassword);
        return keystore;
    }

    /** auxiliary method to calculate digest from text and cipher it */
    public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {

        // get a signature object using the SHA-1 and RSA combo
        // and sign the plain-text with the private key
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initSign(privateKey);
        sig.update(bytes);
        byte[] signature = sig.sign();

        return signature;
    }

    /**
     * auxiliary method to calculate new digest from text and compare it to the
     * to deciphered digest
     */
    public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
            throws Exception {

        // verify the signature with the public key
        Signature sig = Signature.getInstance("SHA1WithRSA");
        sig.initVerify(publicKey);
        sig.update(bytes);
        try {
            return sig.verify(cipherDigest);
        } catch (SignatureException se) {
            System.err.println("Caught exception while verifying signature " + se);
            return false;
        }
    }

}