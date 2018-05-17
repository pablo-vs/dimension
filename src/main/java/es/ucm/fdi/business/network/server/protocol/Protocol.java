
package es.ucm.fdi.business.network.server.protocol;

import es.ucm.fdi.business.exceptions.network.ProtocolException;
import es.ucm.fdi.business.network.server.codes.ClientMessages;


public interface Protocol {
    
    
    boolean apply(ClientMessages msg) throws ProtocolException;
    
}
