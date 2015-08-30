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
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Control}.
 */
public abstract class ControlTck extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getValueWhenHiddenControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}

	@Test
	public void getValueWhenTextControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}

	@Test
	public void getValueWhenPasswordControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}

	@Test
	public void getValueWhenCheckedCheckboxControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("on"));
	}

	@Test
	public void getValueWhenUncheckedCheckboxControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenValuedCheckedCheckboxControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenValuedUncheckedCheckboxControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenCheckedRadioControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("on"));
	}

	@Test
	public void getValueWhenUncheckedRadioControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenValuedCheckedRadioControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenValuedUncheckedRadioControlReturnsInitialValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, isEmptyString());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// setValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void setValueWhenHiddenControlThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot set hidden control value: c");
		
		control.setValue("y");
	}
	
	@Test
	public void setValueWhenTextControlSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("y");
		
		assertThat("form control value", control.getValue(), is("y"));
	}

	@Test
	public void setValueWhenPasswordControlSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("y");
		
		assertThat("form control value", control.getValue(), is("y"));
	}

	@Test
	public void setValueWhenCheckboxControlWithCheckedSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("on");
		
		assertThat("form control value", control.getValue(), is("on"));
	}

	@Test
	public void setValueWhenCheckboxControlWithUncheckedSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("");
		
		assertThat("form control value", control.getValue(), isEmptyString());
	}
	
	@Test
	public void setValueWhenCheckboxControlWithInvalidValueThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: x");
		
		control.setValue("x");
	}
	
	@Test
	public void setValueWhenValuedCheckboxControlWithCheckedSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("x");
		
		assertThat("form control value", control.getValue(), is("x"));
	}
	
	@Test
	public void setValueWhenValuedCheckboxControlWithUncheckedSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("");
		
		assertThat("form control value", control.getValue(), isEmptyString());
	}
	
	@Test
	public void setValueWhenValuedCheckboxControlWithInvalidValueThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: y");
		
		control.setValue("y");
	}
	
	@Test
	public void setValueWhenRadioControlWithCheckedSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("on");
		
		assertThat("form control value", control.getValue(), is("on"));
	}

	@Test
	public void setValueWhenRadioControlWithUncheckedThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot uncheck radio control");
		
		control.setValue("");
	}
	
	@Test
	public void setValueWhenRadioControlWithInvalidValueThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: x");
		
		control.setValue("x");
	}
	
	@Test
	public void setValueWhenValuedRadioControlWithCheckedSetsValue() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("x");
		
		assertThat("form control value", control.getValue(), is("x"));
	}
	
	@Test
	public void setValueWhenValuedRadioControlWithUncheckedThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot uncheck radio control");
		
		control.setValue("");
	}
	
	@Test
	public void setValueWhenValuedRadioControlWithInvalidValueThrowsException() throws IOException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		server().start();
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: y");
		
		control.setValue("y");
	}
}
