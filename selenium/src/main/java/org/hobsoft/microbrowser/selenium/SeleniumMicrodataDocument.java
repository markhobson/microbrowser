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

import org.hobsoft.microbrowser.CookieNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.spi.AbstractMicrodataDocument;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static org.hobsoft.microbrowser.spi.Urls.newUrl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataDocument} adapter to a Selenium {@code WebDriver}.
 */
class SeleniumMicrodataDocument extends AbstractMicrodataDocument
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

	public List<MicrodataItem> getItems(String type)
	{
		List<WebElement> elements = driver.findElements(byItemType(newUrl(type)));
		
		return Lists.transform(elements, new Function<WebElement, MicrodataItem>()
		{
			public MicrodataItem apply(WebElement element)
			{
				return new SeleniumMicrodataItem(driver, element);
			}
		});
	}
	
	public String getCookie(String name)
	{
		Cookie cookie = driver.manage().getCookieNamed(name);
		
		if (cookie == null)
		{
			throw new CookieNotFoundException(name);
		}
		
		return cookie.getValue();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Hypermedia methods
	// ----------------------------------------------------------------------------------------------------------------

	public List<Link> getLinks(String rel)
	{
		List<WebElement> elements = driver.findElements(byLink(rel));
		
		return Lists.transform(elements, new Function<WebElement, Link>()
		{
			public Link apply(WebElement element)
			{
				return new SeleniumLink(driver, element);
			}
		});
	}
	
	public Form getForm(String name)
	{
		List<WebElement> elements = driver.findElements(byForm(name));
		
		if (elements.isEmpty())
		{
			throw new FormNotFoundException(name);
		}
		
		return new SeleniumForm(driver, elements.iterator().next());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Unwrappable methods
	// ----------------------------------------------------------------------------------------------------------------

	public <T> T unwrap(Class<T> type)
	{
		checkArgument(WebDriver.class.equals(type), "Cannot unwrap to: %s", type);
		
		return type.cast(driver);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static By byItemType(URL itemType)
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
