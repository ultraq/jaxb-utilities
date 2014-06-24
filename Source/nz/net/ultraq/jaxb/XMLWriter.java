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

package nz.net.ultraq.jaxb;

import org.xml.sax.SAXException;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;

/**
 * XML file marshaller using JAXB to turn JAXB-annotated classes into XML files.
 * 
 * @param <T> A JAXB generated class representing the root element of the XML
 * 			  document to be written.
 * @author Emanuel Rabina
 */
public class XMLWriter<T> extends XMLValidatingProcessor {

	private static final String CHARACTER_ESCAPE_HANDLER = "com.sun.xml.bind.marshaller.CharacterEscapeHandler";
	private static final String NAMESPACE_PREFIX_MAPPER  = "com.sun.xml.bind.namespacePrefixMapper";

	private final Marshaller marshaller;

	/**
	 * Sets-up the XML marshaller.
	 * 
	 * @param xmlclasses List of classes that the reader needs to recognize.
	 * @throws XMLException
	 */
	public XMLWriter(Class<?>... xmlclasses) throws XMLException {

		try {
			JAXBContext jaxbcontext = JAXBContext.newInstance(xmlclasses);
			marshaller = jaxbcontext.createMarshaller();
		}
		catch (JAXBException ex) {
			throw new XMLException("An error occurred when creating the marshaller", ex);
		}
	}

	/**
	 * Sets-up the XML marshaller.
	 * 
	 * @param xmlpack Name of the package containing JAXB-generated classes.
	 * @throws XMLException
	 */
	public XMLWriter(String xmlpack) throws XMLException {

		try {
			JAXBContext jaxbcontext = JAXBContext.newInstance(xmlpack);
			marshaller = jaxbcontext.createMarshaller();
		}
		catch (JAXBException ex) {
			throw new XMLException("An error occurred when creating the marshaller", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearValidatingSchemasImpl() throws JAXBException {

		marshaller.setSchema(null);
		marshaller.setEventHandler(null);
	}

	/**
	 * Set whether the resulting XML file will be formatted 'nicely'.
	 * 
	 * @param formatoutput
	 * @throws XMLException If there was some issue in accepting the 'format'
	 * 		   property.
	 */
	public void setFormatOutput(boolean formatoutput) {

		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatoutput);
		}
		catch (PropertyException ex) {
			throw new XMLException("Unable to set 'format output' property to " + formatoutput, ex);
		}
	}

	/**
	 * Sets a <tt>NamespacePrefixMapper</tt> implementation to allow control
	 * over the namespace prefixes used in the marshaller.
	 * 
	 * @param prefixmapper <tt>NamespacePrefixMapper</tt> implementation.
	 * @throws XMLException
	 */
	public void setNamespacePrefixMapper(NamespacePrefixMapper prefixmapper) throws XMLException {

		try {
			marshaller.setProperty(NAMESPACE_PREFIX_MAPPER, prefixmapper);
		}
		catch (PropertyException ex) {
			throw new XMLException("Unable to specify a custom namespace prefix mapping", ex);
		}
	}

	/**
	 * Sets-up a schema location to go into the output XML.  If this method is
	 * called more than once, then only the latest schema location is used.
	 * 
	 * @param namespace Schema target namespace.
	 * @param url		Schema URL.
	 * @throws XMLException
	 */
	public void setSchemaLocation(String namespace, String url) throws XMLException {

		try {
			marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, namespace + " " + url);
		}
		catch (PropertyException ex) {
			throw new XMLException("Unable to specify a schema location", ex);
		}
	}

	/**
	 * Enable/Disable CDATA sections in the XML output, provided the XML objects
	 * being serialized are annotated to make use of the @{link XMLCDataAdapter}.
	 * 
	 * @param usecdatasections
	 * @throws XMLException
	 */
	public void setUseCDATASections(boolean usecdatasections) throws XMLException {

		try {
			marshaller.setProperty(CHARACTER_ESCAPE_HANDLER, usecdatasections ?
					new XMLCDataEscapeHandler() : null);
		}
		catch (PropertyException ex) {
			throw new XMLException("Unable to " + (usecdatasections ? "enable" : "disable") +
					"the use of CDATA sections", ex);
		}
	}

	/**
	 * Marshalls the given XML to the given file.
	 * 
	 * @param xmlroot Object representing the root element of the resulting XML
	 * 				  document.
	 * @param output  File to write the output to.
	 * @throws XMLException If an error occurred during marshalling, or at least
	 * 		   one schema was set against the marshaller and the output didn't
	 * 		   conform to the schema.
	 */
	public void writeXMLData(T xmlroot, File output) throws XMLException {

		try {
			writeXMLData(xmlroot, new BufferedWriter(new FileWriter(output)));
		}
		catch (IOException ex) {
			throw new XMLException("Output file cannot be written", ex);
		}
	}

	/**
	 * Marshalls the given XML to the given output stream.
	 * 
	 * @param xmlroot Object representing the root element of the resulting XML
	 * 				  document.
	 * @param output  Stream to write the output to.
	 * @throws XMLException If an error occurred during marshalling, or at least
	 * 		   one schema was set against the marshaller and the output didn't
	 * 		   conform to the schema.
	 */
	public void writeXMLData(T xmlroot, OutputStream output) throws XMLException {

		writeXMLData(xmlroot, new OutputStreamWriter(output));
	}

	/**
	 * Marshalls the given XML to the given writer.
	 * 
	 * @param xmlroot Object representing the root element of the resulting XML
	 * 				  document.
	 * @param output  File to write the output to.
	 * @throws XMLException If an error occurred during marshalling, or at least
	 * 		   one schema was set against the marshaller and the output didn't
	 * 		   conform to the schema.
	 */
	public void writeXMLData(T xmlroot, Writer output) throws XMLException {

		try {
			// Combine and apply schemas to the marshaller
			if (!schemabuilt && !sources.isEmpty()) {
				marshaller.setSchema(buildValidatingSchema());
				marshaller.setEventHandler(new ValidationEventCollector());
				schemabuilt = true;
			}

			// Marshall
			marshaller.marshal(xmlroot, output);

			// Check for validation errors (if validating)
			if (marshaller.getSchema() != null) {
				ValidationEventCollector vec = (ValidationEventCollector)marshaller.getEventHandler();
				if (vec.hasEvents()) {
					StringBuilder eventlist = new StringBuilder();
					for (ValidationEvent event: vec.getEvents()) {
						eventlist.append(event.getMessage()).append("\n");
					}
					throw new XMLException("Validation errors were detected in the output: " +
							eventlist.toString().trim());
				}
			}
		}
		catch (SAXException ex) {
			throw new XMLException("An error occurred when processing the validating schemas", ex);
		}
		catch (JAXBException ex) {
			throw new XMLException("An error occurred during marshalling", ex);
		}
	}
}
