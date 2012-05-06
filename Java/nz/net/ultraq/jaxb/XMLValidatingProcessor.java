
package nz.net.ultraq.jaxb;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * Common code used between the XML processors that can validate their
 * input/output.
 * 
 * @author Emanuel Rabina
 */
abstract class XMLValidatingProcessor {

	final ArrayList<StreamSource> sources = new ArrayList<StreamSource>();
	boolean schemabuilt;

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
	 * @param schemas Array of schemas from files.
	 */
	public void addValidatingSchema(File... schemas) {

		schemabuilt = false;
		for (File schema: schemas) {
			sources.add(new StreamSource(schema));
		}
	}

	/**
	 * Makes this reader a validating reader by applying the given schemas.
	 * Adding a schema to this reader will cause reading to fail if the XML
	 * input doesn't conform to the schemas added by any
	 * <tt>addValidatingSchema()</tt> methods.
	 * <p>
	 * Note that use of the <tt>addValidatingSchema()</tt> methods are
	 * cumulative, such that the resulting schema against which the input will
	 * be validated against will contain all the supplied schemas. 
	 * 
	 * @param schemas Array of schemas from inputstreams.
	 */
	public void addValidatingSchema(InputStream... schemas) {

		schemabuilt = false;
		for (InputStream schema: schemas) {
			sources.add(new StreamSource(schema));
		}
	}

	/**
	 * Create the overall validating schema, if schemas have been added.
	 * 
	 * @return The overall schema.
	 * @throws XMLException If any errors came out of the schema creation.
	 * @throws SAXException
	 */
	Schema buildValidatingSchema() throws XMLException, SAXException {

		final StringBuilder messages = new StringBuilder();

		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		sf.setErrorHandler(new DefaultHandler() {
			@Override
			public void error(SAXParseException exception) {
				messages.append(exception.getMessage()).append("\n");
			}
			@Override
			public void fatalError(SAXParseException exception) {
				messages.append(exception.getMessage()).append("\n");
			}
		});
		Schema schema = sf.newSchema(sources.toArray(new StreamSource[sources.size()]));

		// Report any schema-creation errors
		if (messages.length() > 0) {
			throw new XMLException(messages.toString().trim());
		}

		return schema;
	}

	/**
	 * Remove all validating schemas.
	 * 
	 * @throws XMLException
	 */
	public final void clearValidatingSchemas() throws XMLException {

		sources.clear();
		try {
			clearValidatingSchemasImpl();
		}
		catch (JAXBException ex) {
			throw new XMLException("An error occurred when clearing the validating schemas", ex);
		}
		schemabuilt = false;
	}

	/**
	 * Remove all validating schemas.
	 * 
	 * @throws JAXBException
	 */
	abstract void clearValidatingSchemasImpl() throws JAXBException;
}
