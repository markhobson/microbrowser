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
import java.util.List;

import org.hobsoft.microbrowser.CookieNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.FormNotFoundException;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.LinkNotFoundException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataItemNotFoundException;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.item;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.link;
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
	public void getItemWhenItemReturnsItem() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "</body></html>"));
		
		MicrodataItem actual = newBrowser().get(url(server()))
			.getItem("http://x");
		
		assertThat("item", actual, is(item("http://y")));
	}
	
	@Test
	public void getItemWhenItemsReturnsFirstItem() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://z'/>"
			+ "</body></html>"));
		
		MicrodataItem actual = newBrowser().get(url(server()))
			.getItem("http://x");
		
		assertThat("item", actual, is(item("http://y")));
	}

	@Test
	public void getItemWithUnknownTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		MicrodataDocument document = newBrowser().get(url(server()));
		
		thrown().expect(MicrodataItemNotFoundException.class);
		thrown().expectMessage("http://x");
		
		document.getItem("http://x");
	}
	
	@Test
	public void getItemWithInvalidTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));

		MicrodataDocument document = newBrowser().get(url(server()));
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("x");
		
		document.getItem("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getItems tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getItemsWhenItemReturnsItem() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "</body></html>"));
		
		List<MicrodataItem> actual = newBrowser().get(url(server()))
			.getItems("http://x");
		
		assertThat("items", actual, contains(item("http://y")));
	}
	
	@Test
	public void getItemsWhenItemsReturnsItems() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://y'/>"
			+ "<div itemscope='itemscope' itemtype='http://x' itemid='http://z'/>"
			+ "</body></html>"));
		
		List<MicrodataItem> actual = newBrowser().get(url(server()))
			.getItems("http://x");
		
		assertThat("items", actual, contains(
			item("http://y"),
			item("http://z")
		));
	}
	
	@Test
	public void getItemsWithUnknownTypeReturnsEmptyList()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		List<MicrodataItem> actual = newBrowser().get(url(server()))
			.getItems("http://x");
		
		assertThat("items", actual, is(empty()));
	}
	
	@Test
	public void getItemsWithInvalidTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		MicrodataDocument document = newBrowser().get(url(server()));

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("x");
		
		document.getItems("x");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinkWhenAnchorReturnsLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</body></html>"));
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}
	
	@Test
	public void getLinkWhenAnchorsReturnsFirstLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "<a rel='x' href='http://z/'/>"
			+ "</body></html>"));
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test
	public void getLinkWhenLinkReturnsLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x' href='http://y/'/>"
			+ "</body></html>"));
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}
	
	@Test
	public void getLinkWhenLinksReturnsFirstLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x' href='http://y/'/>"
			+ "<link rel='x' href='http://z/'/>"
			+ "</body></html>"));
		
		Link actual = newBrowser().get(url(server()))
			.getLink("x");
		
		assertThat("link", actual, is(link("x", "http://y/")));
	}

	@Test
	public void getLinkWithUnknownRelThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		MicrodataDocument document = newBrowser().get(url(server()));

		thrown().expect(LinkNotFoundException.class);
		thrown().expectMessage("x");
		
		document.getLink("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getLinks tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinksWhenAnchorReturnsLink() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenAnchorsReturnsLinks() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x' href='http://y/'/>"
			+ "<a rel='x' href='http://z/'/>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
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
			+ "<link rel='x' href='http://y/'/>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, contains(link("x", "http://y/")));
	}
	
	@Test
	public void getLinksWhenLinksReturnsLinks() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x' href='http://y/'/>"
			+ "<link rel='x' href='http://z/'/>"
			+ "</body></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, contains(
			link("x", "http://y/"),
			link("x", "http://z/")
		));
	}

	@Test
	public void getLinksWithUnknownRelReturnsEmptyList()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		List<Link> actual = newBrowser().get(url(server()))
			.getLinks("x");
		
		assertThat("links", actual, is(empty()));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getForm tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getFormReturnsForm()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		
		Form actual = newBrowser().get(url(server()))
			.getForm("x");
		
		assertThat("form", actual.getName(), is("x"));
	}

	@Test
	public void getFormRetainsSetControlValues()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		
		MicrodataDocument document = newBrowser().get(url(server()));
		document.getForm("f")
			.setControlValue("c", "x");
		
		assertThat("form control value", document.getForm("f").getControlValue("c"), is("x"));
	}

	@Test
	public void getFormWithUnknownNameThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		MicrodataDocument document = newBrowser().get(url(server()));
		
		thrown().expect(FormNotFoundException.class);
		thrown().expectMessage("x");
		
		document.getForm("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getCookie tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getCookieReturnsValue()
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		
		String actual = newBrowser().get(url(server()))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void getCookieWithUnknownNameThrowsException()
	{
		server().enqueue(new MockResponse());
		
		MicrodataDocument document = newBrowser().get(url(server()));
		
		thrown().expect(CookieNotFoundException.class);
		thrown().expectMessage("x");
		
		document.getCookie("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapWithUnknownTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		MicrodataDocument document = newBrowser().get(url(server()));

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot unwrap to: class java.lang.Void");
		
		document.unwrap(Void.class);
	}
}
