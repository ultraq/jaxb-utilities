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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;

/**
 * XML file unmarshaller using JAXB to turn XML files into instances of their
 * JAXB-annotated classes.
 * 
 * @param <T> A JAXB generated class representing the root element of the XML
 * 			  document to be read.
 * @author Emanuel Rabina
 */
public class XMLReader<T> extends XMLValidatingProcessor {

	private final Unmarshaller unmarshaller;

    /**
	 * Sets-up the XML unmarshaller.
	 * 
	 * @param xmlpack Package containing the Java bindings.
	 * @throws XMLException
	 */
	public XMLReader(String xmlpack) throws XMLException {

		try {
			JAXBContext jaxbcontext = JAXBContext.newInstance(xmlpack, getClass().getClassLoader());
			unmarshaller = jaxbcontext.createUnmarshaller();
		}
		catch (JAXBException ex) {
			throw new XMLException("An error occurred when creating the unmarshaller", ex);
		}
	}

	/**
	 * Sets-up the XML unmarshaller.
	 * 
	 * @param xmlclasses List of classes to be recognized by the reader.
	 * @throws XMLException
	 */
	public XMLReader(Class<?>... xmlclasses) throws XMLException {

		try {
			JAXBContext jaxbcontext = JAXBContext.newInstance(xmlclasses);
			unmarshaller = jaxbcontext.createUnmarshaller();
		}
		catch (JAXBException ex) {
			throw new XMLException("An error occurred when creating the unmarshaller", ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearValidatingSchemasImpl() throws JAXBException {

		unmarshaller.setSchema(null);
		unmarshaller.setEventHandler(null);
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input File to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XMLException If an error occurred during unmarshalling, or at
	 * 		   least one schema has been added against this reader and the input
	 * 		   didn't conform to the schema.
	 */
	public T readXMLData(File input) throws XMLException {

		try {
			return readXMLData(new BufferedReader(new FileReader(input)));
		}
		catch (FileNotFoundException ex) {
			throw new XMLException("Input file does not exist", ex);
		}
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input Stream to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XMLException If an error occurred during unmarshalling, or at
	 * 		   least one schema has been added against this reader and the input
	 * 		   didn't conform to the schema.
	 */
	public T readXMLData(InputStream input) throws XMLException {

		return readXMLData(new InputStreamReader(input));
	}

	/**
	 * Unmarshalls the data inside the given XML document, returning it as a
	 * Java object.
	 * 
	 * @param input Stream to read the XML data from.
	 * @return Object representing the root element of the given XML document.
	 * @throws XMLException If an error occurred during unmarshalling, or at
	 * 		   least one schema has been added against this reader and the input
	 * 		   didn't conform to the schema.
	 */
	@SuppressWarnings("unchecked")
	public T readXMLData(Reader input) throws XMLException {

		try {
			// Combine and apply schemas to the unmarshaller
			if (!schemabuilt && !sources.isEmpty()) {
				unmarshaller.setSchema(buildValidatingSchema());
				unmarshaller.setEventHandler(new ValidationEventCollector());
				schemabuilt = true;
			}

			// Unmarshall
			T xmlroot = (T)unmarshaller.unmarshal(input);

			// Check for validation errors (if validating)
			if (unmarshaller.getSchema() != null) {
				ValidationEventCollector vec = (ValidationEventCollector)unmarshaller.getEventHandler();
				if (vec.hasEvents()) {
					StringBuilder eventlist = new StringBuilder();
					for (ValidationEvent event: vec.getEvents()) {
						eventlist.append(event.getMessage()).append("\n");
					}
					throw new XMLException("Validation errors were detected in the input: " +
							eventlist.toString().trim());
				}
			}

			return xmlroot;
		}
		catch (SAXException ex) {
			throw new XMLException("An error occurred when processing the validating schemas", ex);
		}
		catch (JAXBException ex) {
			throw new XMLException("An error occurred during unmarshalling", ex);
		}
	}
}
