
package nz.net.ultraq.jaxb.adapters;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML Date/Time adapter to convert between XML DateTime format and the Joda
 * DateTime object.
 * 
 * @author Emanuel Rabina
 */
public class XMLDateTimeAdapter extends XmlAdapter<String, DateTime> {

	/**
	 * Converts any ISO8601 date/time string into a Joda DateTime object.
	 * 
	 * @param value
	 * @return Joda DateTime.
	 */
	@Override
    public DateTime unmarshal(String value) {

		return ISODateTimeFormat.dateTimeParser().parseDateTime(value);
    }

	/**
	 * Converts a Joda DateTime to an XML/ISO8601 date/time string.
	 * 
	 * @param value
	 * @return XML date/time string.
	 */
	@Override
    public String marshal(DateTime value) {

		return ISODateTimeFormat.dateTimeParser().print(value);
    }
}
