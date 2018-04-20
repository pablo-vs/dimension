package es.ucm.fdi.operations;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DonateAS {
	
	public static void donate() {
		enlace("http://dimensionapp.emiweb.es/");
	}
	
	/**
     * Abre el enlace pasado por parametro
     * @param enlaceAAceder Enlace a abrir
     */
    private static void enlace (String enlaceAAceder){
        Desktop enlace=Desktop.getDesktop();
        try {
                enlace.browse(new URI(enlaceAAceder));
        } catch (IOException | URISyntaxException e) {
            e.getMessage();
        }
    }

}
