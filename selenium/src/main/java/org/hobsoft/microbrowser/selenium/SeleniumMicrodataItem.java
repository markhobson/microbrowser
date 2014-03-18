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

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItem} adapter to a Selenium {@code WebElement}.
 */
class SeleniumMicrodataItem implements MicrodataItem
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebDriver driver;
	
	private final WebElement element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumMicrodataItem(WebDriver driver, WebElement element)
	{
		this.driver = checkNotNull(driver, "driver");
		this.element = checkNotNull(element, "element");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public String getType()
	{
		return element.getAttribute("itemtype");
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataProperty getProperty(String name)
	{
		List<WebElement> elements = element.findElements(byItemProp(name));
		checkArgument(!elements.isEmpty(), "Cannot find item property: %s", name);
		
		return new SeleniumMicrodataProperty(elements.iterator().next());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Link getLink(String rel)
	{
		checkArgument(hasLink(rel), "Cannot find link: %s", rel);
		
		List<WebElement> elements = element.findElements(byLink(rel));
		return new SeleniumLink(driver, elements.iterator().next());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private boolean hasLink(String rel)
	{
		return !driver.findElements(byLink(rel)).isEmpty();
	}
	
	private static By byItemProp(String itemProp)
	{
		return By.cssSelector(String.format("[itemprop='%s']", itemProp));
	}
	
	private static By byLink(String rel)
	{
		return By.cssSelector(String.format("a[rel='%1$s'], link[rel='%1$s']", rel));
	}
}
