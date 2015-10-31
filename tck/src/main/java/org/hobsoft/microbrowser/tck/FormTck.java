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

import org.hobsoft.microbrowser.Control;
import org.hobsoft.microbrowser.ControlGroup;
import org.hobsoft.microbrowser.ControlNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerMatchers.get;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerMatchers.post;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.takeRequest;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.RecordedRequestUtils.body;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Form}.
 * 
 * @param <T>
 *            the provider-specific form type
 */
public abstract class FormTck<T> extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getName tests
	// ----------------------------------------------------------------------------------------------------------------

	public void getNameReturnsName() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("x")
			.getName();
		
		assertThat("form name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getControl tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getControlReturnsControl() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("x");
		
		assertThat("form control", actual.getName(), is("x"));
	}
	
	@Test
	public void getControlWithUnknownNameThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(ControlNotFoundException.class);
		thrown().expectMessage("x");
		
		form.getControl("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getControlValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getControlValueReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlValue("x");
		
		assertThat("form control value", actual, is("y"));
	}
	
	@Test
	public void getControlValueWithUnknownNameThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(ControlNotFoundException.class);
		thrown().expectMessage("x");
		
		form.getControlValue("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// setControlValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void setControlValueSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		form.setControlValue("x", "y");
		
		assertThat("form control value", form.getControlValue("x"), is("y"));
	}

	@Test
	public void setControlValueWithUnknownNameThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(ControlNotFoundException.class);
		thrown().expectMessage("x");
		
		form.setControlValue("x", "y");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getControlGroup tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getControlGroupReturnsControlGroup() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		ControlGroup actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("x");
		
		assertThat("form control group", actual.getName(), is("x"));
	}
	
	@Test
	public void getControlGroupWithUnknownNameThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(ControlNotFoundException.class);
		thrown().expectMessage("x");
		
		form.getControlGroup("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void submitWhenSubmitInputSubmitsRequest() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void submitWhenSubmitButtonSubmitsRequest() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void submitWhenDefaultButtonSubmitsRequest() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void submitWhenNoSubmitButtonThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(IllegalStateException.class);
		thrown().expectMessage("Missing form submit button");
		
		form.submit();
	}

	@Test
	public void submitWhenNoMethodSubmitsGetRequest() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/x")));
	}
	
	@Test
	public void submitWhenNoActionSubmitsRequest() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server(), "/x"))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}
	
	@Test
	public void submitWhenInvalidActionThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='x:/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid action: x:/a");
		
		form.submit();
	}

	@Test
	public void submitWhenGetSubmitsGetRequest() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/x")));
	}

	@Test
	public void submitWhenGetSubmitsHiddenControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}

	@Test
	public void submitWhenGetSubmitsTextControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}

	@Test
	public void submitWhenGetSubmitsTextControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='text' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}

	@Test
	public void submitWhenGetSubmitsPasswordControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}

	@Test
	public void submitWhenGetSubmitsPasswordControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='password' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}

	@Test
	public void submitWhenGetSubmitsCheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=on")));
	}
	
	@Test
	public void submitWhenGetSubmitsCheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=on")));
	}

	@Test
	public void submitWhenGetDoesNotSubmitUncheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a")));
	}
	
	@Test
	public void submitWhenGetDoesNotSubmitUncheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a")));
	}

	@Test
	public void submitWhenGetSubmitsValuedCheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}
	
	@Test
	public void submitWhenGetSubmitsValuedCheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}

	@Test
	public void submitWhenGetDoesNotSubmitValuedUncheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a")));
	}
	
	@Test
	public void submitWhenGetDoesNotSubmitValuedUncheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a")));
	}
	
	@Test
	public void submitWhenGetSubmitsCheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=on")));
	}
	
	@Test
	public void submitWhenGetSubmitsCheckedRadioControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=on")));
	}

	@Test
	public void submitWhenGetDoesNotSubmitUncheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a")));
	}
	
	@Test
	public void submitWhenGetSubmitsValuedCheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}
	
	@Test
	public void submitWhenGetSubmitsValuedCheckedRadioControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a?c=x")));
	}

	@Test
	public void submitWhenGetDoesNotSubmitValuedUncheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/a")));
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
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void submitWhenGetSendsCookie() throws Exception
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void submitWhenGetSendsPreviousCookie() throws Exception
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
		server().start();
		
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
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		server().start();
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}

	@Test
	public void submitWhenPostSubmitsPostRequest() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(post("/x")));
	}

	@Test
	public void submitWhenPostSubmitsHiddenControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsTextControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsTextControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsPasswordControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsPasswordControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='password' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostSubmitsCheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request body", body(takeRequest(server())), is("c=on"));
	}

	@Test
	public void submitWhenPostSubmitsCheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=on"));
	}

	@Test
	public void submitWhenPostDoesNotSubmitUncheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostDoesNotSubmitUncheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}

	@Test
	public void submitWhenPostSubmitsValuedCheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}
	
	@Test
	public void submitWhenPostSubmitsValuedCheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostDoesNotSubmitValuedUncheckedCheckboxControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostDoesNotSubmitValuedUncheckedCheckboxControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostSubmitsCheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=on"));
	}
	
	@Test
	public void submitWhenPostSubmitsCheckedRadioControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "on")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=on"));
	}

	@Test
	public void submitWhenPostDoesNotSubmitUncheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
	}
	
	@Test
	public void submitWhenPostSubmitsValuedCheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}
	
	@Test
	public void submitWhenPostSubmitsValuedCheckedRadioControlSetValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.setControlValue("c", "x")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), is("c=x"));
	}

	@Test
	public void submitWhenPostDoesNotSubmitValuedUncheckedRadioControlInitialValue() throws Exception
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", body(takeRequest(server())), isEmptyString());
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
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit()
			.getCookie("x");
		
		assertThat("cookie", actual, is("y"));
	}

	@Test
	public void submitWhenPostSendsCookie() throws Exception
	{
		server().enqueue(new MockResponse().addHeader("Set-Cookie", "x=y").setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		server().start();
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("cookie", takeRequest(server()).getHeader("Cookie"), is("x=y"));
	}

	@Test
	public void submitWhenPostSendsPreviousCookie() throws Exception
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
		server().start();
		
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
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		server().start();
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapReturnsProvider() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().start();
		
		T actual = newBrowser().get(url(server()))
			.getForm("x")
			.unwrap(getProviderType());
		
		assertThat("form provider", actual, is(instanceOf(getProviderType())));
	}

	@Test
	public void unwrapWithUnknownTypeThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server().start();
		
		Form form = newBrowser().get(url(server()))
			.getForm("x");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot unwrap to: class java.lang.Void");
		
		form.unwrap(Void.class);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Class<T> getProviderType();
}
