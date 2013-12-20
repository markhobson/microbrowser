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
import org.hobsoft.microbrowser.MicrodataDocument;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

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
	public String getName()
	{
		return element.getAttribute("name");
	}
	
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
		WebElement control = getControl(name);
		control.clear();
		control.sendKeys(value);
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument submit()
	{
		getSubmit().click();
		
		return new SeleniumMicrodataDocument(driver);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private WebElement getControl(String name)
	{
		List<WebElement> elements = element.findElements(byControl(name));
		checkArgument(!elements.isEmpty(), "Cannot find form control: %s", name);
		
		return elements.iterator().next();
	}
	
	private static By byControl(String name)
	{
		return By.cssSelector(String.format("input[name='%s']", name));
	}

	private WebElement getSubmit()
	{
		List<WebElement> elements = element.findElements(bySubmit());
		checkState(!elements.isEmpty(), "Missing form submit button");
		
		return elements.iterator().next();
	}

	private static By bySubmit()
	{
		return By.cssSelector("input[type='submit'], button[type='submit'], button:not([type])");
	}
}
