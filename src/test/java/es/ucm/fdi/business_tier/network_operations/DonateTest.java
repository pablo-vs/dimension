package es.ucm.fdi.business_tier.network_operations;

import es.ucm.fdi.business_tier.network_operations.DonateAS;
import static org.junit.Assert.*;

import org.junit.Test;

public class DonateTest {

    //@Test
    public void donateTest() {
        try {
            DonateAS.donate();
        } catch (Exception e) {
            fail("The link to the webpage is not working");
        }
    }
}
