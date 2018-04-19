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
import java.util.List;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.LinkNotFoundException;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.hobsoft.microbrowser.MicrodataPropertyNotFoundException;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.link;
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
	public void getIdReturnsId() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y' itemid='http://x'/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getItem("http://y")
			.getId();
		
		assertThat("item id", actual, is(new URL("http://x")));
	}
	
	@Test
	public void getIdReturnsTrimmedId() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y' itemid=' http://x '/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getItem("http://y")
			.getId();
		
		assertThat("item id", actual, is(new URL("http://x")));
	}
	
	@Test
	public void getIdWhenInvalidReturnsNull()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y' itemid='x'/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getItem("http://y")
			.getId();
		
		assertThat("item id", actual, is(nullValue()));
	}
	
	@Test
	public void getIdWhenNotFoundReturnsNull()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://y'/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getItem("http://y")
			.getId();
		
		assertThat("item id", actual, is(nullValue()));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getType tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void getTypeReturnsType() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x'/>"
			+ "</body></html>"));
		
		URL actual = newBrowser().get(url(server()))
			.getItem("http://x")
			.getType();
		
		assertThat("item type", actual, is(new URL("http://x")));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getProperty tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getPropertyReturnsProperty()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		MicrodataProperty actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getProperty("x");
		
		assertThat("item property", actual.getName(), is("x"));
	}

	@Test
	public void getPropertyWithUnknownNameThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		
		MicrodataItem item = newBrowser().get(url(server()))
			.getItem("http://i");
		
		thrown().expect(MicrodataPropertyNotFoundException.class);
		thrown().expectMessage("x");
		
		item.getProperty("x");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinkWhenAnchorReturnsLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		Link actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test
	public void getLinkWhenLinkReturnsLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		Link actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test
	public void getLinkWithUnknownRelThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		
		MicrodataItem item = newBrowser().get(url(server()))
			.getItem("http://i");
		
		thrown().expect(LinkNotFoundException.class);
		thrown().expectMessage("x");
		
		item.getLink("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getLinks tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinksWhenAnchorReturnsLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getLinks("x");
		
		assertThat("links", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenAnchorsReturnsLinks() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<a rel='x' href='http://y/'/>"
			+ "<a rel='x' href='http://z/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getLinks("x");
		
		assertThat("links", actual, contains(
			link("x", "http://y/"),
			link("x", "http://z/")
		));
	}

	@Test
	public void getLinksWhenLinkReturnsLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link rel='x' href='http://y/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getLinks("x");
		
		assertThat("link", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenLinksReturnsLinks() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<link rel='x' href='http://y/'/>"
			+ "<link rel='x' href='http://z/'/>"
			+ "</div>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getLinks("x");
		
		assertThat("link", actual, contains(
			link("x", "http://y/"),
			link("x", "http://z/")
		));
	}

	@Test
	public void getLinksWithUnknownRelReturnsEmptyList()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getLinks("x");
		
		assertThat("link", actual, is(empty()));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getForm tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getFormReturnsForm()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<form name='x'/>"
			+ "</div>"
			+ "</body></html>"));
		
		Form actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.getForm("x");
		
		assertThat("form", actual.getName(), is("x"));
	}

	@Test
	public void getFormRetainsSetControlValues()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'>"
			+ "<form name='f'>"
			+ "<input type='text' name='c'/>"
			+ "</form>"
			+ "</div>"
			+ "</body></html>"));
		
		MicrodataItem item = newBrowser().get(url(server()))
			.getItem("http://i");
		item.getForm("f")
			.setControlValue("c", "x");
		
		assertThat("form control value", item.getForm("f").getControlValue("c"), is("x"));
	}

	@Test
	public void getFormWhenNotFoundThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		
		MicrodataItem item = newBrowser().get(url(server()))
			.getItem("http://i");
		
		thrown().expect(FormNotFoundException.class);
		thrown().expectMessage("x");
		
		item.getForm("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapWithUnknownTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		
		MicrodataItem item = newBrowser().get(url(server()))
			.getItem("http://i");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot unwrap to: class java.lang.Void");
		
		item.unwrap(Void.class);
	}
}
