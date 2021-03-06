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
public interface Form extends Unwrappable
{
	/**
	 * Gets the name of this form.
	 * 
	 * @return the form name
	 */
	String getName();
	
	/**
	 * Gets the control in this form with the specified name.
	 * 
	 * @param name
	 *            the name of the form control to get
	 * @return the form control
	 * @throws ControlNotFoundException
	 *             if the form control cannot be found
	 */
	Control getControl(String name);
	
	/**
	 * Gets the value of the specified form control.
	 * 
	 * @param name
	 *            the name of the form control whose value to get
	 * @return the form control value
	 * @throws ControlNotFoundException
	 *             if the form control cannot be found
	 */
	String getControlValue(String name);

	/**
	 * Sets the value of the specified form control.
	 * 
	 * @param name
	 *            the name of the form control to set
	 * @param value
	 *            the form control value to set
	 * @return this form
	 * @throws ControlNotFoundException
	 *             if the form control cannot be found
	 * @throws IllegalArgumentException
	 *             if the form control is read-only or the value is invalid
	 */
	Form setControlValue(String name, String value);

	/**
	 * Gets the control group in this form with the specified name.
	 * 
	 * @param name
	 *            the name of the form control group to get
	 * @return the form control group
	 * @throws ControlNotFoundException
	 *             if the form control group cannot be found
	 */
	ControlGroup getControlGroup(String name);

	/**
	 * Submits this form.
	 * 
	 * @return the response as a microdata document
	 * @throws MicrobrowserException
	 *             if an error occurs
	 * @throws IllegalStateException
	 *             if the form is missing a submit button
	 * @throws IllegalArgumentException
	 *             if the action is not a valid URL
	 */
	MicrodataDocument submit();
}
