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
import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.MicrodataItemMatcher.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.takeRequest;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestMatcher.get;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestMatcher.post;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestUtils.body;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Form}.
 */
public abstract class FormTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getName tests
	// ----------------------------------------------------------------------------------------------------------------

	public void getNameReturnsName() throws IOException
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
	// getParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getParameterWhenTextControlReturnsInitialValue() throws IOException
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
	public void getParameterWhenTextControlReturnsSetValue() throws IOException
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
	public void getParameterWhenPasswordControlReturnsInitialValue() throws IOException
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
	public void getParameterWhenPasswordControlReturnsSetValue() throws IOException
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
	public void getParameterWhenHiddenControlReturnsInitialValue() throws IOException
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
	public void getParameterWhenNotFoundThrowsException() throws IOException
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
	// setParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void setParameterWhenTextControlSetsValue() throws IOException
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
	public void setParameterWhenPasswordControlSetsValue() throws IOException
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
	public void setParameterWhenNotFoundThrowsException() throws IOException
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
	// submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void submitWhenSubmitInputSubmitsRequest() throws IOException, InterruptedException
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
	public void submitWhenSubmitButtonSubmitsRequest() throws IOException, InterruptedException
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
	public void submitWhenDefaultButtonSubmitsRequest() throws IOException, InterruptedException
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
	public void submitWhenNoSubmitButtonThrowsException() throws IOException
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
	public void submitWhenNoMethodSubmitsGetRequest() throws IOException, InterruptedException
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
	public void submitWhenGetSubmitsGetRequest() throws IOException, InterruptedException
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
	public void submitWhenGetSubmitsTextControlInitialValue() throws IOException, InterruptedException
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
	public void submitWhenGetSubmitsTextControlSetValue() throws IOException, InterruptedException
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
	public void submitWhenGetSubmitsPasswordControlInitialValue() throws IOException, InterruptedException
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
	public void submitWhenGetSubmitsPasswordControlSetValue() throws IOException, InterruptedException
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
	public void submitWhenGetSubmitsHiddenControlInitialValue() throws IOException, InterruptedException
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
	public void submitWhenGetSetsCookie() throws IOException
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
	public void submitWhenGetSendsCookie() throws IOException, InterruptedException
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
	public void submitWhenGetSendsPreviousCookie() throws IOException, InterruptedException
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
	public void submitWhenGetReturnsResponse() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i' itemid='http://x'/>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i"), is(item("http://x")));
	}

	@Test
	public void submitWhenPostSubmitsPostRequest() throws IOException, InterruptedException
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
	public void submitWhenPostSubmitsTextControlInitialValue() throws IOException, InterruptedException
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
	public void submitWhenPostSubmitsTextControlSetValue() throws IOException, InterruptedException
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
	public void submitWhenPostSubmitsPasswordControlInitialValue() throws IOException, InterruptedException
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
	public void submitWhenPostSubmitsPasswordControlSetValue() throws IOException, InterruptedException
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
	public void submitWhenPostSubmitsHiddenControlInitialValue() throws IOException, InterruptedException
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
	public void submitWhenPostSetsCookie() throws IOException
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
	public void submitWhenPostSendsCookie() throws IOException, InterruptedException
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
	public void submitWhenPostSendsPreviousCookie() throws IOException, InterruptedException
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
	public void submitWhenPostReturnsResponse() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i' itemid='http://x'/>"
			+ "</body></html>"));
		server().play();
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i"), is(item("http://x")));
	}
}
