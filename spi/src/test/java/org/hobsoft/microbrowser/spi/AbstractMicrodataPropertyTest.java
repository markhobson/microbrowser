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
package org.hobsoft.microbrowser.spi;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
		AbstractMicrodataProperty property = newMockProperty("meta");
		when(property.getAttribute("content", false)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithMetaAndNoContentReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("meta");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithAudioReturnsSrc()
	{
		AbstractMicrodataProperty property = newMockProperty("audio");
		when(property.getAttribute("src", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithAudioAndNoSrcReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("audio");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithEmbedReturnsSrc()
	{
		AbstractMicrodataProperty property = newMockProperty("embed");
		when(property.getAttribute("src", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithEmbedAndNoSrcReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("embed");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithIframeReturnsSrc()
	{
		AbstractMicrodataProperty property = newMockProperty("iframe");
		when(property.getAttribute("src", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithIframeAndNoSrcReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("iframe");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithImgReturnsSrc()
	{
		AbstractMicrodataProperty property = newMockProperty("img");
		when(property.getAttribute("src", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithImgAndNoSrcReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("img");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithSourceReturnsSrc()
	{
		AbstractMicrodataProperty property = newMockProperty("source");
		when(property.getAttribute("src", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithSourceAndNoSrcReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("source");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithTrackReturnsSrc()
	{
		AbstractMicrodataProperty property = newMockProperty("track");
		when(property.getAttribute("src", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithTrackAndNoSrcReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("track");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithVideoReturnsSrc()
	{
		AbstractMicrodataProperty property = newMockProperty("video");
		when(property.getAttribute("src", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithVideoAndNoSrcReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("video");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithAnchorReturnsHref()
	{
		AbstractMicrodataProperty property = newMockProperty("a");
		when(property.getAttribute("href", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithAnchorAndNoHrefReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("a");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithAreaReturnsHref()
	{
		AbstractMicrodataProperty property = newMockProperty("area");
		when(property.getAttribute("href", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithAreaAndNoHrefReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("area");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithLinkReturnsHref()
	{
		AbstractMicrodataProperty property = newMockProperty("link");
		when(property.getAttribute("href", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithLinkAndNoHrefReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("link");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithObjectReturnsData()
	{
		AbstractMicrodataProperty property = newMockProperty("object");
		when(property.getAttribute("data", true)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithObjectAndNoDataReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("object");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithDataReturnsValue()
	{
		AbstractMicrodataProperty property = newMockProperty("data");
		when(property.getAttribute("value", false)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithDataAndNoValueReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("data");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithMeterReturnsValue()
	{
		AbstractMicrodataProperty property = newMockProperty("meter");
		when(property.getAttribute("value", false)).thenReturn("1");
		
		assertThat(property.getValue(), is("1"));
	}
	
	@Test
	public void getValueWithMeterAndNoValueReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("meter");
		
		assertThat(property.getValue(), is("0"));
	}
	
	@Test
	public void getValueWithTimeReturnsDatetime()
	{
		AbstractMicrodataProperty property = newMockProperty("time");
		when(property.getAttribute("datetime", false)).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithTimeAndNoDatetimeReturnsText()
	{
		AbstractMicrodataProperty property = newMockProperty("time");
		when(property.getText()).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithTimeAndNoTextReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("time");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getValueWithOtherReturnsText()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("x");
		
		assertThat(property.getValue(), is("x"));
	}
	
	@Test
	public void getValueWithOtherAndNoTextReturnsEmpty()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		
		assertThat(property.getValue(), isEmptyString());
	}
	
	@Test
	public void getBooleanValueWhenTrueReturnsTrue()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("true");
		
		assertThat(property.getBooleanValue(), is(true));
	}
	
	@Test
	public void getBooleanValueWhenFalseReturnsFalse()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("false");
		
		assertThat(property.getBooleanValue(), is(false));
	}
	
	@Test
	public void getBooleanValueWhenEmptyReturnsFalse()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("");
		
		assertThat(property.getBooleanValue(), is(false));
	}
	
	@Test
	public void getBooleanValueWhenInvalidReturnsFalse()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("x");
		
		assertThat(property.getBooleanValue(), is(false));
	}
	
	@Test
	public void getIntValueReturnsInteger()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("1");
		
		assertThat(property.getIntValue(), is(1));
	}
	
	@Test
	public void getIntValueWhenEmptyReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("");
		
		assertThat(property.getIntValue(), is(0));
	}
	
	@Test
	public void getIntValueWhenInvalidReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("x");
		
		assertThat(property.getIntValue(), is(0));
	}

	@Test
	public void getLongValueReturnsLong()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("1");
		
		assertThat(property.getLongValue(), is(1L));
	}
	
	@Test
	public void getLongValueWhenEmptyReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("");
		
		assertThat(property.getLongValue(), is(0L));
	}
	
	@Test
	public void getLongValueWhenInvalidReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("x");
		
		assertThat(property.getLongValue(), is(0L));
	}

	@Test
	public void getFloatValueReturnsFloat()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("1");
		
		assertThat(property.getFloatValue(), is(1f));
	}
	
	@Test
	public void getFloatValueWhenEmptyReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("");
		
		assertThat(property.getFloatValue(), is(0f));
	}
	
	@Test
	public void getFloatValueWhenInvalidReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("x");
		
		assertThat(property.getFloatValue(), is(0f));
	}

	@Test
	public void getDoubleValueReturnsDouble()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("1");
		
		assertThat(property.getDoubleValue(), is(1d));
	}
	
	@Test
	public void getDoubleValueWhenEmptyReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("");
		
		assertThat(property.getDoubleValue(), is(0d));
	}
	
	@Test
	public void getDoubleValueWhenInvalidReturnsZero()
	{
		AbstractMicrodataProperty property = newMockProperty("div");
		when(property.getText()).thenReturn("x");
		
		assertThat(property.getDoubleValue(), is(0d));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static AbstractMicrodataProperty newMockProperty(String elementName)
	{
		AbstractMicrodataProperty property = mock(AbstractMicrodataProperty.class);
		when(property.getElementName()).thenReturn(elementName);
		return property;
	}
}
