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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Clase para generar un hash a partir de una contraseña
 *
 * @author Pablo Villalobos
 * @see <a href="http://stackoverflow.com/a/2861125/3474">StackOverflow</a>
 */
public class HashGenerator {

    /**
     * Prefix used by every hash created on this class
     */
    public static final String ID = "$31$";

    /**
     * Minimum cost
     */
    public static final int DEFAULT_COST = 16;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    private static final int SIZE = 128;

    private static final Pattern LAYOUT = Pattern.compile("(\\$31\\$)(.{43})");

    private final SecureRandom random = new SecureRandom();

    private final int cost;

    public HashGenerator() {
        cost = DEFAULT_COST;
    }

    public HashGenerator(int cost) {
        this.cost = cost;
    }

    /**
     * Creates a hash from a password with a random salt.
     *
     * @param password
     * @return the safety hash
     */
    public String hash(char[] password) {
        byte[] salt = new byte[SIZE / 8];
        random.nextBytes(salt);
        byte[] dk = pbkdf2(password, salt, 1 << cost);
        byte[] hash = new byte[salt.length + dk.length];
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(dk, 0, hash, salt.length, dk.length);
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        return ID + enc.encodeToString(hash);
    }

    /**
     * Autentifies a password with the token previously saved.
     *
     * @param password
     * @param token
     * @return if the password matches the hash
     */
    public boolean authenticate(char[] password, String token) {
        Matcher m = LAYOUT.matcher(token);
        if (!m.matches()) {
            throw new IllegalArgumentException("Invalid token format");
        }
        byte[] hash = Base64.getUrlDecoder().decode(m.group(2));
        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
        byte[] check = pbkdf2(password, salt, 1 << cost);
        int zero = 0;
        for (int idx = 0; idx < check.length; ++idx) {
            zero |= hash[salt.length + idx] ^ check[idx];
        }
        return zero == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            return f.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
        } catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }

    /**
     * Checks if a string matches the hash layout.
     *
     * @param s The string to check.
     * @return if format is correct
     */
    public static boolean checkFormat(String s) {
        return LAYOUT.matcher(s).matches();
    }
}
