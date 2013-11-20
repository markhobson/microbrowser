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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

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
		
		assertEquals("request path", "/x", server.takeRequest().getPath());
	}

	@Test
	public void itemPropertyValueWhenTextReturnsValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://a/'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(server.getUrl("/").toString())
			.getItem("http://a/")
			.getProperty("p")
			.getValue();
		
		assertEquals("item property value", "x", actual);
	}

	@Test
	public void formSubmitPostsControlValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/b'>"
			+ "<input type='text' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		MicrodataDocument document = newBrowser().get(server.getUrl("/").toString());
		document.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server.takeRequest();
		
		RecordedRequest request = takeRequest(server);
		assertEquals("request path", "/b", request.getPath());
		assertEquals("request method", "POST", request.getMethod());
		assertEquals("request body", "p=x", new String(request.getBody()));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Microbrowser newBrowser();
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

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
