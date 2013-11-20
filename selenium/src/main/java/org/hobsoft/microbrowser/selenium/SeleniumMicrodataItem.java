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

import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataItem} adapter to a Selenium {@code SearchContext}.
 */
public class SeleniumMicrodataItem implements MicrodataItem
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final SearchContext context;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumMicrodataItem(SearchContext context)
	{
		this.context = checkNotNull(context, "context");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public MicrodataProperty getProperty(String propertyName)
	{
		WebElement element = quietFindElementBy(context, byItemProp(propertyName));
		
		if (element == null)
		{
			return null;
		}
		
		return new SeleniumMicrodataProperty(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static WebElement quietFindElementBy(SearchContext context, By by)
	{
		List<WebElement> elements = context.findElements(by);
		
		return elements.isEmpty() ? null : elements.iterator().next();
	}

	private static By byItemProp(String itemProp)
	{
		return By.cssSelector(String.format("[itemprop='%s']", itemProp));
	}
}
