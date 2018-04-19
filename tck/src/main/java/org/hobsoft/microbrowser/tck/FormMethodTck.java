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

import org.hamcrest.Matcher;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.takeRequest;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Form.submit} using a specific HTTP method.
 */
public abstract class FormMethodTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void submitSubmitsRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/x")));
	}
	
	@Test
	public void submitSubmitsHiddenControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSubmitsTextControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSubmitsTextControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='text' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSubmitsPasswordControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSubmitsPasswordControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='password' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSubmitsCheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=on")));
	}
	
	@Test
	public void submitSubmitsCheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=on")));
	}

	@Test
	public void submitDoesNotSubmitUncheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "")));
	}
	
	@Test
	public void submitDoesNotSubmitUncheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "")));
	}

	@Test
	public void submitSubmitsValuedCheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}
	
	@Test
	public void submitSubmitsValuedCheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitDoesNotSubmitValuedUncheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "")));
	}
	
	@Test
	public void submitDoesNotSubmitValuedUncheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "")));
	}
	
	@Test
	public void submitSubmitsCheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=on")));
	}
	
	@Test
	public void submitSubmitsCheckedRadioControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=on")));
	}

	@Test
	public void submitSubmitsCheckedRadioControlsLastValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=on")));
	}
	
	@Test
	public void submitDoesNotSubmitUncheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "")));
	}
	
	@Test
	public void submitSubmitsValuedCheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}
	
	@Test
	public void submitSubmitsValuedCheckedRadioControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSubmitsValuedCheckedRadioControlsLastValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "<input type='radio' name='c' value='y' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=y")));
	}
	
	@Test
	public void submitDoesNotSubmitValuedUncheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "")));
	}
	
	@Test
	public void submitSubmitsTextAreaControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<textarea name='c'>x</textarea>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSubmitsTextAreaControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<textarea name='c'></textarea>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(method("/a", "c=x")));
	}

	@Test
	public void submitSetsCookie()
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void submitSendsCookie() throws InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void submitSendsPreviousCookie() throws InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow()
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void submitReturnsResponse() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody(String.format("<html><body>"
			+ "<form name='f' method='%s' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>", getMethod())));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract String getMethod();
	
	protected abstract Matcher<RecordedRequest> method(String expectedPath);
	
	protected abstract Matcher<RecordedRequest> method(String expectedPath, String expectedData);
}
