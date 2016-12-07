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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.util.ValidationEventCollector

/**
 * XML file marshaller using JAXB to turn JAXB-annotated classes into XML files.
 * 
 * @param <T> A JAXB generated class representing the root element of the XML
 * 			  document to be written.
 * @author Emanuel Rabina
 */
class XmlWriter<T> extends XmlValidatingProcessor {

	private static final String CHARACTER_ESCAPE_HANDLER = 'com.sun.xml.bind.marshaller.CharacterEscapeHandler'
	private static final String NAMESPACE_PREFIX_MAPPER  = 'com.sun.xml.bind.namespacePrefixMapper'

	private final Marshaller marshaller

	/**
	 * Sets-up the XML marshaller.
	 * 
	 * @param xmlPackage Name of the package containing JAXB-generated classes.
	 */
	XmlWriter(String xmlPackage) {

		def jaxbcontext = JAXBContext.newInstance(xmlPackage)
		marshaller = jaxbcontext.createMarshaller()
	}

	/**
	 * Sets-up the XML marshaller.
	 * 
	 * @param xmlClasses List of classes that the reader needs to recognize.
	 */
	XmlWriter(Class<?>... xmlClasses) {

		def jaxbcontext = JAXBContext.newInstance(xmlClasses)
		marshaller = jaxbcontext.createMarshaller()
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void clearValidatingSchemas() {

		super.clearValidatingSchemas()
		marshaller.schema       = null
		marshaller.eventHandler = null
	}

	/**
	 * Set whether the resulting XML file will be formatted 'nicely'.
	 * 
	 * @param formatOutput
	 */
	void setFormatOutput(boolean formatOutput) {

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatOutput)
	}

	/**
	 * Sets a <tt>NamespacePrefixMapper</tt> implementation to allow control
	 * over the namespace prefixes used in the marshaller.
	 * 
	 * @param prefixMapper <tt>NamespacePrefixMapper</tt> implementation.
	 */
	void setNamespacePrefixMapper(NamespacePrefixMapper prefixMapper) {

		marshaller.setProperty(NAMESPACE_PREFIX_MAPPER, prefixMapper)
	}

	/**
	 * Set the schema location to go into the output XML.
	 * 
	 * @param namespace Schema target namespace.
	 * @param url       Schema URL.
	 */
	void setSchemaLocation(String namespace, String url) {

		// Value can't be a GString because of internal checks against the type
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, namespace + ' ' + url)
	}

	/**
	 * Enable/Disable CDATA sections in the XML output, provided the XML objects
	 * being serialized are annotated to make use of the @{link CDataAdapter}.
	 * 
	 * @param useCDataSections
	 */
	void setUseCDataSections(boolean useCDataSections) {

		marshaller.setProperty(CHARACTER_ESCAPE_HANDLER, useCDataSections ? new CDataEscapeHandler() : null)
	}

	/**
	 * Marshalls the given XML to the given file.
	 * 
	 * @param xmlRoot Object representing the root element of the resulting XML
	 *                document.
	 * @param output  File to write the output to.
	 * @throws XmlValidationException If at least one schema has been added to
	 *         this writer and the output didn't conform to the schema.
	 */
	void write(T xmlRoot, File output) throws XmlValidationException {

		write(xmlRoot, new BufferedWriter(new FileWriter(output)))
	}

	/**
	 * Marshalls the given XML to the given output stream.
	 * 
	 * @param xmlRoot Object representing the root element of the resulting XML
	 *                document.
	 * @param output  Stream to write the output to.
	 * @throws XmlValidationException If at least one schema has been added to
	 *         this writer and the output didn't conform to the schema.
	 */
	void write(T xmlRoot, OutputStream output) throws XmlValidationException {

		write(xmlRoot, new OutputStreamWriter(output))
	}

	/**
	 * Marshalls the given XML to the given writer.
	 * 
	 * @param xmlRoot Object representing the root element of the resulting XML
	 *                document.
	 * @param output  File to write the output to.
	 * @throws XmlValidationException If at least one schema has been added to
	 *         this writer and the output didn't conform to the schema.
	 */
	void write(T xmlRoot, Writer output) throws XmlValidationException {

		// Combine and apply schemas to the marshaller
		if (!schemaBuilt && !sources) {
			marshaller.schema       = buildValidatingSchema()
			marshaller.eventHandler = new ValidationEventCollector()
			schemaBuilt = true
		}

		// Marshall
		marshaller.marshal(xmlRoot, output)

		// Check for validation errors (if validating)
		if (marshaller.schema) {
			def vec = marshaller.eventHandler
			if (vec.events) {
				throw new XmlValidationException(
						"Validation errors were detected in the output: ${vec.events.join(', ')}")
			}
		}
	}
}
