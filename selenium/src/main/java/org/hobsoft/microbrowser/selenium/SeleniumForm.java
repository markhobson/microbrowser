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

import java.util.List;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.getFirst;

/**
 * {@code Form} adapter to a Selenium {@code WebElement}.
 */
class SeleniumForm implements Form
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebDriver driver;
	
	private final WebElement element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumForm(WebDriver driver, WebElement element)
	{
		this.driver = checkNotNull(driver, "driver");
		this.element = checkNotNull(element, "element");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public String getParameter(String name)
	{
		return getControl(name).getAttribute("value");
	}

	/**
	 * {@inheritDoc}
	 */
	public Form setParameter(String name, String value)
	{
		getControl(name).sendKeys(value);
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument submit()
	{
		WebElement submitElement = getSubmit();
		
		if (submitElement == null)
		{
			throw new MicrobrowserException("Missing form submit button");
		}
		
		submitElement.click();
		
		return new SeleniumMicrodataDocument(driver);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private WebElement getControl(String name)
	{
		List<WebElement> controlElements = element.findElements(byControl(name));
		checkArgument(!controlElements.isEmpty(), "Cannot find form control: %s", name);
		
		return controlElements.iterator().next();
	}
	
	private WebElement getSubmit()
	{
		return getFirst(element.findElements(bySubmit()), null);
	}

	private static By byControl(String name)
	{
		return By.cssSelector(String.format("input[name='%s']", name));
	}

	private static By bySubmit()
	{
		return By.cssSelector("input[type='submit'], button[type='submit'], button:not([type])");
	}
}
