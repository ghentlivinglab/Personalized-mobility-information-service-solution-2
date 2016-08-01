/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database_v2.models;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hannedesutter
 */
public class CRUDModelTest {
    
    public CRUDModelTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getJoinStatementPart method, of class CRUDModel.
     */
    @Test
    public void testGetJoinStatementPart() {
        System.out.println("getJoinStatementPart");
        CRUDModel instance = new CRUDModelImpl();
        String expResult = "name";
        String result = instance.getJoinStatementPart();
        assertEquals(expResult, result);
    }

    public class CRUDModelImpl implements CRUDModel {

        public List<Attribute> getActiveAttributeList() {
            return null;
        }

        public List<Attribute> getAllAttributeList() {
            return null;
        }

        public String getTableName() {
            return "name";
        }

        public void setId(int id) {
        }

        public int getId() {
            return 0;
        }

        public String getIdColumnName() {
            return "";
        }
    }
    
}
