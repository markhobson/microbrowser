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

import org.hobsoft.microbrowser.Microbrowser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hobsoft.microbrowser.tck.RecordedRequestMatcher.get;
import static org.hobsoft.microbrowser.tck.RecordedRequestMatcher.post;
import static org.junit.Assert.assertEquals;
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
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getRequestsPath() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(server.getUrl("/x").toString());
		
		assertThat("request path", server.takeRequest().getPath(), is("/x"));
	}

	@Test
	public void itemPropertyValueWhenAnchorReturnsAbsoluteHref() throws IOException, InterruptedException
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
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void itemPropertyValueWhenLinkReturnsAbsoluteHref() throws IOException, InterruptedException
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
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void itemPropertyValueWhenUnknownReturnsText() throws IOException, InterruptedException
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
	public void formSubmitWhenNoMethodSubmitsGetRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(get("/a")));
	}

	@Test
	public void formSubmitWhenGetSubmitsControlValue() throws IOException, InterruptedException
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
	public void formSubmitWhenPostSubmitsControlValue() throws IOException, InterruptedException
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
		RecordedRequest actual = takeRequest(server);
		assertThat("request", actual, is(post("/a")));
		assertEquals("request body", "p=x", new String(actual.getBody(), Charset.forName("UTF-8")));
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
		return server.getUrl("/").toString();
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
}
