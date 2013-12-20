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
import org.hobsoft.microbrowser.MicrodataProperty;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.takeRequest;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestMatcher.get;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestMatcher.post;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestUtils.body;
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

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.get tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetRequestsPath() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse());
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.get(url(server(), "/x"));
		
		server().takeRequest();
		assertThat("request path", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void documentGetSetsCookie() throws IOException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void documentGetSendsCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.get(url(server()));
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void documentGetSendsPreviousCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().enqueue(new MockResponse());
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.get(url(server()))
			.get(url(server()));
		
		server().takeRequest();
		takeRequest(server());
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
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
	// MicrodataItem.getType tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void itemGetTypeReturnsType() throws IOException
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
	// MicrodataItem.getProperty tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void itemGetPropertyReturnsProperty() throws IOException
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
	public void itemGetPropertyWhenNotFoundThrowsException() throws IOException
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
	// Link.getRel tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkGetRelWhenAnchorReturnsRelationship() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	@Test
	public void linkGetRelWhenLinkReturnsRelationship() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Link.getHref tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkGetHrefWhenAnchorAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='http://x/'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is("http://x/"));
	}

	@Test
	public void linkGetHrefWhenAnchorAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='x'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, is(url(server(), "/x")));
	}

	@Test
	public void linkGetHrefWhenAnchorAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, isEmptyString());
	}

	@Test
	public void linkGetHrefWhenLinkAndAbsoluteHrefReturnsUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='x' href='http://x/'/>"
			+ "</head></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("x")
			.getHref();
		
		assertThat("link href", actual, is("http://x/"));
	}

	@Test
	public void linkGetHrefWhenLinkAndRelativeHrefReturnsAbsoluteUrl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='x' href='x'/>"
			+ "</head></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("x")
			.getHref();
		
		assertThat("link href", actual, is(url(server(), "/x")));
	}

	@Test
	public void linkGetHrefWhenLinkAndNoHrefReturnsEmpty() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><head>"
			+ "<link rel='r'/>"
			+ "</head></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("r")
			.getHref();
		
		assertThat("link href", actual, isEmptyString());
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Link.follow tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkFollowWhenAnchorSubmitsRequest() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/x'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void linkFollowWhenAnchorSetsCookie() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getLink("r")
			.follow()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void linkFollowWhenAnchorSendsCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void linkFollowWhenAnchorSendsPreviousCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.get(url(server()))
			.getLink("r")
			.follow();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void linkFollowWhenAnchorReturnsResponse() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getLink("r")
			.follow();
		
		server().takeRequest();
		assertThat("request", actual.getItem("i").getProperty("p").getValue(), is("x"));
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

	@Test(expected = IllegalArgumentException.class)
	public void documentGetFormWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.getName tests
	// ----------------------------------------------------------------------------------------------------------------

	public void formGetNameReturnsName() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("x")
			.getName();
		
		assertThat("form name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.getParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void formGetParameterWhenTextControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "<input type='text' name='x' value='y'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getParameter("x");
		assertThat("form parameter value", actual, is("y"));
	}

	@Test
	public void formGetParameterWhenTextControlReturnsSetValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "<input type='text' name='x' value='y'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.setParameter("x", "z")
			.getParameter("x");
		assertThat("form parameter value", actual, is("z"));
	}

	@Test
	public void formGetParameterWhenPasswordControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "<input type='password' name='x' value='y'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getParameter("x");
		assertThat("form parameter value", actual, is("y"));
	}

	@Test
	public void formGetParameterWhenPasswordControlReturnsSetValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "<input type='password' name='x' value='y'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.setParameter("x", "z")
			.getParameter("x");
		assertThat("form parameter value", actual, is("z"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void formGetParameterWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.getParameter("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.setParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void formSetParameterWhenTextControlSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='p'/>"
			+ "</form>"
			+ "</body></html>"));
		server().play();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		form.setParameter("p", "x");
		
		assertThat("form parameter value", form.getParameter("p"), is("x"));
	}

	@Test
	public void formSetParameterWhenPasswordControlSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='password' name='p'/>"
			+ "</form>"
			+ "</body></html>"));
		server().play();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		form.setParameter("p", "x");
		
		assertThat("form parameter value", form.getParameter("p"), is("x"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void formSetParameterWhenNotFoundThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setParameter("p", "x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void formSubmitWhenSubmitInputSubmitsRequest() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void formSubmitWhenSubmitButtonSubmitsRequest() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void formSubmitWhenDefaultButtonSubmitsRequest() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test(expected = IllegalStateException.class)
	public void formSubmitWhenNoSubmitButtonThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
	}

	@Test
	public void formSubmitWhenNoMethodSubmitsGetRequest() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsGetRequest() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsTextControlInitialValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='text' name='p' value='x'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?p=x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsTextControlSetValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='text' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?p=x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsPasswordControlInitialValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='password' name='p' value='x'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?p=x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsPasswordControlSetValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='password' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?p=x")));
	}

	@Test
	public void formSubmitWhenGetSetsCookie() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void formSubmitWhenGetSendsCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void formSubmitWhenGetSendsPreviousCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void formSubmitWhenGetReturnsResponse() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}

	@Test
	public void formSubmitWhenPostSubmitsPostRequest() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(post("/x")));
	}

	@Test
	public void formSubmitWhenPostSubmitsTextControlInitialValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='p' value='x'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("p=x"));
	}

	@Test
	public void formSubmitWhenPostSubmitsTextControlSetValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("p=x"));
	}

	@Test
	public void formSubmitWhenPostSubmitsPasswordControlInitialValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='password' name='p' value='x'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("p=x"));
	}

	@Test
	public void formSubmitWhenPostSubmitsPasswordControlSetValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='password' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("p=x"));
	}

	@Test
	public void formSubmitWhenPostSetsCookie() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void formSubmitWhenPostSendsCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void formSubmitWhenPostSendsPreviousCookie() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void formSubmitWhenPostReturnsResponse() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i").getProperty("p").getValue(), is("x"));
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
