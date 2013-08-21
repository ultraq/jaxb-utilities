package nz.net.ultraq.jaxb.tests.adapters;

import nz.net.ultraq.jaxb.adapters.XMLLocalDateAdapter;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author <a href="mailto:david@davidkarlsen.com">David J. M. Karlsen<a>
 *
 */
public class XMLLocalDateAdapterTest
{
    private XMLLocalDateAdapter xmlLocalDateAdapter;
    
    @Before
    public void before() {
        this.xmlLocalDateAdapter = new XMLLocalDateAdapter();
    }
    
    @Test
    public void testMarshalDate() {
        LocalDate localDate = new LocalDate().withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 );
        String marshalledValue = xmlLocalDateAdapter.marshal( localDate );
        Assert.assertEquals( "2013-08-21", marshalledValue );
    }
    
    @Test
    public void testMarshalNull() {
        String marshalledValue = xmlLocalDateAdapter.marshal( null );
        Assert.assertNull( marshalledValue );
    }
    
    @Test
    public void testUnmarshalNull() {
        LocalDate unmarshalledValue = xmlLocalDateAdapter.unmarshal( null );
        Assert.assertNull( unmarshalledValue );
    }
    
    @Test
    public void testUnmarshalNoTz() {
        String dateString = "2013-08-21";
        LocalDate localDate = xmlLocalDateAdapter.unmarshal( dateString );
        LocalDate expectedDate = new LocalDate().withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 );
        Assert.assertEquals( expectedDate, localDate );
    }
    
    @Test
    public void testUnmarshalWithZone() {
        String dateString = "2013-08-21+06:00";
        LocalDate localDate = xmlLocalDateAdapter.unmarshal( dateString );
        LocalDate expectedDate = new LocalDate().withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 );
        Assert.assertEquals( expectedDate, localDate );
    }
    
    @Test
    public void testUnmarshalUTC() {
        String dateString = "2013-08-21Z";
        LocalDate localDate = xmlLocalDateAdapter.unmarshal( dateString );
        LocalDate expectedDate = new LocalDate().withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 );
        Assert.assertEquals( expectedDate, localDate );
    }
}
