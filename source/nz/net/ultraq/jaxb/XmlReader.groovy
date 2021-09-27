/* 
 * Copyright 2007, Emanuel Rabina (http://www.ultraq.net.nz/)
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

package nz.net.ultraq.jaxb

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.bind.ValidationException
import javax.xml.bind.util.ValidationEventCollector

/**
 * XML file unmarshaller using JAXB to turn XML files into instances of their
 * JAXB-annotated classes.
 * 
 * @param <T> A JAXB generated class representing the root element of the XML
 * 			  document to be read.
 * @author Emanuel Rabina
 */
class XmlReader<T> extends XmlValidatingProcessor {

	private final Unmarshaller unmarshaller

	/**
	 * Sets-up the XML unmarshaller.
	 * 
	 * @param xmlPackage Package containing the Java bindings.
	 */
	XmlReader(String xmlPackage) {

		def jaxbcontext = JAXBContext.newInstance(xmlPackage, this.class.classLoader)
		unmarshaller = jaxbcontext.createUnmarshaller()
	}

	/**
	 * Sets-up the XML unmarshaller.
	 * 
	 * @param xmlClasses List of classes to be recognized by the reader.
	 */
	XmlReader(Class<?>... xmlClasses) {

		def jaxbcontext = JAXBContext.newInstance(xmlClasses)
		unmarshaller = jaxbcontext.createUnmarshaller()
	}

	@Override
	void clearValidatingSchemas() {

		super.clearValidatingSchemas()
		unmarshaller.schema       = null
		unmarshaller.eventHandler = null
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input File to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XmlValidationException If at least one schema has been added to
	 *         this reader and the input didn't conform to the schema.
	 */
	T read(File input) throws XmlValidationException {

		return read(new BufferedReader(new FileReader(input)))
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input Stream to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XmlValidationException If at least one schema has been added to
	 *         this reader and the input didn't conform to the schema.
	 */
	T read(InputStream input) throws XmlValidationException {

		return read(new InputStreamReader(input))
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input Stream to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XmlValidationException If at least one schema has been added to
	 *         this reader and the input didn't conform to the schema.
	 */
	T read(Reader input) throws XmlValidationException {

		// Combine and apply schemas to the unmarshaller
		if (!schemaBuilt && sources) {
			unmarshaller.schema       = buildValidatingSchema()
			unmarshaller.eventHandler = new ValidationEventCollector()
			schemaBuilt = true
		}

		// Unmarshall
		def xmlRoot = unmarshaller.unmarshal(input)

		// Check for validation errors (if validating)
		if (unmarshaller.schema) {
			def vec = unmarshaller.eventHandler
			if (vec.events) {
				throw new ValidationException(
					"Validation errors were detected in the input: ${vec.events.join(', ')}")
			}
		}

		return xmlRoot
	}
}
