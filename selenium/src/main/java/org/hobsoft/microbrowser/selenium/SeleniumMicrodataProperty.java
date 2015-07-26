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

import org.hobsoft.microbrowser.AbstractMicrodataProperty;
import org.hobsoft.microbrowser.MicrodataItem;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItemValue} adapter to a Selenium {@code WebElement}.
 */
class SeleniumMicrodataProperty extends AbstractMicrodataProperty
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebDriver driver;
	
	private final WebElement element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumMicrodataProperty(WebDriver driver, WebElement element)
	{
		this.driver = checkNotNull(driver, "driver");
		this.element = checkNotNull(element, "element");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataProperty methods
	// ----------------------------------------------------------------------------------------------------------------

	public MicrodataItem getItemValue()
	{
		if (element.getAttribute("itemscope") == null)
		{
			return null;
		}
		
		return new SeleniumMicrodataItem(driver, element);
	}
	
	public <T> T unwrap(Class<T> type)
	{
		T instance;
		
		if (WebElement.class.equals(type))
		{
			instance = type.cast(element);
		}
		else
		{
			throw new IllegalArgumentException("Cannot unwrap to: " + type);
		}
		
		return instance;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// AbstractMicrodataProperty methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected String getElementName()
	{
		return element.getTagName();
	}

	@Override
	protected String getAttribute(String name, boolean absoluteUrl)
	{
		return element.getAttribute(name);
	}

	@Override
	protected String getText()
	{
		return element.getText();
	}
}
