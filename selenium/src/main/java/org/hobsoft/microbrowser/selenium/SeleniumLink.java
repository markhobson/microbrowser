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

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hobsoft.microbrowser.spi.Urls.newUrlOrNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code Link} adapter to a Selenium {@code WebElement}.
 */
class SeleniumLink implements Link
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebDriver driver;
	
	private final WebElement element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumLink(WebDriver driver, WebElement element)
	{
		this.driver = checkNotNull(driver, "driver");
		this.element = checkNotNull(element, "element");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Link methods
	// ----------------------------------------------------------------------------------------------------------------
	
	public String getRel()
	{
		return element.getAttribute("rel");
	}
	
	public URL getHref()
	{
		String href = element.getAttribute("href");
		
		if (href == null)
		{
			return null;
		}
		
		return newUrlOrNull(href);
	}

	public MicrodataDocument follow()
	{
		checkArgument(getHref() != null, "Invalid URL: " + element.getAttribute("href"));

		element.click();
		
		return new SeleniumMicrodataDocument(driver);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Object methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return String.format("%s[rel=%s, href=%s]", getClass().getName(), getRel(), getHref());
	}
}
