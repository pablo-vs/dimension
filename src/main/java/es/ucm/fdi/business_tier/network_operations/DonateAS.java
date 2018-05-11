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
package es.ucm.fdi.business_tier.network_operations;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * This utility class opens an url in the default browser used by the user.
 */
public class DonateAS {

    /**
     * Private constructor to avoid instantiation of this class
     *
     */
    private DonateAS() {
    }

    /**
     * Opens the url of the project webpage.
     */
    public static void donate() {
        openURL("http://dimensionapp.emiweb.es/");
    }

    /**
     * Opens the default browser used by the user. It uses the given url as the
     * default web page.
     *
     * @param url
     */
    private static void openURL(String url) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
