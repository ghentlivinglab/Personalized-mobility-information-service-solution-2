/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class JoinableTest {
    
    public JoinableTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getJoinStatementPart method, of class Joinable.
     */
    @Test
    public void testGetJoinStatementPart() {
        System.out.println("getJoinStatementPart");
        Joinable instance = new JoinableImpl();
        String expResult = "bla bla bla";
        String result = instance.getJoinStatementPart();
        assertEquals(expResult, result);
    }

    public class JoinableImpl implements Joinable {

        public String getJoinStatementPart() {
            return "bla bla bla";
        }
    }
    
}
