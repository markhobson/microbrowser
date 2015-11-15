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

import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerMatchers.post;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.takeRequest;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestUtils.body;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Form.submit} using the POST method.
 */
public abstract class FormPostTck extends FormMethodTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void submitWhenPostSubmitsPostRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(post("/x")));
	}

	@Test
	public void submitWhenPostSubmitsHiddenControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsTextControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsTextControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsPasswordControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsPasswordControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='password' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsCheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=on"));
	}

	@Test
	public void submitWhenPostSubmitsCheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=on"));
	}

	@Test
	public void submitWhenPostDoesNotSubmitUncheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostDoesNotSubmitUncheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}

	@Test
	public void submitWhenPostSubmitsValuedCheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}
	
	@Test
	public void submitWhenPostSubmitsValuedCheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostDoesNotSubmitValuedUncheckedCheckboxControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostDoesNotSubmitValuedUncheckedCheckboxControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostSubmitsCheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=on"));
	}
	
	@Test
	public void submitWhenPostSubmitsCheckedRadioControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=on"));
	}

	@Test
	public void submitWhenPostSubmitsCheckedRadioControlsLastValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=on"));
	}
	
	@Test
	public void submitWhenPostDoesNotSubmitUncheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostSubmitsValuedCheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}
	
	@Test
	public void submitWhenPostSubmitsValuedCheckedRadioControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsValuedCheckedRadioControlsLastValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "<input type='radio' name='c' value='y' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=y"));
	}

	@Test
	public void submitWhenPostDoesNotSubmitValuedUncheckedRadioControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostSubmitsTextAreaControlInitialValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<textarea name='c'>x</textarea>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsTextAreaControlSetValue() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<textarea name='c'></textarea>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSetsCookie()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void submitWhenPostSendsCookie() throws InterruptedException
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void submitWhenPostSendsPreviousCookie() throws InterruptedException
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
		
		newBrowser().get(url(server()))
			.getLink("r")
			.follow()
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void submitWhenPostReturnsResponse() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}
}
