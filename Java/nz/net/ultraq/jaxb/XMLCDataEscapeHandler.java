
package nz.net.ultraq.jaxb;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.bind.marshaller.MinimumEscapeHandler;

import java.io.IOException;
import java.io.Writer;

/**
 * Escape handler to avoid escaping CDATA sections.
 * 
 * @author Emanuel Rabina
 */
public class XMLCDataEscapeHandler implements CharacterEscapeHandler {

	private static final CharacterEscapeHandler defaulthandler = MinimumEscapeHandler.theInstance;

	/**
	 * Perform escaping as normal except on CDATA blocks.
	 * 
	 * @param ch
	 * @param start
	 * @param length
	 * @param isAttVal
	 * @param out
	 * @throws IOException
	 */
	@Override
	public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {

		// If this is a CDATA block, do no escaping
		if (length - start >= 9) {
			String cdatacheck = new String(ch, start, 9);
			if (cdatacheck.equals("<![CDATA[")) {
				out.write(ch, start, length);
				return;
			}
		}

		// Otherwise, escape as normal
		defaulthandler.escape(ch, start, length, isAttVal, out);
	}
}
