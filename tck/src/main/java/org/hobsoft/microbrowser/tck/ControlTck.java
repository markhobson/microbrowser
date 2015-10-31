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
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Control}.
 * 
 * @param <T>
 *            the provider-specific control type
 */
public abstract class ControlTck<T> extends AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// getName tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getNameReturnsName()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("x")
			.getName();
		
		assertThat("form control name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// getValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getValueWhenHiddenControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}

	@Test
	public void getValueWhenTextControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}

	@Test
	public void getValueWhenPasswordControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}

	@Test
	public void getValueWhenCheckedCheckboxControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("on"));
	}

	@Test
	public void getValueWhenUncheckedCheckboxControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenValuedCheckedCheckboxControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenValuedUncheckedCheckboxControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenCheckedRadioControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("on"));
	}

	@Test
	public void getValueWhenUncheckedRadioControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, isEmptyString());
	}
	
	@Test
	public void getValueWhenValuedCheckedRadioControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		String actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c")
			.getValue();
		
		assertThat("form control value", actual, is("x"));
	}
	
	@Test
	public void getValueWhenValuedUncheckedRadioControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
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
	public void setValueWhenHiddenControlThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='hidden' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot set hidden control value: c");
		
		control.setValue("y");
	}
	
	@Test
	public void setValueWhenTextControlSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("y");
		
		assertThat("form control value", control.getValue(), is("y"));
	}

	@Test
	public void setValueWhenPasswordControlSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='password' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("y");
		
		assertThat("form control value", control.getValue(), is("y"));
	}

	@Test
	public void setValueWhenCheckboxControlWithCheckedSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("on");
		
		assertThat("form control value", control.getValue(), is("on"));
	}

	@Test
	public void setValueWhenCheckboxControlWithUncheckedSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("");
		
		assertThat("form control value", control.getValue(), isEmptyString());
	}
	
	@Test
	public void setValueWhenCheckboxControlWithInvalidValueThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: x");
		
		control.setValue("x");
	}
	
	@Test
	public void setValueWhenValuedCheckboxControlWithCheckedSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("x");
		
		assertThat("form control value", control.getValue(), is("x"));
	}
	
	@Test
	public void setValueWhenValuedCheckboxControlWithUncheckedSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("");
		
		assertThat("form control value", control.getValue(), isEmptyString());
	}
	
	@Test
	public void setValueWhenValuedCheckboxControlWithInvalidValueThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: y");
		
		control.setValue("y");
	}
	
	@Test
	public void setValueWhenRadioControlWithCheckedSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("on");
		
		assertThat("form control value", control.getValue(), is("on"));
	}

	@Test
	public void setValueWhenRadioControlWithUncheckedThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot uncheck radio control");
		
		control.setValue("");
	}
	
	@Test
	public void setValueWhenRadioControlWithInvalidValueThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: x");
		
		control.setValue("x");
	}
	
	@Test
	public void setValueWhenValuedRadioControlWithCheckedSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		control.setValue("x");
		
		assertThat("form control value", control.getValue(), is("x"));
	}
	
	@Test
	public void setValueWhenValuedRadioControlWithUncheckedThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot uncheck radio control");
		
		control.setValue("");
	}
	
	@Test
	public void setValueWhenValuedRadioControlWithInvalidValueThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='radio' name='c' value='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("c");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox value: y");
		
		control.setValue("y");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapReturnsProvider()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		T actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("x")
			.unwrap(getProviderType());
		
		assertThat("form control provider", actual, is(instanceOf(getProviderType())));
	}

	@Test
	public void unwrapWithUnknownTypeThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		Control control = newBrowser().get(url(server()))
			.getForm("f")
			.getControl("x");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Cannot unwrap to: class java.lang.Void");
		
		control.unwrap(Void.class);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Class<T> getProviderType();
}
