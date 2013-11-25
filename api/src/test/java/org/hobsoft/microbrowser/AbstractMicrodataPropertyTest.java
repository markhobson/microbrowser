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
	public void getValueWithIframeReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("iframe");
		property.addAttribute("src", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithIframeAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("iframe");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithImgReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("img");
		property.addAttribute("src", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithImgAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("img");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithSourceReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("source");
		property.addAttribute("src", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithSourceAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("source");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithTrackReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("track");
		property.addAttribute("src", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithTrackAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("track");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithVideoReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("video");
		property.addAttribute("src", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithVideoAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("video");
		
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
	public void getValueWithObjectReturnsData()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("object");
		property.addAttribute("data", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithObjectAndNoDataReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("object");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithDataReturnsValue()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("data");
		property.addAttribute("value", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithDataAndNoValueReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("data");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithMeterReturnsValue()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("meter");
		property.addAttribute("value", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithMeterAndNoValueReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("meter");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithTimeReturnsDatetime()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("time");
		property.addAttribute("datetime", "x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithTimeAndNoDatetimeReturnsText()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("time");
		property.setText("x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithTimeAndNoTextReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("time");
		
		assertEquals("", property.getValue());
	}
	
	@Test
	public void getValueWithOtherReturnsText()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("x");
		
		assertEquals("x", property.getValue());
	}
	
	@Test
	public void getValueWithOtherAndNoTextReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		
		assertEquals("", property.getValue());
	}
}
