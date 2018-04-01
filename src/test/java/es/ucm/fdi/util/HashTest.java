package es.ucm.fdi.util;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

public class HashTest {

	@Test
	public void authenticateTest() {
		HashGenerator hashgen = new HashGenerator();
		char[] pass = "1234".toCharArray();
		String hash = hashgen.hash(pass);
		assertTrue(hashgen.authenticate(pass, hash));
	}
}
