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

package nz.net.ultraq.jaxb.adapters

import javax.xml.bind.annotation.adapters.XmlAdapter

/**
 * Special adapter class to marshall certain sections as CDATA text instead of
 * escaped text.
 * 
 * @author Emanuel Rabina
 */
class XmlCDataAdapter extends XmlAdapter<String, String> {

	/**
	 * Wraps <tt>v</tt> in a CDATA section.
	 * 
	 * @param value
	 * @return <tt>&lt;![CDATA[ + v + ]]&gt;</tt>
	 */
	@Override
	String marshal(String value) {

		return "<![CDATA[$value]]>"
	}

	/**
	 * Nothing special, returns <tt>v</tt> as is.
	 * 
	 * @param value
	 * @return <tt>v</tt>
	 */
	@Override
	String unmarshal(String value) {

		return value
	}
}
