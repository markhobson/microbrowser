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
import java.net.URL;
import java.util.List;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.LinkNotFoundException;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hobsoft.microbrowser.tck.support.LinkMatcher.link;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code MicrodataItem}.
 */
public abstract class MicrodataItemTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getId tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getIdReturnsId() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y' itemid='http://x'/>"
			+ "</body></html>"));
		server().play();
		
		URL actual = newBrowser().get(url(server()))
			.getItem(new URL("http://y"))
			.getId();
		
		assertThat("item id", actual, is(new URL("http://x")));
	}
	
	@Test
	public void getIdReturnsTrimmedId() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y' itemid=' http://x '/>"
			+ "</body></html>"));
		server().play();
		
		URL actual = newBrowser().get(url(server()))
			.getItem(new URL("http://y"))
			.getId();
		
		assertThat("item id", actual, is(new URL("http://x")));
	}
	
	@Test
	public void getIdWhenInvalidReturnsNull() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y' itemid='x'/>"
			+ "</body></html>"));
		server().play();
		
		URL actual = newBrowser().get(url(server()))
			.getItem(new URL("http://y"))
			.getId();
		
		assertThat("item id", actual, is(nullValue()));
	}
	
	@Test
	public void getIdWhenNotFoundReturnsNull() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y'/>"
			+ "</body></html>"));
		server().play();
		
		URL actual = newBrowser().get(url(server()))
			.getItem(new URL("http://y"))
			.getId();
		
		assertThat("item id", actual, is(nullValue()));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getType tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void getTypeReturnsType() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x'/>"
			+ "</body></html>"));
		server().play();
		
		URL actual = newBrowser().get(url(server()))
			.getItem(new URL("http://x"))
			.getType();
		
		assertThat("item type", actual, is(new URL("http://x")));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getProperty tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getPropertyReturnsProperty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		MicrodataProperty actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getProperty("x");
		
		assertThat("item property", actual.getName(), is("x"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getPropertyWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getProperty("x");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinkWhenAnchorReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test
	public void getLinkWhenLinkReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test(expected = LinkNotFoundException.class)
	public void getLinkWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLink("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getLinks tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinksWhenAnchorReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLinks("x");
		
		assertThat("links", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenAnchorsReturnsLinks() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a rel='x' href='http://y/'/>"
			+ "<a rel='x' href='http://z/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLinks("x");
		
		assertThat("links", actual, contains(
			link("x", "http://y/"),
			link("x", "http://z/")
		));
	}

	@Test
	public void getLinksWhenLinkReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLinks("x");
		
		assertThat("link", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenLinksReturnsLinks() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link rel='x' href='http://y/'/>"
			+ "<link rel='x' href='http://z/'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLinks("x");
		
		assertThat("link", actual, contains(
			link("x", "http://y/"),
			link("x", "http://z/")
		));
	}

	@Test
	public void getLinkWhenNotFoundReturnsEmptyList() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getLinks("x");
		
		assertThat("link", actual, is(empty()));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getForm tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getFormReturnsForm() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<form name='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		Form actual = newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getForm("x");
		
		assertThat("form", actual.getName(), is("x"));
	}

	@Test
	public void getFormCachesForm() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<form name='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		MicrodataItem item = newBrowser().get(url(server()))
			.getItem(new URL("http://i"));
		
		assertThat("form", item.getForm("x"), is(sameInstance(item.getForm("x"))));
	}

	@Test(expected = FormNotFoundException.class)
	public void getFormWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getItem(new URL("http://i"))
			.getForm("x");
	}
}
