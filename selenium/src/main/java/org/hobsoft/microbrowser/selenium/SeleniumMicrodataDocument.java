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

import org.hobsoft.microbrowser.AbstractMicrodataDocument;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataDocument} adapter to a Selenium {@code WebDriver}.
 */
// TODO: make package private when clients use SeleniumMicrobrowser directly
public class SeleniumMicrodataDocument extends AbstractMicrodataDocument
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebDriver driver;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumMicrodataDocument(WebDriver driver)
	{
		this.driver = checkNotNull(driver, "driver");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public List<MicrodataItem> getItems(String type)
	{
		List<WebElement> elements = driver.findElements(byItemType(type));
		
		return Lists.transform(elements, new Function<WebElement, MicrodataItem>()
		{
			public MicrodataItem apply(WebElement element)
			{
				return new SeleniumMicrodataItem(element);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasLink(String rel)
	{
		return !driver.findElements(byLink(rel)).isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Link getLink(String rel)
	{
		checkArgument(hasLink(rel), "Cannot find link: %s", rel);
		
		List<WebElement> elements = driver.findElements(byLink(rel));
		return new SeleniumLink(driver, elements.iterator().next());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Form getForm(String name)
	{
		List<WebElement> elements = driver.findElements(byForm(name));
		checkArgument(!elements.isEmpty(), "Cannot find form: %s", name);
		
		return new SeleniumForm(driver, elements.iterator().next());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static By byItemType(String itemType)
	{
		return By.cssSelector(String.format("[itemscope][itemtype='%s']", itemType));
	}
	
	private static By byLink(String rel)
	{
		return By.cssSelector(String.format("a[rel='%1$s'], link[rel='%1$s']", rel));
	}
	
	private static By byForm(String name)
	{
		return By.cssSelector(String.format("form[name='%s']", name));
	}
}
