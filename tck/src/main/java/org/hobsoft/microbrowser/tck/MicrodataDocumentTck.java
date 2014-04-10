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

import org.hobsoft.microbrowser.CookieNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.LinkNotFoundException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataItemNotFoundException;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hobsoft.microbrowser.tck.support.LinkMatcher.link;
import static org.hobsoft.microbrowser.tck.support.MicrodataItemMatcher.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code MicrodataDocument}.
 */
public abstract class MicrodataDocumentTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getItem tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getItemWhenItemReturnsItem() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "</body></html>"));
		server().play();
		
		MicrodataItem actual = newBrowser().get(url(server()))
			.getItem(new URL("http://x"));
		
		assertThat("item", actual, is(item("http://y")));
	}
	
	@Test
	public void getItemWhenItemsReturnsFirstItem() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://z'/>"
			+ "</body></html>"));
		server().play();
		
		MicrodataItem actual = newBrowser().get(url(server()))
			.getItem(new URL("http://x"));
		
		assertThat("item", actual, is(item("http://y")));
	}

	@Test(expected = MicrodataItemNotFoundException.class)
	public void getItemWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getItem(new URL("http://x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getItems tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getItemsWhenItemReturnsItem() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "</body></html>"));
		server().play();
		
		List<MicrodataItem> actual = newBrowser().get(url(server()))
			.getItems(new URL("http://x"));
		
		assertThat("items", actual, contains(item("http://y")));
	}
	
	@Test
	public void getItemsWhenItemsReturnsItems() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://z'/>"
			+ "</body></html>"));
		server().play();
		
		List<MicrodataItem> actual = newBrowser().get(url(server()))
			.getItems(new URL("http://x"));
		
		assertThat("items", actual, contains(
			item("http://y"),
			item("http://z")
		));
	}
	
	@Test
	public void getItemsWhenNotFoundReturnsEmptyList() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		List<MicrodataItem> actual = newBrowser().get(url(server()))
			.getItems(new URL("http://x"));
		
		assertThat("items", actual, is(empty()));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinkWhenAnchorReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}
	
	@Test
	public void getLinkWhenAnchorsReturnsFirstLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "<a rel='x' href='http://z/'/>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test
	public void getLinkWhenLinkReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x' href='http://y/'/>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}
	
	@Test
	public void getLinkWhenLinksReturnsFirstLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x' href='http://y/'/>"
			+ "<link rel='x' href='http://z/'/>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test(expected = LinkNotFoundException.class)
	public void getLinkWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getLink("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getLinks tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinksWhenAnchorReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenAnchorsReturnsLinks() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "<a rel='x' href='http://z/'/>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
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
			+ "<link rel='x' href='http://y/'/>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenLinksReturnsLinks() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x' href='http://y/'/>"
			+ "<link rel='x' href='http://z/'/>"
			+ "</body></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, contains(
			link("x", "http://y/"),
			link("x", "http://z/")
		));
	}

	@Test
	public void getLinksWhenNotFoundReturnsEmptyList() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, is(empty()));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getForm tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getFormReturnsForm() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().play();
		
		Form actual = newBrowser().get(url(server()))
			.getForm("x");
		
		assertThat("form", actual.getName(), is("x"));
	}

	@Test
	public void getFormCachesForm() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument document = newBrowser().get(url(server()));
		
		assertThat("form", document.getForm("x"), is(sameInstance(document.getForm("x"))));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFormWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getCookie tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getCookieReturnsValue() throws IOException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test(expected = CookieNotFoundException.class)
	public void getCookieWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getCookie("x");
	}
}
