package database_v2.mappers;

import database_v2.DAOLayer.EventDAO;
import database_v2.DAOLayer.impl.CRUDdaoImpl;
import database_v2.controlLayer.impl.DataAccessContextImpl;
import database_v2.controlLayer.impl.DataAccessProviderImpl;
import database_v2.controlLayer.impl.DatabaseCacheImpl;
import database_v2.models.relational.AccountDBModel;
import models.address.City;
import models.users.Password;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 */
public abstract class AbstractDBMapperTest {

    @Mock
    protected DatabaseCacheImpl cache;
    @Mock
    protected DataAccessProviderImpl dap;
    @Mock
    protected DataAccessContextImpl dac;
    @Mock
    protected CRUDdaoImpl cd;
    @Mock
    protected EventDAO ed;

    public AbstractDBMapperTest() throws Exception {
        dap = mock(DataAccessProviderImpl.class);
        dac = mock(DataAccessContextImpl.class);
        cache = mock(DatabaseCacheImpl.class);
        cd = mock(CRUDdaoImpl.class);
        ed = mock(EventDAO.class);

        // set up some basic mock returns
        when(dap.getDataAccessContext()).thenReturn(dac);
        when(dac.getCRUDdao()).thenReturn(cd);
        when(dac.getEventDAO()).thenReturn(ed);
    }
    
    protected AccountDBModel getTestDbAccount() {
        Password pw = new Password("Default123");
        return new AccountDBModel(
                "test@test.com",
                "testVoornaam",
                "testAchternaam",
                pw.getStringPassword(),
                Boolean.FALSE, // mute notifications
                Boolean.TRUE, // email valid
                "REFRESH_TOKEN_STUB",
                pw.getSalt(),
                Boolean.FALSE, // is operator
                Boolean.FALSE, // is admin
                "pushToken"
        );
    }
    
    protected void checkCities(City expected, City got) {
        assertEquals(expected.getCityName(), got.getCityName());
        assertEquals(expected.getCountry(), got.getCountry());
        assertEquals(expected.getPostalCode(), got.getPostalCode());
    }
    
}