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

	@Test
	public void formGetParameterWhenHiddenControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "<input type='hidden' name='x' value='y'/>"
			+ "</body></html>"));
		server().play();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getParameter("x");
		assertThat("form parameter value", actual, is("y"));
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
			+ "<input type='submit'/>"
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
			+ "<button type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
	public void formSubmitWhenGetSubmitsHiddenControlInitialValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='hidden' name='p' value='x'/>"
			+ "<input type='submit'/>"
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
	public void formSubmitWhenGetSetsCookie() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow()
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
	public void formSubmitWhenPostSubmitsHiddenControlInitialValue() throws IOException, InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='hidden' name='p' value='x'/>"
			+ "<input type='submit'/>"
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
	public void formSubmitWhenPostSetsCookie() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'/>"
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
			+ "<input type='submit'/>"
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
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().play();
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow()
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
			+ "<input type='submit'/>"
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
