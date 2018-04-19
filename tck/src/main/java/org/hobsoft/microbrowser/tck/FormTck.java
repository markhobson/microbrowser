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

import org.hobsoft.microbrowser.Control;
import org.hobsoft.microbrowser.ControlGroup;
import org.hobsoft.microbrowser.ControlNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerMatchers.get;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.takeRequest;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Form}.
 */
public abstract class FormTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getName tests
	// ----------------------------------------------------------------------------------------------------------------

	public void getNameReturnsName()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("x")
			.getName();
		
		assertThat("form name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getControl tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getControlReturnsControl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("x");
		
		assertThat("form control", actual.getName(), is("x"));
	}
	
	@Test
	public void getControlWithUnknownNameThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		
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
	public void getControlValueReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlValue("x");
		
		assertThat("form control value", actual, is("y"));
	}
	
	@Test
	public void getControlValueWithUnknownNameThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		
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
	public void setControlValueSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		form.setControlValue("x", "y");
		
		assertThat("form control value", form.getControlValue("x"), is("y"));
	}

	@Test
	public void setControlValueWithUnknownNameThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		
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
	public void getControlGroupReturnsControlGroup()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		ControlGroup actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("x");
		
		assertThat("form control group", actual.getName(), is("x"));
	}
	
	@Test
	public void getControlGroupWithUnknownNameThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		
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
	public void submitWhenSubmitInputSubmitsRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void submitWhenSubmitButtonSubmitsRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void submitWhenDefaultButtonSubmitsRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}

	@Test
	public void submitWhenNoSubmitButtonThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(IllegalStateException.class);
		thrown().expectMessage("Missing form submit button");
		
		form.submit();
	}

	@Test
	public void submitWhenNoMethodSubmitsGetRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()), is(get("/x")));
	}
	
	@Test
	public void submitWhenNoActionSubmitsRequest() throws InterruptedException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse());
		
		newBrowser().get(url(server(), "/x"))
			.getForm("f")
			.submit();
		
		server().takeRequest();
		assertThat("request", takeRequest(server()).getPath(), is("/x"));
	}
	
	@Test
	public void submitWhenInvalidActionThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='x:/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Form form = newBrowser().get(url(server()))
			.getForm("f");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid action: x:/a");
		
		form.submit();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapWithUnknownTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		
		Form form = newBrowser().get(url(server()))
			.getForm("x");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot unwrap to: class java.lang.Void");
		
		form.unwrap(Void.class);
	}
}
