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
package org.hobsoft.microbrowser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@code AbstractMicrodataProperty}.
 */
public class AbstractMicrodataPropertyTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getValueWithMetaReturnsContent()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("meta");
		property.addAttribute("content", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithMetaAndNoContentReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("meta");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithAudioReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("audio");
		property.addAttribute("src", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithAudioAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("audio");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithEmbedReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("embed");
		property.addAttribute("src", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithEmbedAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("embed");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithAnchorReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("a");
		property.addAttribute("href", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithAnchorAndNoHrefReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("a");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithAreaReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("area");
		property.addAttribute("href", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithAreaAndNoHrefReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("area");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithLinkReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("link");
		property.addAttribute("href", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithLinkAndNoHrefReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("link");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithOtherReturnsText()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("x");
		
		assertEquals("x", property.getValue());
	}
}
