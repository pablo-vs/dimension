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
package es.ucm.fdi.business.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class HashTest {

    @Test
    public void authenticateTest() {
        HashGenerator hashgen = new HashGenerator();
        char[] pass = "1234".toCharArray();
        assertTrue(hashgen.authenticate(pass, hashgen.hash(pass)));
    }
}
