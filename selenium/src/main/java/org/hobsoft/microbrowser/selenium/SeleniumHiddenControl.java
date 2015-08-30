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
package org.hobsoft.microbrowser.selenium;

import org.openqa.selenium.WebElement;

/**
 * Hidden form control wrapper for a Selenium {@code WebElement}.
 */
class SeleniumHiddenControl extends SeleniumTextControl
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumHiddenControl(WebElement element)
	{
		super(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Control methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	public final void setValue(String value)
	{
		throw new IllegalArgumentException("Cannot set hidden control value: " + getName());
	}
}
