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
import java.net.URL;

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.takeRequest;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Link}.
 * 
 * @param <T>
 *            the provider-specific link type
 */
public abstract class LinkTck<T> extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getRel tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getRelWhenAnchorReturnsRelationship()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	@Test
	public void getRelWhenLinkReturnsRelationship()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getHref tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getHrefWhenAnchorAndAbsoluteHrefReturnsUrl() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='http://x/'/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is(new URL("http://x/")));
	}

	@Test
	public void getHrefWhenAnchorAndRelativeHrefReturnsAbsoluteUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='x'/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is(server().getUrl("/x")));
	}

	@Test
	public void getHrefWhenAnchorAndNoHrefReturnsNull()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r'/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is(nullValue()));
	}

	@Test
	public void getHrefWhenLinkAndAbsoluteHrefReturnsUrl() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='x' href='http://x/'/>"
			+ "</head></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getLink("x")
			.getHref();
		
		assertThat("link href", actual, is(new URL("http://x/")));
	}

	@Test
	public void getHrefWhenLinkAndRelativeHrefReturnsAbsoluteUrl()
	{
		server().enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='x' href='x'/>"
			+ "</head></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getLink("x")
			.getHref();
		
		assertThat("link href", actual, is(server().getUrl("/x")));
	}

	@Test
	public void getHrefWhenLinkAndNoHrefReturnsNull()
	{
		server().enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='r'/>"
			+ "</head></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is(nullValue()));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// follow tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void followWhenAnchorSubmitsRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/x'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void followWhenAnchorSetsCookie()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		
		String actual = newBrowser().get(url(server()))
			.getLink("r")
			.follow()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void followWhenAnchorSendsCookie() throws InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void followWhenAnchorSendsPreviousCookie() throws InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<a rel='r1' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r2' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getLink("r1")
			.follow()
			.getLink("r2")
			.follow();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void followWhenAnchorReturnsResponse() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getLink("r")
			.follow();
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}
	
	@Test
	public void followWhenInvalidUrlThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='x:/a'>a</a>"
			+ "</body></html>"));
		
		Link link = newBrowser().get(url(server()))
			.getLink("r");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid URL: x:/a");
		
		link.follow();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapReturnsProvider()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		
		T actual = newBrowser().get(url(server()))
			.getLink("x")
			.unwrap(getProviderType());
		
		assertThat("link provider", actual, is(instanceOf(getProviderType())));
	}

	@Test
	public void unwrapWithUnknownTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		
		Link link = newBrowser().get(url(server()))
			.getLink("x");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot unwrap to: class java.lang.Void");
		
		link.unwrap(Void.class);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Class<T> getProviderType();
}
