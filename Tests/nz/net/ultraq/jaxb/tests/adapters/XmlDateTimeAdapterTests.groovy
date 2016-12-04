/* 
 * Copyright 2013, Emanuel Rabina (http://www.ultraq.net.nz/)
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

package nz.net.ultraq.jaxb.tests.adapters

import nz.net.ultraq.jaxb.adapters.XmlDateTimeAdapter

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

/**
 * Tests for the Joda LocalDate / XML Date adapter.
 * 
 * @author <a href="mailto:david@davidkarlsen.com">David J. M. Karlsen<a>
 */
class XmlDateTimeAdapterTests {

	private XmlDateTimeAdapter xmlDateTimeAdapter

	/**
	 * Test setup.
	 */
	@Before
	void before() {

		this.xmlDateTimeAdapter = new XmlDateTimeAdapter()
	}

	/**
	 * Test the marshalling of a date.
	 */
	@Test
	void testMarshalDate() {

		def dateTime = new DateTime(2013, 8, 21, 12, 12, 12, 0, DateTimeZone.forOffsetHours(2))
		def marshalledValue = xmlDateTimeAdapter.marshal(dateTime)
		assertEquals('2013-08-21T12:12:12+02:00', marshalledValue)
	}

	/**
	 * Test the marshalling of a <tt>null</tt> value.
	 */
	@Test
	void testMarshalNull() {

		def marshalledValue = xmlDateTimeAdapter.marshal(null)
		assertNull(marshalledValue)
	}

	/**
	 * Test the unmarshalling of a date without a time zone.
	 */
	@Test
	void testUnmarshalNoTz() {

		def dateTime = xmlDateTimeAdapter.unmarshal('2013-08-21T12:12:12')
		def expectedDateTime = new DateTime(2013, 8, 21, 12, 12, 12, 0)
		assertEquals(expectedDateTime.toString(), dateTime.toString())
	}

	/**
	 * Test the unmarshalling of a <tt>null</tt> value.
	 */
	@Test
	void testUnmarshalNull() {

		def unmarshalledValue = xmlDateTimeAdapter.unmarshal(null)
		assertNull(unmarshalledValue)
	}

	/**
	 * Test the unmarshalling of a date set to UTC.
	 */
	@Test
	void testUnmarshalUTC() {

		def dateTime = xmlDateTimeAdapter.unmarshal('2013-08-21T10:10:10Z')
		def expectedDateTime = new DateTime(2013, 8, 21, 10, 10, 10, 0, DateTimeZone.UTC)
		assertEquals(expectedDateTime.toString(), dateTime.toString())
	}

	/**
	 * Test the unmarshalling of a date with a time zone.
	 */
	@Test
	void testUnmarshalWithZone() {

		def dateTime = xmlDateTimeAdapter.unmarshal('2013-08-21T06:10:08+02:00')
		def expectedDateTime = new DateTime(2013, 8, 21, 6, 10, 8, 0, DateTimeZone.forOffsetHours(2))
		assertEquals(expectedDateTime.toString(), dateTime.toString())
	}
}
