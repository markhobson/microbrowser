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

import org.hobsoft.microbrowser.Control;
import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base form control wrapper for a Selenium {@code WebElement}.
 */
abstract class AbstractSeleniumControl implements Control
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebElement element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	AbstractSeleniumControl(WebElement element)
	{
		this.element = checkNotNull(element, "element");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Control methods
	// ----------------------------------------------------------------------------------------------------------------

	public final String getName()
	{
		return element.getAttribute("name");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Unwrappable methods
	// ----------------------------------------------------------------------------------------------------------------

	public <T> T unwrap(Class<T> type)
	{
		checkArgument(WebElement.class.equals(type), "Cannot unwrap to: %s", type);
		
		return type.cast(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected final WebElement getElement()
	{
		return element;
	}
}
