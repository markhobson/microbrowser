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

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.MicrodataItem;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Microbrowser}.
 */
public abstract class MicrobrowserTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// Microbrowser.get tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getRequestsPath() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server(), "/x"));
		
		assertThat("request path", server().takeRequest().getPath(), is("/x"));
	}

	@Test
	public void getSetsCookie() throws IOException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void getReturnsResponse() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument actual = newBrowser().get(url(server()));
		
		assertThat("response", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getItem tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetItemReturnsItem() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='x'/>"
			+ "</body></html>"));
		server().play();
		
		MicrodataItem actual = newBrowser().get(url(server()))
			.getItem("x");
		
		assertThat("item", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetItemWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getItem("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.hasLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentHasLinkWhenAnchorReturnsTrue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server().play();
		
		boolean actual = newBrowser().get(url(server()))
			.hasLink("x");
		
		assertThat("hasLink", actual, is(true));
	}

	@Test
	public void documentHasLinkWhenLinkReturnsTrue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server().play();
		
		boolean actual = newBrowser().get(url(server()))
			.hasLink("x");
		
		assertThat("hasLink", actual, is(true));
	}

	@Test
	public void documentHasLinkWhenNoFoundReturnsFalse() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		boolean actual = newBrowser().get(url(server()))
			.hasLink("x");
		
		assertThat("hasLink", actual, is(false));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetLinkWhenAnchorReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test
	public void documentGetLinkWhenLinkReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetLinkWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getLink("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getForm tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetFormReturnsForm() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().play();
		
		Form actual = newBrowser().get(url(server()))
			.getForm("x");
		
		assertThat("form", actual, is(notNullValue()));
	}

	@Test
	public void documentGetFormCachesForm() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument document = newBrowser().get(url(server()));
		
		assertThat("form", document.getForm("x"), is(sameInstance(document.getForm("x"))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetFormWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getCookie tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetCookieReturnsValue() throws IOException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetCookieWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getCookie("x");
	}
}
