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
 * Defines an HTML form control.
 */
public interface Control extends Unwrappable
{
	/**
	 * Gets the name of this form control.
	 * 
	 * @return the form control name
	 */
	String getName();

	/**
	 * Gets the value of this form control.
	 * 
	 * @return the form control value
	 */
	String getValue();

	/**
	 * Sets the value of this form control.
	 * 
	 * @param value
	 *            the form control value to set
	 * @throws IllegalArgumentException
	 *             if the form control is read-only or the value is invalid
	 */
	void setValue(String value);
}
