package database_v2.mappers;

import database_v2.models.CRUDModel;
import database_v2.models.relational.CityDBModel;
import java.util.ArrayList;
import models.address.City;
import org.javatuples.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 */
public class CityMapperTest extends AbstractDBMapperTest {
    
    private CityMapper cityMapper;
    
    public CityMapperTest() throws Exception {
        super();
        cityMapper = new CityMapper();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getCity method, of class CityMapper.
     */
    @Test
    public void testGetCity() throws Exception {
        System.out.println("getCity");
        
        // fist we check if it works for a cached city.
        int cityId = 1;
        City cachedCity = new City("cachedCity", "1111", "BE");
        when(cache.getCityIfPresent(cityId)).thenReturn(cachedCity);
        
        City result = cityMapper.getCity(cityId, dac, cache);
        assertEquals(cachedCity, result);
        
        // then test a city not in cache
        cityId = 2;
        
        // mock the response of db
        CityDBModel dbCity = new CityDBModel("dbCity", "1234", "BE");
        dbCity.setCityId(cityId);
        when(cd.read(cityId, CityDBModel.class)).thenReturn(dbCity);
        
        // catch the insert in the cache
        result = cityMapper.getCity(cityId, dac, cache);
        
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<City> cityCaptor = ArgumentCaptor.forClass(City.class);
        verify(cache, times(1)).addCity(idCaptor.capture(), cityCaptor.capture());
        
        assertEquals(cityId, (int) idCaptor.getValue());
        assertEquals(result, cityCaptor.getValue());
        assertEquals(dbCity.getCity(), result.getCityName());
        assertEquals(dbCity.getPostalCode(), result.getPostalCode());
    }

    /**
     * Test of validateCity method, of class CityMapper.
     */
    @Test
    public void testValidateCity() throws Exception {
        System.out.println("validateCity");
        
        // first test a cached city
        int cityId = 1;
        City toValidate = new City("cachedCity", "1111", "BE");
        City cachedCity = new City("cachedCity", "1111", "BE");
        when(cache.getCityIfPresent(cachedCity.getPostalCode(), cachedCity.getCountry()))
                .thenReturn(cachedCity);
        when(cache.getCityIdIfPresent(cachedCity)).thenReturn(cityId);
        
        Pair<Integer, City> result = cityMapper.validateCity(toValidate, dac, cache);
        
        assertEquals(cityId, (int) result.getValue0());
        assertEquals(cachedCity, result.getValue1());
        
        // test a city that is not cached and not in the database
        cityId = 2;
        toValidate = new City("newCity", "1234", "BE");
        
        // no city found, so empty list
        when(cd.simpleSearch(eq(CityDBModel.class), anyList())).thenReturn(new ArrayList<>());
        
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                CRUDModel model = (CRUDModel) args[0];
                model.setId(2);
                return null;
            }
        }).when(cd).create(any(CityDBModel.class));
        
        result = cityMapper.validateCity(toValidate, dac, cache);
                
        verify(cache, times(1)).addCity(anyInt(), any(City.class));
        
        // check the result
        assertEquals(toValidate.getCityName(), result.getValue1().getCityName());
        assertEquals(2, (int) result.getValue0());
        assertEquals(toValidate.getPostalCode(), result.getValue1().getPostalCode());
    }
    
    @Test
    public void testDefaultConstructor() {
        CityMapper instance = new CityMapper();
        assertNotNull(instance);
    }
    
}
