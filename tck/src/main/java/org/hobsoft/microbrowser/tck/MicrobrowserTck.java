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
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.RecordedRequestMatcher.get;
import static org.hobsoft.microbrowser.tck.RecordedRequestMatcher.post;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Microbrowser}.
 */
public abstract class MicrobrowserTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private MockWebServer server;
	
	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@Before
	public final void setUpTck()
	{
		Logger.getLogger("com.google.mockwebserver").setLevel(Level.WARNING);
		
		server = new MockWebServer();
	}

	@After
	public final void tearDownTck() throws IOException
	{
		server.shutdown();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Microbrowser.get tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getRequestsPath() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server, "/x"));
		
		assertThat("request path", server.takeRequest().getPath(), is("/x"));
	}

	@Test
	public void getSetsCookie() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.get tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetRequestsPath() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse());
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.get(url(server, "/x"));
		
		server.takeRequest();
		assertThat("request path", takeRequest(server).getPath(), is("/x"));
	}

	@Test
	public void documentGetSendsCookie() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.get(url(server));
		
		server.takeRequest();
		assertThat("cookie", takeRequest(server).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void documentGetWhenSubsequentRequestSendsCookie() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server.enqueue(new MockResponse());
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.get(url(server))
			.get(url(server));
		
		server.takeRequest();
		takeRequest(server);
		assertThat("cookie", takeRequest(server).getHeader("Cookie"), is("x=y"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getItem tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetItemReturnsItem() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='x'/>"
			+ "</body></html>"));
		server.play();
		
		MicrodataItem actual = newBrowser().get(url(server))
			.getItem("x");
		
		assertThat("item", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetItemWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body/></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getItem("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem.getType tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void itemGetTypeReturnsType() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("x")
			.getType();
		
		assertThat("item type", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem.getProperty tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void itemGetPropertyReturnsProperty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataProperty actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("x");
		
		assertThat("item property", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void itemGetPropertyWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getItem("i")
			.getProperty("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataProperty.getName tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void propertyGetNameReturnsName() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("x")
			.getName();
		
		assertThat("item property name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataProperty.getValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void propertyGetValueWhenMetaReturnsContent() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meta itemprop='p' content='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenMetaAndNoContentReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meta itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenAudioAndAbsoluteSrcReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenAudioAndRelativeSrcReturnsAbsoluteSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}
	
	@Test
	public void propertyGetValueWhenAudioAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenEmbedAndAbsoluteSrcReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenEmbedAndRelativeSrcReturnsAbsoluteSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}
	
	@Test
	public void propertyGetValueWhenEmbedAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenIframeAndAbsoluteSrcReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenIframeAndRelativeSrcReturnsAbsoluteSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}
	
	@Test
	public void propertyGetValueWhenIframeAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenImgAndAbsoluteSrcReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenImgAndRelativeSrcReturnsAbsoluteSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}
	
	@Test
	public void propertyGetValueWhenImgAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenSourceAndAbsoluteSrcReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenSourceAndRelativeSrcReturnsAbsoluteSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}
	
	@Test
	public void propertyGetValueWhenSourceAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenTrackAndAbsoluteSrcReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	// TODO: fix for Selenium
	@Ignore
	@Test
	public void propertyGetValueWhenTrackAndRelativeSrcReturnsAbsoluteSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}
	
	@Test
	public void propertyGetValueWhenTrackAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenVideoAndAbsoluteSrcReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p' src='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}
	
	@Test
	public void propertyGetValueWhenVideoAndRelativeSrcReturnsAbsoluteSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}
	
	@Test
	public void propertyGetValueWhenVideoAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenAnchorAndAbsoluteHrefReturnsUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenAnchorAndRelativeHrefReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}

	@Test
	public void propertyGetValueWhenAnchorAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenAreaAndAbsoluteHrefReturnsUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenAreaAndRelativeHrefReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}

	@Test
	public void propertyGetValueWhenAreaAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenLinkAndAbsoluteHrefReturnsUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p' href='http://x/'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("http://x/"));
	}

	@Test
	public void propertyGetValueWhenLinkAndRelativeHrefReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}

	@Test
	public void propertyGetValueWhenLinkAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	// TODO: fix for Selenium
	@Ignore
	@Test
	public void propertyGetValueWhenObjectReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<object itemprop='p' data='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(url(server, "/x")));
	}

	@Test
	public void propertyGetValueWhenObjectAndNoDataReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<object itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenDataReturnsValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<data itemprop='p' value='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenDataAndNoValueReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<data itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenMeterReturnsValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meter itemprop='p' value='1'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("1"));
	}
	
	@Test
	public void propertyGetValueWhenMeterAndNoValueReturnsZero() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meter itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("0"));
	}
	
	@Test
	public void propertyGetValueWhenTimeReturnsDatetime() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<time itemprop='p' datetime='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void propertyGetValueWhenTimeAndNoDatetimeReturnsText() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<time itemprop='p'>x</time>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void propertyGetValueWhenTimeAndNoTextReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<time itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenUnknownReturnsText() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenUnknownAndNoTextReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.hasLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentHasLinkWhenAnchorReturnsTrue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		boolean actual = newBrowser().get(url(server))
			.hasLink("x");
		
		assertThat("hasLink", actual, is(true));
	}

	@Test
	public void documentHasLinkWhenLinkReturnsTrue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		boolean actual = newBrowser().get(url(server))
			.hasLink("x");
		
		assertThat("hasLink", actual, is(true));
	}

	@Test
	public void documentHasLinkWhenNoFoundReturnsFalse() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body/></html>"));
		server.play();
		
		boolean actual = newBrowser().get(url(server))
			.hasLink("x");
		
		assertThat("hasLink", actual, is(false));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetLinkWhenAnchorReturnsLink() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		Link actual = newBrowser().get(url(server))
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test
	public void documentGetLinkWhenLinkReturnsLink() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		Link actual = newBrowser().get(url(server))
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetLinkWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body/></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getLink("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Link.getRel tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkGetRelWhenAnchorReturnsRelationship() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	@Test
	public void linkGetRelWhenLinkReturnsRelationship() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Link.getHref tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkGetHrefWhenAnchorAndAbsoluteHrefReturnsUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='http://x/'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is("http://x/"));
	}

	@Test
	public void linkGetHrefWhenAnchorAndRelativeHrefReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is(url(server, "/x")));
	}

	@Test
	public void linkGetHrefWhenAnchorAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, isEmptyString());
	}

	@Test
	public void linkGetHrefWhenLinkAndAbsoluteHrefReturnsUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='x' href='http://x/'/>"
			+ "</head></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("x")
			.getHref();
		
		assertThat("link href", actual, is("http://x/"));
	}

	@Test
	public void linkGetHrefWhenLinkAndRelativeHrefReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='x' href='x'/>"
			+ "</head></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("x")
			.getHref();
		
		assertThat("link href", actual, is(url(server, "/x")));
	}

	@Test
	public void linkGetHrefWhenLinkAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='r'/>"
			+ "</head></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, isEmptyString());
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Link.follow tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkFollowWhenAnchorSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/x'>a</a>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getLink("r")
			.follow();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test
	public void linkFollowWhenAnchorReturnsResponse() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataDocument actual = newBrowser().get(url(server))
			.getLink("r")
			.follow();
		
		server.takeRequest();
		assertThat("request", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getForm tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetFormReturnsForm() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server.play();
		
		Form actual = newBrowser().get(url(server))
			.getForm("x");
		
		assertThat("form", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetFormWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body/></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.getName tests
	// ----------------------------------------------------------------------------------------------------------------

	public void formGetNameReturnsName() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getForm("x")
			.getName();
		
		assertThat("form name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.getParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test(expected = IllegalArgumentException.class)
	public void formGetParameterWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.getParameter("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.setParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void formSetParameterSetsValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='p'/>"
			+ "</form>"
			+ "</body></html>"));
		server.play();
		
		Form form = newBrowser().get(url(server))
			.getForm("f");
		form.setParameter("p", "x");
		
		assertThat("form parameter value", form.getParameter("p"), is("x"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void formSetParameterWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.setParameter("p", "x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void formSubmitWhenSubmitInputSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test
	public void formSubmitWhenSubmitButtonSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test
	public void formSubmitWhenDefaultButtonSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button/>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test(expected = IllegalStateException.class)
	public void formSubmitWhenNoSubmitButtonThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
	}

	@Test
	public void formSubmitWhenNoMethodSubmitsGetRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(get("/x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsGetRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(get("/x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsTextControlValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='text' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(get("/a?p=x")));
	}

	@Test
	public void formSubmitWhenGetSendsCookie() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("cookie", takeRequest(server).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void formSubmitWhenGetSetsCookie() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit()
			.get(url(server));
		
		server.takeRequest();
		takeRequest(server);
		assertThat("cookie", takeRequest(server).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void formSubmitWhenGetReturnsResponse() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataDocument actual = newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}

	@Test
	public void formSubmitWhenPostSubmitsPostRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(post("/x")));
	}

	@Test
	public void formSubmitWhenPostSubmitsTextControlValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server.takeRequest();
		assertThat("request body", body(takeRequest(server)), is("p=x"));
	}

	@Test
	public void formSubmitWhenPostReturnsResponse() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataDocument actual = newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getCookie tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetCookieReturnsValue() throws IOException
	{
		server.enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetCookieWhenNotFoundThrowsException() throws IOException
	{
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getCookie("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Microbrowser newBrowser();
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static String url(MockWebServer server)
	{
		return url(server, "/");
	}
	
	private static String url(MockWebServer server, String path)
	{
		return server.getUrl(path).toString();
	}
	
	/**
	 * Workaround MockWebServer issue #11.
	 * 
	 * @see https://code.google.com/p/mockwebserver/issues/detail?id=11
	 */
	private static RecordedRequest takeRequest(MockWebServer server) throws InterruptedException
	{
		RecordedRequest request = server.takeRequest();
		
		while ("GET /favicon.ico HTTP/1.1".equals(request.getRequestLine()))
		{
			request = server.takeRequest();
		}
		
		return request;
	}
	
	private static String body(RecordedRequest request)
	{
		return new String(request.getBody(), Charset.forName("ISO-8859-1"));
	}
}
