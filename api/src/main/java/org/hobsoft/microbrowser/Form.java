/*
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
package org.hobsoft.microbrowser;

/**
 * Defines an HTML form.
 */
public interface Form
{
	String getParameter(String name);

	/**
	 * Sets the value of the specified parameter.
	 * 
	 * @param name
	 *            the name of the parameter to set
	 * @param value
	 *            the value to set
	 * @return this form
	 * @throws IllegalArgumentException
	 *             if the parameter cannot be found
	 */
	Form setParameter(String name, String value);

	/**
	 * Submits this form.
	 * 
	 * @return the response as a Microdata document
	 * @throws MicrobrowserException
	 *             if an error occurs
	 */
	MicrodataDocument submit();
}
