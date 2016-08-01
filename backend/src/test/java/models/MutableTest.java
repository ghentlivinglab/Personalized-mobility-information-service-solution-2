/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class MutableTest {
    
    public MutableTest() {
    }

    /**
     * Test of getMuteNotifications method, of class Mutable.
     */
    @Test
    public void testGetMuteNotifications() {
        System.out.println("getMuteNotifications");
        Mutable instance = new MutableImpl();
        boolean expResult = false;
        boolean result = instance.isMuted();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMuteNotifications method, of class Mutable.
     */
    @Test
    public void testSetMuteNotifications() {
        System.out.println("setMuteNotifications");
        boolean muteNotifications = true;
        Mutable instance = new MutableImpl();
        instance.setMuted(muteNotifications);
        assertEquals(instance.isMuted(), muteNotifications);
    }

    /**
     * Test of enableNotifications method, of class Mutable.
     */
    @Test
    public void testEnableNotifications() {
        System.out.println("enableNotifications");
        Mutable instance = new MutableImpl();
        instance.mute();
        assertTrue(instance.isMuted());
    }

    /**
     * Test of disableNotification method, of class Mutable.
     */
    @Test
    public void testDisableNotification() {
        System.out.println("disableNotification");
        Mutable instance = new MutableImpl();
        instance.unmute();
        assertFalse(instance.isMuted());
    }

    public class MutableImpl extends Mutable {

        public MutableImpl() {
            super(false);
        }
    }
    
}
