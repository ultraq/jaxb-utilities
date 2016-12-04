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

import org.xml.sax.SAXException

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Unmarshaller
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
	 * @param xmlPack Package containing the Java bindings.
	 * @throws XmlException
	 */
	XmlReader(String xmlPack) throws XmlException {

		try {
			def jaxbcontext = JAXBContext.newInstance(xmlPack, this.class.classLoader)
			unmarshaller = jaxbcontext.createUnmarshaller()
		}
		catch (JAXBException ex) {
			throw new XmlException('An error occurred when creating the unmarshaller', ex)
		}
	}

	/**
	 * Sets-up the XML unmarshaller.
	 * 
	 * @param xmlClasses List of classes to be recognized by the reader.
	 * @throws XmlException
	 */
	XmlReader(Class<?>... xmlClasses) throws XmlException {

		try {
			def jaxbcontext = JAXBContext.newInstance(xmlClasses)
			unmarshaller = jaxbcontext.createUnmarshaller()
		}
		catch (JAXBException ex) {
			throw new XmlException('An error occurred when creating the unmarshaller', ex)
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void clearValidatingSchemasImpl() throws JAXBException {

		unmarshaller.schema       = null
		unmarshaller.eventHandler = null
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input File to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XmlException If an error occurred during unmarshalling, or at
	 * 		   least one schema has been added against this reader and the input
	 * 		   didn't conform to the schema.
	 */
	T readXMLData(File input) throws XmlException {

		try {
			return readXMLData(new BufferedReader(new FileReader(input)))
		}
		catch (FileNotFoundException ex) {
			throw new XmlException('Input file does not exist', ex)
		}
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input Stream to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XmlException If an error occurred during unmarshalling, or at
	 * 		   least one schema has been added against this reader and the input
	 * 		   didn't conform to the schema.
	 */
	T readXMLData(InputStream input) throws XmlException {

		return readXMLData(new InputStreamReader(input))
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input Stream to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XmlException If an error occurred during unmarshalling, or at
	 * 		   least one schema has been added against this reader and the input
	 * 		   didn't conform to the schema.
	 */
	@SuppressWarnings("unchecked")
	T readXMLData(Reader input) throws XmlException {

		try {
			// Combine and apply schemas to the unmarshaller
			if (!schemaBuilt && !sources) {
				unmarshaller.schema       = buildValidatingSchema()
				unmarshaller.eventHandler = new ValidationEventCollector()
				schemaBuilt = true;
			}

			// Unmarshall
			def xmlroot = (T)unmarshaller.unmarshal(input)

			// Check for validation errors (if validating)
			if (unmarshaller.schema) {
				def vec = (ValidationEventCollector)unmarshaller.eventHandler
				if (vec.hasEvents()) {
					def eventList = new StringBuilder()
					vec.events.each { event -> eventList << "${event.message}\n" }
					throw new XmlException(
							"Validation errors were detected in the input: ${eventList.toString().trim()}")
				}
			}

			return xmlroot
		}
		catch (SAXException ex) {
			throw new XmlException('An error occurred when processing the validating schemas', ex)
		}
		catch (JAXBException ex) {
			throw new XmlException('An error occurred during unmarshalling', ex)
		}
	}
}
