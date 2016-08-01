/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.event;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class SourceTest {
    
    public SourceTest() {
    }
    
    /**
     * Test of getName method, of class Source.
     */
    @Test
    public void testGetName() throws MalformedURLException {
        System.out.println("getName");
        Source instance = new Source(new URL("https://www.google.com"),"google");
        String expResult = "google";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUrl method, of class Source.
     */
    @Test
    public void testGetUrl() throws MalformedURLException {
        System.out.println("getUrl");
        Source instance = new Source(new URL("https://www.google.com"),"google");
        URL expResult = new URL("https://www.google.com");
        URL result = instance.getUrl();
        assertEquals(expResult, result);
    }
    
}
