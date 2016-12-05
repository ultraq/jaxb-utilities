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

import org.xml.sax.SAXParseException
import org.xml.sax.helpers.DefaultHandler

import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory

/**
 * Common code used between the XML processors that can validate their
 * input/output.
 * 
 * @author Emanuel Rabina
 */
abstract class XmlValidatingProcessor {

	protected final ArrayList<StreamSource> sources = []
	protected boolean schemaBuilt

	/**
	 * Makes this reader a validating reader by applying the given schemas.
	 * Adding a schema to this reader will cause reading to fail if the XML
	 * input doesn't conform to the schemas added by this and
	 * {@link #addValidatingSchema(InputStream...)}.
	 * <p>
	 * Note that use of the <tt>addValidatingSchema()</tt> methods are
	 * cumulative, such that the resulting schema against which the input will
	 * be validated against will contain all the supplied schemas. 
	 * 
	 * @param schemas Array or argument list of schemas from files.
	 */
	void addValidatingSchema(File... schemas) {

		schemaBuilt = false
		schemas.each { schema ->
			sources << new StreamSource(schema)
		}
	}

	/**
	 * Makes this reader a validating reader by applying the given schemas.
	 * Adding a schema to this reader will cause reading to fail if the XML
	 * input doesn't conform to the schemas added by this and
	 * {@link #addValidatingSchema(File...)}.
	 * <p>
	 * Note that use of the <tt>addValidatingSchema()</tt> methods are
	 * cumulative, such that the resulting schema against which the input will
	 * be validated against will contain all the supplied schemas. 
	 * 
	 * @param schemas Array or argument list of schemas from input streams.
	 */
	void addValidatingSchema(InputStream... schemas) {

		schemaBuilt = false
		schemas.each { schema ->
			sources << new StreamSource(schema)
		}
	}

	/**
	 * Create the overall validating schema, if schemas have been added.
	 * 
	 * @return The overall schema.
	 */
	protected Schema buildValidatingSchema() {

		def messages = []

		def schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
		schemaFactory.errorHandler = new DefaultHandler() {
			@Override
			void error(SAXParseException exception) {
				messages << exception.message
			}
			@Override
			void fatalError(SAXParseException exception) {
				messages << exception.message
			}
		}
		def schema = schemaFactory.newSchema(sources.toArray(new StreamSource[sources.size()]))

		// Report any schema-creation errors
		if (messages) {
			throw new XmlValidationException(messages.join(', '))
		}

		return schema
	}

	/**
	 * Remove all validating schemas.
	 */
	void clearValidatingSchemas() {

		sources.clear()
		schemaBuilt = false
	}
}
