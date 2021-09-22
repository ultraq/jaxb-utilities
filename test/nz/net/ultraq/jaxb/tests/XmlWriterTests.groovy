/*
 * Copyright 2016, Emanuel Rabina (http://www.ultraq.net.nz/)
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

package nz.net.ultraq.jaxb.tests

import nz.net.ultraq.jaxb.XmlWriter

import org.junit.Before
import org.junit.Test

/**
 * Tests for the XML writer.
 * 
 * @author Emanuel Rabina
 */
class XmlWriterTests {

	XmlWriter<XmlTestClass> xmlWriter

	/**
	 * Set up the XML writer.
	 */
	@Before
	void setupXmlWriter() {

		xmlWriter = new XmlWriter<>(XmlTestClass)
	}

	/**
	 * 
	 */
	@Test
	void schemaLocationProperty() {

		xmlWriter.setSchemaLocation('http://test.org/', 'http://test.org/schema.xsd')
	}
}
