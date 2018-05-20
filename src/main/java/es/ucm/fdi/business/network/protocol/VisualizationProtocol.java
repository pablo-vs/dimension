/*
  This file is part of Dimension.
  Dimension is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  Dimension is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  You should have received a copy of the GNU General Public License
  along with Dimension.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.business.network.protocol;

import es.ucm.fdi.business.exceptions.network.ProtocolException;
import es.ucm.fdi.business.network.messages.client.ClientMessage;
import es.ucm.fdi.business.workspace.project.WorkAS;

/**
 * VisualizationProtocol provides a way of dealing with a visualization
 * request from a clinet. Given a WorkAS (Application Service) a VisualizationProtocol
 * can be invoked using the apply method and providing a ClientMessage. If the
 * ClientMessage matches the type of a request visualization package and it is 
 * correctly filled up, the protocol is applied using the application service.
 * If the visualization can be created, a true statement is returned and the
 * view is generated for the user requesting the visualization. 
 * @see WorkAS
 * @see Protocol 
 * @author Arturo Acuaviva 
 */
public class VisualizationProtocol implements Protocol {

    /**
     * Application Service to manage views in the project
     */
    private final WorkAS manager;
    
    /**
     * Class constructor. A VisualizationProtocol receives an Application
     * Service to manage the working set of the user. 
     * @param manager 
     */
    public VisualizationProtocol(WorkAS manager){
        this.manager = manager;
    }
    
    /**
     * Not implemented yet. 
     * @param msg
     * @return
     * @throws ProtocolException 
     */
    @Override
    public boolean apply(ClientMessage msg) throws ProtocolException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
