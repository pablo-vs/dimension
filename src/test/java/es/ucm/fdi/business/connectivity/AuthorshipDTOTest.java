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
package es.ucm.fdi.business.connectivity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test for AuthorshipDTO class.
 *
 * @see AuthorshipDTO
 * @author Javier Galiana
 */
public class AuthorshipDTOTest {

    @Test
    public void authorDTOTest() {

        AuthorshipDTO relationShip = new AuthorshipDTO("Tim",
                "continuousFunction");
        AuthorshipDTO relationShip2 = new AuthorshipDTO("Jack",
                "discontinuousFunction");

        assertEquals("These objects do not have the same ID!", false,
                relationShip.equals(relationShip2));

        assertEquals("These objects are not from the same class!", false,
                relationShip.equals(""));

    }
}
