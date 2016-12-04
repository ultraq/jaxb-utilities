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

import nz.net.ultraq.jaxb.adapters.XmlLocalDateAdapter

import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

/**
 * Tests for the Joda LocalDate / XML Date adapter.
 * 
 * @author <a href="mailto:david@davidkarlsen.com">David J. M. Karlsen<a>
 */
class XmlLocalDateAdapterTests {

	private XmlLocalDateAdapter xmlLocalDateAdapter

	/**
	 * Test setup.
	 */
	@Before
	void before() {

		this.xmlLocalDateAdapter = new XmlLocalDateAdapter()
	}

	/**
	 * Test the marshalling of a date.
	 */
	@Test
	void testMarshalDate() {

		def localDate = new LocalDate(2013, 8, 21)
		def marshalledValue = xmlLocalDateAdapter.marshal(localDate)
		assertEquals('2013-08-21', marshalledValue)
	}

	/**
	 * Test the marshalling of a null value.
	 */
	@Test
	void testMarshalNull() {

		def marshalledValue = xmlLocalDateAdapter.marshal(null)
		assertNull(marshalledValue)
	}

	/**
	 * Test the unmarshalling of a local date without a time zone.
	 */
	@Test
	void testUnmarshalNoTz() {

		def localDate = xmlLocalDateAdapter.unmarshal('2013-08-21')
		def expectedDate = new LocalDate(2013, 8, 21)
		assertEquals(expectedDate, localDate)
	}

	/**
	 * Test the unmarshalling of a null value.
	 */
	@Test
	void testUnmarshalNull() {

		def unmarshalledValue = xmlLocalDateAdapter.unmarshal(null)
		assertNull(unmarshalledValue)
	}

	/**
	 * Test the unmarshalling of a local date with a time zone.
	 */
	@Test
	void testUnmarshalWithZone() {

		def localDate = xmlLocalDateAdapter.unmarshal('2013-08-21+06:00')
		def expectedDate = new LocalDate(2013, 8, 21)
		assertEquals(expectedDate, localDate)
	}

	/**
	 * Test the unmarshalling of a local date set to UTC.
	 */
	@Test
	void testUnmarshalUTC() {

		def localDate = xmlLocalDateAdapter.unmarshal('2013-08-21Z')
		def expectedDate = new LocalDate(2013, 8, 21)
		assertEquals(expectedDate, localDate)
	}
}
