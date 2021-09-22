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

import nz.net.ultraq.jaxb.adapters.JodaLocalDateAdapter

import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Test

/**
 * Tests for the Joda LocalDate / XML Date adapter.
 * 
 * @author <a href="mailto:david@davidkarlsen.com">David J. M. Karlsen<a>
 */
class JodaLocalDateAdapterTests {

	private JodaLocalDateAdapter jodaLocalDateAdapter

	/**
	 * Test setup.
	 */
	@Before
	void before() {

		this.jodaLocalDateAdapter = new JodaLocalDateAdapter()
	}

	/**
	 * Test the marshalling of a date.
	 */
	@Test
	void testMarshalDate() {

		def localDate = new LocalDate(2013, 8, 21)
		def marshalledValue = jodaLocalDateAdapter.marshal(localDate)
		assert '2013-08-21' == marshalledValue
	}

	/**
	 * Test the marshalling of a null value.
	 */
	@Test
	void testMarshalNull() {

		def marshalledValue = jodaLocalDateAdapter.marshal(null)
		assert marshalledValue == null
	}

	/**
	 * Test the unmarshalling of a local date without a time zone.
	 */
	@Test
	void testUnmarshalNoTz() {

		def localDate = jodaLocalDateAdapter.unmarshal('2013-08-21')
		def expectedDate = new LocalDate(2013, 8, 21)
		assert expectedDate == localDate
	}

	/**
	 * Test the unmarshalling of a null value.
	 */
	@Test
	void testUnmarshalNull() {

		def unmarshalledValue = jodaLocalDateAdapter.unmarshal(null)
		assert unmarshalledValue == null
	}

	/**
	 * Test the unmarshalling of a local date with a time zone.
	 */
	@Test
	void testUnmarshalWithZone() {

		def localDate = jodaLocalDateAdapter.unmarshal('2013-08-21+06:00')
		def expectedDate = new LocalDate(2013, 8, 21)
		assert expectedDate == localDate
	}

	/**
	 * Test the unmarshalling of a local date set to UTC.
	 */
	@Test
	void testUnmarshalUTC() {

		def localDate = jodaLocalDateAdapter.unmarshal('2013-08-21Z')
		def expectedDate = new LocalDate(2013, 8, 21)
		assert expectedDate == localDate
	}
}
