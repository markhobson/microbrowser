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
package org.hobsoft.microbrowser.tck;

import org.hobsoft.microbrowser.MicrodataProperty;
import org.junit.Ignore;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code MicrodataProperty}.
 * 
 * @param <T>
 *            the provider-specific property type
 */
public abstract class MicrodataPropertyTck<T> extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getName tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getNameReturnsName()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("x")
			.getName();
		
		assertThat("item property name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getValueWhenMetaReturnsContent()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meta itemprop='p' content='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void getValueWhenMetaAndNoContentReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meta itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenAudioAndAbsoluteSrcReturnsSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<audio itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenAudioAndRelativeSrcReturnsAbsoluteSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<audio itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenAudioAndNoSrcReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<audio itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenEmbedAndAbsoluteSrcReturnsSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<embed itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenEmbedAndRelativeSrcReturnsAbsoluteSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<embed itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenEmbedAndNoSrcReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<embed itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenIframeAndAbsoluteSrcReturnsSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<iframe itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenIframeAndRelativeSrcReturnsAbsoluteSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<iframe itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenIframeAndNoSrcReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<iframe itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenImgAndAbsoluteSrcReturnsSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<img itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenImgAndRelativeSrcReturnsAbsoluteSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<img itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenImgAndNoSrcReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<img itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenSourceAndAbsoluteSrcReturnsSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<source itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenSourceAndRelativeSrcReturnsAbsoluteSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<source itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenSourceAndNoSrcReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<source itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenTrackAndAbsoluteSrcReturnsSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<track itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	// TODO: fix for Selenium
	@Ignore
	@Test
	public void getValueWhenTrackAndRelativeSrcReturnsAbsoluteSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<track itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenTrackAndNoSrcReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<track itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenVideoAndAbsoluteSrcReturnsSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<video itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenVideoAndRelativeSrcReturnsAbsoluteSrc()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<video itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenVideoAndNoSrcReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<video itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenAnchorAndAbsoluteHrefReturnsUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenAnchorAndRelativeHrefReturnsAbsoluteUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenAnchorAndNoHrefReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenAreaAndAbsoluteHrefReturnsUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<area itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenAreaAndRelativeHrefReturnsAbsoluteUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<area itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenAreaAndNoHrefReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<area itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenLinkAndAbsoluteHrefReturnsUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenLinkAndRelativeHrefReturnsAbsoluteUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenLinkAndNoHrefReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	// TODO: fix for Selenium
	@Ignore
	@Test
	public void getValueWhenObjectReturnsAbsoluteUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<object itemprop='p' data='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenObjectAndNoDataReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<object itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenDataReturnsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<data itemprop='p' value='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void getValueWhenDataAndNoValueReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<data itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenMeterReturnsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meter itemprop='p' value='1'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("1"));
	}
	
	@Test
	public void getValueWhenMeterAndNoValueReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meter itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("0"));
	}
	
	@Test
	public void getValueWhenTimeReturnsDatetime()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<time itemprop='p' datetime='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenTimeAndNoDatetimeReturnsText()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<time itemprop='p'>x</time>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenTimeAndNoTextReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<time itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenUnknownReturnsText()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void getValueWhenUnknownAndNoTextReturnsEmpty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getBooleanValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getBooleanValueWhenTrueReturnsTrue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>true</p>"
			+ "</div>"
			+ "</body></html>"));
		
		boolean actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getBooleanValue();
		
		assertThat("item property value", actual, is(true));
	}
	
	@Test
	public void getBooleanValueWhenFalseReturnsFalse()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>false</p>"
			+ "</div>"
			+ "</body></html>"));
		
		boolean actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getBooleanValue();
		
		assertThat("item property value", actual, is(false));
	}
	
	@Test
	public void getBooleanValueWhenEmptyReturnsFalse()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		boolean actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getBooleanValue();
		
		assertThat("item property value", actual, is(false));
	}
	
	@Test
	public void getBooleanValueWhenInvalidReturnsFalse()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		
		boolean actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getBooleanValue();
		
		assertThat("item property value", actual, is(false));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getIntValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getIntValueReturnsInteger()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>1</p>"
			+ "</div>"
			+ "</body></html>"));
		
		int actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getIntValue();
		
		assertThat("item property value", actual, is(1));
	}
	
	@Test
	public void getIntValueWhenEmptyReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		int actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getIntValue();
		
		assertThat("item property value", actual, is(0));
	}
	
	@Test
	public void getIntValueWhenInvalidReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		
		int actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getIntValue();
		
		assertThat("item property value", actual, is(0));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getLongValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLongValueReturnsLong()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>1</p>"
			+ "</div>"
			+ "</body></html>"));
		
		long actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getLongValue();
		
		assertThat("item property value", actual, is(1L));
	}
	
	@Test
	public void getLongValueWhenEmptyReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		long actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getLongValue();
		
		assertThat("item property value", actual, is(0L));
	}
	
	@Test
	public void getLongValueWhenInvalidReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		
		long actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getLongValue();
		
		assertThat("item property value", actual, is(0L));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getFloatValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getFloatValueReturnsFloat()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>1</p>"
			+ "</div>"
			+ "</body></html>"));
		
		float actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getFloatValue();
		
		assertThat("item property value", actual, is(1f));
	}
	
	@Test
	public void getFloatValueWhenEmptyReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		float actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getFloatValue();
		
		assertThat("item property value", actual, is(0f));
	}
	
	@Test
	public void getFloatValueWhenInvalidReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		
		float actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getFloatValue();
		
		assertThat("item property value", actual, is(0f));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getDoubleValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getDoubleValueReturnsDouble()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>1</p>"
			+ "</div>"
			+ "</body></html>"));
		
		double actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getDoubleValue();
		
		assertThat("item property value", actual, is(1d));
	}
	
	@Test
	public void getDoubleValueWhenEmptyReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		
		double actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getDoubleValue();
		
		assertThat("item property value", actual, is(0d));
	}
	
	@Test
	public void getDoubleValueWhenInvalidReturnsZero()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		
		double actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getDoubleValue();
		
		assertThat("item property value", actual, is(0d));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapReturnsProvider()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		T actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("x")
			.unwrap(getProviderType());
		
		assertThat("item property provider", actual, is(instanceOf(getProviderType())));
	}

	@Test
	public void unwrapWithUnknownTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		MicrodataProperty property = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("x");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot unwrap to: class java.lang.Void");
		
		property.unwrap(Void.class);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Class<T> getProviderType();
}
