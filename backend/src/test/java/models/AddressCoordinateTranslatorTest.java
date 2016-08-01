//package models;
//
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.net.URLConnection;
//import java.net.URL;
//import org.apache.commons.io.IOUtils;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.runner.RunWith;
//import static org.mockito.Matchers.any;
//import org.mockito.Mock;
//import static org.mockito.Mockito.when;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
//
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({AddressCoordinateTranslator.class,IOUtils.class,URL.class})
//public class AddressCoordinateTranslatorTest {
//    
//    private static final double DELTA = 1e-5;
//    
//    private String resultString = "{\n" +
//"   \"results\" : [\n" +
//"      {\n" +
//"         \"address_components\" : [\n" +
//"            {\n" +
//"               \"long_name\" : \"9-29\",\n" +
//"               \"short_name\" : \"9-29\",\n" +
//"               \"types\" : [ \"street_number\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Krijtekerkweg\",\n" +
//"               \"short_name\" : \"Krijtekerkweg\",\n" +
//"               \"types\" : [ \"route\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Gent\",\n" +
//"               \"short_name\" : \"Gent\",\n" +
//"               \"types\" : [ \"locality\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Oost-Vlaanderen\",\n" +
//"               \"short_name\" : \"OV\",\n" +
//"               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Vlaanderen\",\n" +
//"               \"short_name\" : \"Vlaanderen\",\n" +
//"               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Belgium\",\n" +
//"               \"short_name\" : \"BE\",\n" +
//"               \"types\" : [ \"country\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"9041\",\n" +
//"               \"short_name\" : \"9041\",\n" +
//"               \"types\" : [ \"postal_code\" ]\n" +
//"            }\n" +
//"         ],\n" +
//"         \"formatted_address\" : \"Krijtekerkweg 9-29, 9041 Gent, Belgium\",\n" +
//"         \"geometry\" : {\n" +
//"            \"bounds\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.08276739999999,\n" +
//"                  \"lng\" : 3.772690899999999\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 51.0824302,\n" +
//"                  \"lng\" : 3.772554\n" +
//"               }\n" +
//"            },\n" +
//"            \"location\" : {\n" +
//"               \"lat\" : 51.0825658,\n" +
//"               \"lng\" : 3.7726192\n" +
//"            },\n" +
//"            \"location_type\" : \"RANGE_INTERPOLATED\",\n" +
//"            \"viewport\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.08394778029149,\n" +
//"                  \"lng\" : 3.773971430291502\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 51.08124981970849,\n" +
//"                  \"lng\" : 3.771273469708497\n" +
//"               }\n" +
//"            }\n" +
//"         },\n" +
//"         \"place_id\" : \"EiZLcmlqdGVrZXJrd2VnIDktMjksIDkwNDEgR2VudCwgQmVsZ2nDqw\",\n" +
//"         \"types\" : [ \"street_address\" ]\n" +
//"      },\n" +
//"      {\n" +
//"         \"address_components\" : [\n" +
//"            {\n" +
//"               \"long_name\" : \"Ghent\",\n" +
//"               \"short_name\" : \"Ghent\",\n" +
//"               \"types\" : [ \"locality\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"East Flanders\",\n" +
//"               \"short_name\" : \"OV\",\n" +
//"               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Flanders\",\n" +
//"               \"short_name\" : \"Flanders\",\n" +
//"               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Belgium\",\n" +
//"               \"short_name\" : \"BE\",\n" +
//"               \"types\" : [ \"country\", \"political\" ]\n" +
//"            }\n" +
//"         ],\n" +
//"         \"formatted_address\" : \"Ghent, Belgium\",\n" +
//"         \"geometry\" : {\n" +
//"            \"bounds\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.1880501,\n" +
//"                  \"lng\" : 3.8492301\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 50.97683,\n" +
//"                  \"lng\" : 3.57974\n" +
//"               }\n" +
//"            },\n" +
//"            \"location\" : {\n" +
//"               \"lat\" : 51.0543422,\n" +
//"               \"lng\" : 3.7174243\n" +
//"            },\n" +
//"            \"location_type\" : \"APPROXIMATE\",\n" +
//"            \"viewport\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.1880501,\n" +
//"                  \"lng\" : 3.8492301\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 50.97683,\n" +
//"                  \"lng\" : 3.57974\n" +
//"               }\n" +
//"            }\n" +
//"         },\n" +
//"         \"place_id\" : \"ChIJrUOUM-Fww0cRQFFNL6uZAAQ\",\n" +
//"         \"types\" : [ \"locality\", \"political\" ]\n" +
//"      },\n" +
//"      {\n" +
//"         \"address_components\" : [\n" +
//"            {\n" +
//"               \"long_name\" : \"9041\",\n" +
//"               \"short_name\" : \"9041\",\n" +
//"               \"types\" : [ \"postal_code\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Ghent\",\n" +
//"               \"short_name\" : \"Ghent\",\n" +
//"               \"types\" : [ \"locality\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"East Flanders\",\n" +
//"               \"short_name\" : \"OV\",\n" +
//"               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Flanders\",\n" +
//"               \"short_name\" : \"Flanders\",\n" +
//"               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Belgium\",\n" +
//"               \"short_name\" : \"BE\",\n" +
//"               \"types\" : [ \"country\", \"political\" ]\n" +
//"            }\n" +
//"         ],\n" +
//"         \"formatted_address\" : \"9041 Ghent, Belgium\",\n" +
//"         \"geometry\" : {\n" +
//"            \"bounds\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.1087678,\n" +
//"                  \"lng\" : 3.8109729\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 51.0698607,\n" +
//"                  \"lng\" : 3.7524197\n" +
//"               }\n" +
//"            },\n" +
//"            \"location\" : {\n" +
//"               \"lat\" : 51.0924706,\n" +
//"               \"lng\" : 3.7759992\n" +
//"            },\n" +
//"            \"location_type\" : \"APPROXIMATE\",\n" +
//"            \"viewport\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.1087678,\n" +
//"                  \"lng\" : 3.8109729\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 51.0698607,\n" +
//"                  \"lng\" : 3.7524197\n" +
//"               }\n" +
//"            }\n" +
//"         },\n" +
//"         \"place_id\" : \"ChIJq0pjYz13w0cRLuxvpXAXPN0\",\n" +
//"         \"types\" : [ \"postal_code\" ]\n" +
//"      },\n" +
//"      {\n" +
//"         \"address_components\" : [\n" +
//"            {\n" +
//"               \"long_name\" : \"East Flanders\",\n" +
//"               \"short_name\" : \"OV\",\n" +
//"               \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Flanders\",\n" +
//"               \"short_name\" : \"Flanders\",\n" +
//"               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Belgium\",\n" +
//"               \"short_name\" : \"BE\",\n" +
//"               \"types\" : [ \"country\", \"political\" ]\n" +
//"            }\n" +
//"         ],\n" +
//"         \"formatted_address\" : \"East Flanders, Belgium\",\n" +
//"         \"geometry\" : {\n" +
//"            \"bounds\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.35284,\n" +
//"                  \"lng\" : 4.3301\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 50.72094999999999,\n" +
//"                  \"lng\" : 3.3312501\n" +
//"               }\n" +
//"            },\n" +
//"            \"location\" : {\n" +
//"               \"lat\" : 51.0362101,\n" +
//"               \"lng\" : 3.7373124\n" +
//"            },\n" +
//"            \"location_type\" : \"APPROXIMATE\",\n" +
//"            \"viewport\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.35284,\n" +
//"                  \"lng\" : 4.3301\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 50.72094999999999,\n" +
//"                  \"lng\" : 3.3312501\n" +
//"               }\n" +
//"            }\n" +
//"         },\n" +
//"         \"place_id\" : \"ChIJi-0kywZ2w0cRsIBML6uZAAI\",\n" +
//"         \"types\" : [ \"administrative_area_level_2\", \"political\" ]\n" +
//"      },\n" +
//"      {\n" +
//"         \"address_components\" : [\n" +
//"            {\n" +
//"               \"long_name\" : \"Flanders\",\n" +
//"               \"short_name\" : \"Flanders\",\n" +
//"               \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n" +
//"            },\n" +
//"            {\n" +
//"               \"long_name\" : \"Belgium\",\n" +
//"               \"short_name\" : \"BE\",\n" +
//"               \"types\" : [ \"country\", \"political\" ]\n" +
//"            }\n" +
//"         ],\n" +
//"         \"formatted_address\" : \"Flanders, Belgium\",\n" +
//"         \"geometry\" : {\n" +
//"            \"bounds\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.5051,\n" +
//"                  \"lng\" : 5.911010099999999\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 50.68736000000001,\n" +
//"                  \"lng\" : 2.5449401\n" +
//"               }\n" +
//"            },\n" +
//"            \"location\" : {\n" +
//"               \"lat\" : 51.0338547,\n" +
//"               \"lng\" : 4.1904752\n" +
//"            },\n" +
//"            \"location_type\" : \"APPROXIMATE\",\n" +
//"            \"viewport\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.5051,\n" +
//"                  \"lng\" : 5.911010099999999\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 50.68736000000001,\n" +
//"                  \"lng\" : 2.5449401\n" +
//"               }\n" +
//"            }\n" +
//"         },\n" +
//"         \"place_id\" : \"ChIJpeuHbqSSw0cRIIBML6uZAAE\",\n" +
//"         \"types\" : [ \"administrative_area_level_1\", \"political\" ]\n" +
//"      },\n" +
//"      {\n" +
//"         \"address_components\" : [\n" +
//"            {\n" +
//"               \"long_name\" : \"Belgium\",\n" +
//"               \"short_name\" : \"BE\",\n" +
//"               \"types\" : [ \"country\", \"political\" ]\n" +
//"            }\n" +
//"         ],\n" +
//"         \"formatted_address\" : \"Belgium\",\n" +
//"         \"geometry\" : {\n" +
//"            \"bounds\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.5051449,\n" +
//"                  \"lng\" : 6.408124099999999\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 49.497013,\n" +
//"                  \"lng\" : 2.5447948\n" +
//"               }\n" +
//"            },\n" +
//"            \"location\" : {\n" +
//"               \"lat\" : 50.503887,\n" +
//"               \"lng\" : 4.469936\n" +
//"            },\n" +
//"            \"location_type\" : \"APPROXIMATE\",\n" +
//"            \"viewport\" : {\n" +
//"               \"northeast\" : {\n" +
//"                  \"lat\" : 51.5051449,\n" +
//"                  \"lng\" : 6.408124099999999\n" +
//"               },\n" +
//"               \"southwest\" : {\n" +
//"                  \"lat\" : 49.497013,\n" +
//"                  \"lng\" : 2.5447948\n" +
//"               }\n" +
//"            }\n" +
//"         },\n" +
//"         \"place_id\" : \"ChIJl5fz7WR9wUcR8g_mObTy60c\",\n" +
//"         \"types\" : [ \"country\", \"political\" ]\n" +
//"      }\n" +
//"   ],\n" +
//"   \"status\" : \"OK\"\n" +
//"}\n";
//    
//    @Mock
//    private URLConnection connection;
//    
//    @Mock
//    private URL url;
//    
//    @Mock
//    private ByteArrayInputStream input;
//    
//    @Mock 
//    private ByteArrayOutputStream output;
//    
//    public AddressCoordinateTranslatorTest() {
//    }
//    
//    @Before
//    public void setUp() throws Exception {
//        
//        when(url.openConnection()).thenReturn(connection);
//        when(connection.getInputStream()).thenReturn(input);
//        when(output.toString()).thenReturn(resultString);
//        
//    }
//
//    /**
//     * Test of getFormattedAddress method, of class AddressCoordinateTranslator.
//     */
//    @Test
//    public void testGetFormattedAddress() {
//        System.out.println("getFormattedAddress");
//        Coordinate coord = new Coordinate(51.0825658, 3.7726192);
//        String expResult = "Krijtekerkweg 9-29, 9041 Gent, Belgium";
//        String result = AddressCoordinateTranslator.getFormattedAddress(coord);
//        assertEquals(expResult, result);
//    }
//   
//}
