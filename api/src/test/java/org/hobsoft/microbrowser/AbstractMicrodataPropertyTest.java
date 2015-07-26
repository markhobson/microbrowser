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

import org.hobsoft.microbrowser.support.FakeMicrodataProperty;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;

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
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithMetaAndNoContentReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("meta");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithAudioReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("audio");
		property.addAbsoluteUrlAttribute("src", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithAudioAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("audio");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithEmbedReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("embed");
		property.addAbsoluteUrlAttribute("src", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithEmbedAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("embed");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithIframeReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("iframe");
		property.addAbsoluteUrlAttribute("src", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithIframeAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("iframe");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithImgReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("img");
		property.addAbsoluteUrlAttribute("src", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithImgAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("img");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithSourceReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("source");
		property.addAbsoluteUrlAttribute("src", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithSourceAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("source");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithTrackReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("track");
		property.addAbsoluteUrlAttribute("src", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithTrackAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("track");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithVideoReturnsSrc()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("video");
		property.addAbsoluteUrlAttribute("src", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithVideoAndNoSrcReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("video");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithAnchorReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("a");
		property.addAbsoluteUrlAttribute("href", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithAnchorAndNoHrefReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("a");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithAreaReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("area");
		property.addAbsoluteUrlAttribute("href", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithAreaAndNoHrefReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("area");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithLinkReturnsHref()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("link");
		property.addAbsoluteUrlAttribute("href", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithLinkAndNoHrefReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("link");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithObjectReturnsData()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("object");
		property.addAbsoluteUrlAttribute("data", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithObjectAndNoDataReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("object");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithDataReturnsValue()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("data");
		property.addAttribute("value", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithDataAndNoValueReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("data");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithMeterReturnsValue()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("meter");
		property.addAttribute("value", "1");
		
		assertThat(property.getValue(), is("1"));
	}
	
	@Test
	public void getValueWithMeterAndNoValueReturnsZero()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("meter");
		
		assertThat(property.getValue(), is("0"));
	}
	
	@Test
	public void getValueWithTimeReturnsDatetime()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("time");
		property.addAttribute("datetime", "x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithTimeAndNoDatetimeReturnsText()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("time");
		property.setText("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithTimeAndNoTextReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("time");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithOtherReturnsText()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithOtherAndNoTextReturnsEmpty()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getIntValueReturnsInteger()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("1");
		
		assertThat(property.getIntValue(), is(1));
	}
	
	@Test
	public void getIntValueWhenEmptyReturnsZero()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("");
		assertThat(property.getIntValue(), is(0));
	}
	
	@Test
	public void getIntValueWhenInvalidReturnsZero()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("x");
		
		assertThat(property.getIntValue(), is(0));
	}

	@Test
	public void getLongValueReturnsLong()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("1");
		
		assertThat(property.getLongValue(), is(1L));
	}
	
	@Test
	public void getLongValueWhenEmptyReturnsZero()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("");
		
		assertThat(property.getLongValue(), is(0L));
	}
	
	@Test
	public void getLongValueWhenInvalidReturnsZero()
	{
		FakeMicrodataProperty property = new FakeMicrodataProperty("div");
		property.setText("x");
		
		assertThat(property.getLongValue(), is(0L));
	}
}
