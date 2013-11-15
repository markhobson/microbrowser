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

import org.junit.Test;
import org.openqa.selenium.WebElement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@code SeleniumMicrodataProperty}.
 */
public class SeleniumMicrodataPropertyTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getValueWithLinkReturnsHref()
	{
		WebElement element = mock(WebElement.class);
		when(element.getTagName()).thenReturn("link");
		when(element.getAttribute("href")).thenReturn("x");
		
		assertEquals("x", new SeleniumMicrodataProperty(element).getValue());
	}
	
	@Test
	public void getValueWithAReturnsHref()
	{
		WebElement element = mock(WebElement.class);
		when(element.getTagName()).thenReturn("a");
		when(element.getAttribute("href")).thenReturn("x");
		
		assertEquals("x", new SeleniumMicrodataProperty(element).getValue());
	}
	
	@Test
	public void getValueWithOtherReturnsText()
	{
		WebElement element = mock(WebElement.class);
		when(element.getTagName()).thenReturn("div");
		when(element.getText()).thenReturn("x");
		
		assertEquals("x", new SeleniumMicrodataProperty(element).getValue());
	}
}
