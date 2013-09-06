
package nz.net.ultraq.jaxb.tests.adapters;

import nz.net.ultraq.jaxb.adapters.XMLDateTimeAdapter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Joda LocalDate / XML Date adapter.
 * 
 * @author <a href="mailto:david@davidkarlsen.com">David J. M. Karlsen<a>
 */
public class XMLDateTimeAdapterTest
{
    private XMLDateTimeAdapter xmlDateTimeAdapter;
    
    @Before
    public void before() {
        this.xmlDateTimeAdapter = new XMLDateTimeAdapter();
    }
    
    @Test
    public void testMarshalDate() {
        DateTime dateTime = new DateTime()
            .withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 )
            .withHourOfDay( 12 ).withMinuteOfHour( 12 ).withSecondOfMinute( 12 )
            .withMillisOfSecond( 0 )
            .withZoneRetainFields( DateTimeZone.forOffsetHours( 2 ) );
        String marshalledValue = xmlDateTimeAdapter.marshal( dateTime );
        Assert.assertEquals( "2013-08-21T12:12:12+02:00", marshalledValue );
    }
    
    @Test
    public void testMarshalNull() {
        String marshalledValue = xmlDateTimeAdapter.marshal( null );
        Assert.assertNull( marshalledValue );
    }
    
    @Test
    public void testUnmarshalNull() {
        DateTime unmarshalledValue = xmlDateTimeAdapter.unmarshal( null );
        Assert.assertNull( unmarshalledValue );
    }
    
    @Test
    public void testUnmarshalNoTz() {
        String dateString = "2013-08-21T12:12:12";
        DateTime dateTime = xmlDateTimeAdapter.unmarshal( dateString );
        DateTime expectedDateTime = new DateTime()
            .withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 )
            .withHourOfDay( 12 ).withMinuteOfHour( 12 ).withSecondOfMinute( 12 ).withMillisOfSecond( 0 );

        Assert.assertEquals( expectedDateTime.toString(), dateTime.toString() );
    }
    
    @Test
    public void testUnmarshalWithZone() {
        String dateString = "2013-08-21T06:10:08+02:00";
        DateTime dateTime = xmlDateTimeAdapter.unmarshal( dateString );
        DateTime expectedDateTime = new DateTime()
            .withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 )
            .withHourOfDay( 6 ).withMinuteOfHour( 10 ).withSecondOfMinute( 8 ).withMillisOfSecond( 0 ).withZoneRetainFields( DateTimeZone.forOffsetHours( 2 ));
        Assert.assertEquals( expectedDateTime.toString(), dateTime.toString() );
    }
    
    @Test
    public void testUnmarshalUTC() {
        String dateString = "2013-08-21T10:10:10Z";
        DateTime dateTime = xmlDateTimeAdapter.unmarshal( dateString );
        DateTime expectedDateTime = new DateTime()
            .withYear( 2013 ).withMonthOfYear( 8 ).withDayOfMonth( 21 )
            .withHourOfDay( 10 ).withMinuteOfHour( 10 ).withSecondOfMinute( 10 ).withMillisOfSecond( 0 ).withZoneRetainFields( DateTimeZone.UTC );
        Assert.assertEquals( expectedDateTime.toString(), dateTime.toString() );
    }
}
