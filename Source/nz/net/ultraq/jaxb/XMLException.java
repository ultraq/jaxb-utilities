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

/**
 * Wrapper for any exceptions that may arise during the XML transformating
 * phase.
 * 
 * @author Emanuel Rabina
 */
public class XMLException extends RuntimeException {

	/**
	 * Constructor, generates a new exception with the given message.
	 * 
	 * @param message Exception message to use.
	 */
	protected XMLException(String message) {

		super(message);
	}

	/**
	 * Constructor, wraps the original exception in this unchecked type.
	 * 
	 * @param message Error message to accompany the exception.
	 * @param cause The cause for this exception to be raised.
	 */
	protected XMLException(String message, Exception cause) {

		super(message, cause);
	}
}
