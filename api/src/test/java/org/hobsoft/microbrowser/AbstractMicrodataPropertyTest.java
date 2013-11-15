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
	public void getValueWithLinkReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("link");
		property.addAttribute("href", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithAReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("a");
		property.addAttribute("href", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithOtherReturnsText()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("x");
		
		assertEquals("x", property.getValue());
	}
}
