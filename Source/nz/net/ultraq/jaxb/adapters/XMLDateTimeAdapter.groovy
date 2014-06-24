/*
 * Copyright 2012, Emanuel Rabina (http://www.ultraq.net.nz/)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.net.ultraq.jaxb.adapters

import org.joda.time.DateTime

import javax.xml.bind.DatatypeConverter
import javax.xml.bind.annotation.adapters.XmlAdapter

/**
 * XML Date/Time adapter to convert between XML DateTime format and the Joda DateTime object.
 * 
 * @author Emanuel Rabina
 * @author <a href="mailto:david@davidkarlsen.com">David J. M. Karlsen<a>
 */
class XMLDateTimeAdapter extends XmlAdapter<String, DateTime> {

	/**
	 * Converts a Joda DateTime to an XML/ISO8601 date/time string.
	 * 
	 * @param value
	 * @return XML date/time string.
	 */
	@Override
	String marshal(DateTime value) {

		return value != null ? DatatypeConverter.printDateTime(value.toGregorianCalendar()) : null
	}

	/**
	 * Converts any ISO8601 date/time string into a Joda DateTime object.
	 * 
	 * @param value
	 * @return Joda DateTime.
	 */
	@Override
	DateTime unmarshal(String value) {

		return value != null ? new DateTime(DatatypeConverter.parseDateTime(value)) : null
	}
}
