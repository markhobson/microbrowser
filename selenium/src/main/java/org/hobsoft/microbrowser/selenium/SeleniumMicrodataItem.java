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

import java.net.URL;
import java.util.List;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.hobsoft.microbrowser.MicrodataPropertyNotFoundException;
import org.hobsoft.microbrowser.spi.AbstractHypermediaContainer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static org.hobsoft.microbrowser.spi.Urls.newUrlOrNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItem} adapter to a Selenium {@code WebElement}.
 */
class SeleniumMicrodataItem extends AbstractHypermediaContainer implements MicrodataItem
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

	public URL getId()
	{
		return newUrlOrNull(element.getAttribute("itemid"));
	}

	public URL getType()
	{
		return newUrlOrNull(element.getAttribute("itemtype"));
	}

	public MicrodataProperty getProperty(String name)
	{
		List<WebElement> elements = element.findElements(byItemProp(name));
		
		if (elements.isEmpty())
		{
			throw new MicrodataPropertyNotFoundException(name);
		}
		
		return new SeleniumMicrodataProperty(elements.iterator().next());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// HypermediaContainer methods
	// ----------------------------------------------------------------------------------------------------------------

	public List<Link> getLinks(String rel)
	{
		List<WebElement> elements = element.findElements(byLink(rel));
		
		return Lists.transform(elements, new Function<WebElement, Link>()
		{
			public Link apply(WebElement element)
			{
				return new SeleniumLink(driver, element);
			}
		});
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// AbstractHypermediaContainer methods
	// ----------------------------------------------------------------------------------------------------------------
	
	@Override
	protected Form newForm(String name)
	{
		List<WebElement> elements = element.findElements(byForm(name));
		
		if (elements.isEmpty())
		{
			throw new FormNotFoundException(name);
		}
		
		return new SeleniumForm(driver, elements.iterator().next());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static By byItemProp(String itemProp)
	{
		return By.cssSelector(String.format("[itemprop='%s']", itemProp));
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
