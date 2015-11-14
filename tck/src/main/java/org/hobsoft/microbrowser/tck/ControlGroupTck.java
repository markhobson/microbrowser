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

import java.util.List;

import org.hobsoft.microbrowser.Control;
import org.hobsoft.microbrowser.ControlGroup;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.control;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code ControlGroup}.
 */
public abstract class ControlGroupTck extends AbstractMicrobrowserTest
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
			.getControlGroup("x")
			.getName();
		
		assertThat("form control group name", actual, is("x"));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getControls tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void getControlsReturnsControl()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x'/>"
			+ "</form>"
			+ "</body></html>"));
		
		List<Control> actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("x")
			.getControls();
		
		assertThat("form control group control", actual, contains(control("x")));
	}
	
	@Test
	public void getControlsReturnsControls()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='x' value='y'/>"
			+ "<input type='text' name='x' value='z'/>"
			+ "</form>"
			+ "</body></html>"));
		
		List<Control> actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("x")
			.getControls();
		
		assertThat("form control group controls", actual, contains(
			control("x", "y"),
			control("x", "z")
		));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// getValues tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getValuesWhenValuedCheckedCheckboxControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='checkbox' name='c' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		List<String> actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c")
			.getValues();
		
		assertThat("form control group values", actual, contains("x"));
	}
	
	@Test
	public void getValuesWhenValuedUncheckedCheckboxControlReturnsInitialValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='checkbox' name='c' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		List<String> actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c")
			.getValues();
		
		assertThat("form control group values", actual, is(empty()));
	}
	
	@Test
	public void getValuesWhenValuedCheckedCheckboxControlsReturnsInitialValues()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='checkbox' name='c' value='y' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		List<String> actual = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c")
			.getValues();
		
		assertThat("form control group values", actual, contains("x", "y"));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// setValues tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void setValuesWhenValuedCheckboxControlsWithCheckedValueSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='checkbox' name='c' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		ControlGroup group = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c");
		group.setValues("x");
		
		assertThat("form control group values", group.getValues(), contains("x"));
	}
	
	@Test
	public void setValuesWhenValuedCheckboxControlsWithCheckedValuesSetsValues()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='checkbox' name='c' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		ControlGroup group = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c");
		group.setValues("x", "y");
		
		assertThat("form control group values", group.getValues(), contains("x", "y"));
	}

	@Test
	public void setValuesWhenValuedCheckboxControlsWithUncheckedValueSetsValue()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x' checked/>"
			+ "<input type='checkbox' name='c' value='y' checked/>"
			+ "</form>"
			+ "</body></html>"));
		
		ControlGroup group = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c");
		group.setValues();
		
		assertThat("form control group values", group.getValues(), is(empty()));
	}
	
	@Test
	public void setValuesWhenValuedCheckboxControlsWithInvalidValueThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='checkbox' name='c' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		ControlGroup group = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c");

		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox values: [z]");
		
		group.setValues("z");
	}
	
	@Test
	public void setValuesWhenValuedCheckboxControlsWithInvalidValuesThrowsException()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='checkbox' name='c' value='x'/>"
			+ "<input type='checkbox' name='c' value='y'/>"
			+ "</form>"
			+ "</body></html>"));
		
		ControlGroup group = newBrowser().get(url(server()))
			.getForm("f")
			.getControlGroup("c");
		
		thrown().expect(IllegalArgumentException.class);
		thrown().expectMessage("Invalid checkbox values: [p, q]");
		
		group.setValues("p", "q");
	}
}
