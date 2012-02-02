package backstage.webcrawler.transport.http;

import backstage.webcrawler.transport.common.TransportException;

public class HTTPTransportException extends TransportException {

    private static final long serialVersionUID = 546574708933803471L;

    public HTTPTransportException(String msg, Throwable t) {
        super(msg, t);
    }
    
    public HTTPTransportException(String msg) {
        super(msg);
    }
}