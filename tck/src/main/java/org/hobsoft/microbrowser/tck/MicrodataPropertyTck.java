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

import com.squareup.okhttp.mockwebserver.MockResponse;

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
	public void getNameReturnsName() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
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
	public void getValueWhenMetaReturnsContent() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meta itemprop='p' content='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void getValueWhenMetaAndNoContentReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meta itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenAudioAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<audio itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenAudioAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<audio itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenAudioAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<audio itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenEmbedAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<embed itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenEmbedAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<embed itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenEmbedAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<embed itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenIframeAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<iframe itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenIframeAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<iframe itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenIframeAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<iframe itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenImgAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<img itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenImgAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<img itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenImgAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<img itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenSourceAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<source itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenSourceAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<source itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenSourceAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<source itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenTrackAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<track itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	// TODO: fix for Selenium
	@Ignore
	@Test
	public void getValueWhenTrackAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<track itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenTrackAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<track itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenVideoAndAbsoluteSrcReturnsSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<video itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void getValueWhenVideoAndRelativeSrcReturnsAbsoluteSrc() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<video itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}
	
	@Test
	public void getValueWhenVideoAndNoSrcReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<video itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenAnchorAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenAnchorAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenAnchorAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenAreaAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<area itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenAreaAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<area itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenAreaAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<area itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenLinkAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void getValueWhenLinkAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenLinkAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	// TODO: fix for Selenium
	@Ignore
	@Test
	public void getValueWhenObjectReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<object itemprop='p' data='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server(), "/x")));
	}

	@Test
	public void getValueWhenObjectAndNoDataReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<object itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenDataReturnsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<data itemprop='p' value='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void getValueWhenDataAndNoValueReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<data itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void getValueWhenMeterReturnsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meter itemprop='p' value='1'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("1"));
	}
	
	@Test
	public void getValueWhenMeterAndNoValueReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<meter itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("0"));
	}
	
	@Test
	public void getValueWhenTimeReturnsDatetime() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<time itemprop='p' datetime='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenTimeAndNoDatetimeReturnsText() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<time itemprop='p'>x</time>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenTimeAndNoTextReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<time itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenUnknownReturnsText() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void getValueWhenUnknownAndNoTextReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void getIntValueReturnsInteger() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>1</p>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		int actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getIntValue();
		
		assertThat("item property value", actual, is(1));
	}
	
	@Test
	public void getIntValueWhenEmptyReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		int actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getIntValue();
		
		assertThat("item property value", actual, is(0));
	}
	
	@Test
	public void getIntValueWhenInvalidReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		int actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getIntValue();
		
		assertThat("item property value", actual, is(0));
	}
	
	@Test
	public void getLongValueReturnsLong() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>1</p>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		long actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getLongValue();
		
		assertThat("item property value", actual, is(1L));
	}
	
	@Test
	public void getLongValueWhenEmptyReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		long actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getLongValue();
		
		assertThat("item property value", actual, is(0L));
	}
	
	@Test
	public void getLongValueWhenInvalidReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		long actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getLongValue();
		
		assertThat("item property value", actual, is(0L));
	}
	
	@Test
	public void getFloatValueReturnsFloat() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>1</p>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		float actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getFloatValue();
		
		assertThat("item property value", actual, is(1f));
	}
	
	@Test
	public void getFloatValueWhenEmptyReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		float actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getFloatValue();
		
		assertThat("item property value", actual, is(0f));
	}
	
	@Test
	public void getFloatValueWhenInvalidReturnsZero() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().start();
		
		float actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("p")
			.getFloatValue();
		
		assertThat("item property value", actual, is(0f));
	}
}
