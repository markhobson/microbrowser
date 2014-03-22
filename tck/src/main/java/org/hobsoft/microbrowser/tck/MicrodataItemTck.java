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

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code MicrodataItem}.
 */
public abstract class MicrodataItemTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getType tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getTypeReturnsType() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='x'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getItem("x")
			.getType();
		
		assertThat("item type", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getProperty tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getPropertyReturnsProperty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		MicrodataProperty actual = newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("x");
		
		assertThat("item property", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getPropertyWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getItem("i")
			.getProperty("x");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// hasLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void hasLinkWhenAnchorReturnsTrue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a rel='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		boolean actual = newBrowser().get(url(server()))
			.getItem("i")
			.hasLink("x");
		
		assertThat("hasLink", actual, is(true));
	}

	@Test
	public void hasLinkWhenLinkReturnsTrue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link rel='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		boolean actual = newBrowser().get(url(server()))
			.getItem("i")
			.hasLink("x");
		
		assertThat("hasLink", actual, is(true));
	}

	@Test
	public void hasLinkWhenNoFoundReturnsFalse() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'/>"
			+ "</body></html>"));
		server().play();
		
		boolean actual = newBrowser().get(url(server()))
			.getItem("i")
			.hasLink("x");
		
		assertThat("hasLink", actual, is(false));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getLinkWhenAnchorReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a rel='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getItem("i")
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test
	public void getLinkWhenLinkReturnsLink() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link rel='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		Link actual = newBrowser().get(url(server()))
			.getItem("i")
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getLinkWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getItem("i")
			.getLink("x");
	}
}
