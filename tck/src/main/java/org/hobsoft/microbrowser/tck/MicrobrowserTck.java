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

import java.net.MalformedURLException;

import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Microbrowser}.
 */
public abstract class MicrobrowserTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// get tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getRequestsPath() throws InterruptedException
	{
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server(), "/x"));
		
		assertThat("request path", server().takeRequest().getPath(), is("/x"));
	}

	@Test
	public void getSetsCookie()
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		
		String actual = newBrowser().get(url(server()))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void getReturnsResponse() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		
		MicrodataDocument actual = newBrowser().get(url(server()));
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}
	
	@Test
	public void getWithInvalidUrlThrowsException()
	{
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid URL: x");
		
		newBrowser().get("x");
	}
	
	@Test
	public void getWhenNotFoundReturnsResponse() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setResponseCode(404).setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		
		MicrodataDocument actual = newBrowser().get(url(server()));
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}
	
	@Test
	public void getWhenInternalErrorReturnsResponse() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setResponseCode(500).setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		
		MicrodataDocument actual = newBrowser().get(url(server()));
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}
}
