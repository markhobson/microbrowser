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

import java.util.List;

/**
 * Defines an HTML form control group.
 */
public interface ControlGroup
{
	/**
	 * Gets the name of this form control group.
	 * 
	 * @return the form control group name
	 */
	String getName();

	/**
	 * Gets all the controls within this form control group.
	 * 
	 * @return the controls
	 */
	List<Control> getControls();

	/**
	 * Gets the control with the specified value within this form control group.
	 * 
	 * @param value
	 *            the value of the required control
	 * @return the control
	 * @throws ControlNotFoundException
	 *             if the form control cannot be found
	 */
	Control getControl(String value);

	/**
	 * Gets the values of all the controls within this form control group.
	 * 
	 * @return the form control values
	 */
	List<String> getValues();

	/**
	 * Sets the values of all the controls within this form control group.
	 * 
	 * @param values
	 *            the form control values to set
	 * @throws IllegalArgumentException
	 *             if any of the values are invalid
	 */
	void setValues(String... values);
}
