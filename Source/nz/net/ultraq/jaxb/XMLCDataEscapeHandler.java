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
