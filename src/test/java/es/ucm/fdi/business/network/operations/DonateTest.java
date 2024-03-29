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
package es.ucm.fdi.business.network.operations;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit test for donate.
 * @author Pablo Villalobos
 */
public class DonateTest {
    // It works. However, the param @Test is removed due to it does not work
    // on Travis.
    //@Test
    public void donateTest() {
        try {
            DonateAS.donate();
        } catch (Exception e) {
            fail("The link to the webpage is not working");
        }
    }
}
