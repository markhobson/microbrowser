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

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code MicrodataProperty}.
 */
public abstract class MicrodataPropertyTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getName tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void propertyGetNameReturnsName() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("x")
			.getName();
		
		assertThat("item property name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void propertyGetValueWhenMetaReturnsContent() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meta itemprop='p' content='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenMetaAndNoContentReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meta itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenAudioAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenAudioAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void propertyGetValueWhenAudioAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenEmbedAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenEmbedAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void propertyGetValueWhenEmbedAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenIframeAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenIframeAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void propertyGetValueWhenIframeAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenImgAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenImgAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void propertyGetValueWhenImgAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenSourceAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenSourceAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void propertyGetValueWhenSourceAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenTrackAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	// TODO: fix for Selenium
	@Ignore
	@Test
	public void propertyGetValueWhenTrackAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void propertyGetValueWhenTrackAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenVideoAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenVideoAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void propertyGetValueWhenVideoAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenAnchorAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenAnchorAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void propertyGetValueWhenAnchorAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenAreaAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenAreaAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void propertyGetValueWhenAreaAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenLinkAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenLinkAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void propertyGetValueWhenLinkAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	// TODO: fix for Selenium
	@Ignore
	@Test
	public void propertyGetValueWhenObjectReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<object itemprop='p' data='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void propertyGetValueWhenObjectAndNoDataReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<object itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenDataReturnsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<data itemprop='p' value='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenDataAndNoValueReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<data itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenMeterReturnsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meter itemprop='p' value='1'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("1"));
	}
	
	@Test
	public void propertyGetValueWhenMeterAndNoValueReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meter itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("0"));
	}
	
	@Test
	public void propertyGetValueWhenTimeReturnsDatetime() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<time itemprop='p' datetime='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void propertyGetValueWhenTimeAndNoDatetimeReturnsText() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<time itemprop='p'>x</time>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void propertyGetValueWhenTimeAndNoTextReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<time itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenUnknownReturnsText() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenUnknownAndNoTextReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
}
