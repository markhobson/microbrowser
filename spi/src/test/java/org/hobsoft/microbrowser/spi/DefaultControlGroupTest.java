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
package org.hobsoft.microbrowser.spi;

import java.util.ArrayList;
import java.util.List;

import org.hobsoft.microbrowser.Control;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@code DefaultControlGroup}.
 */
public class DefaultControlGroupTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private ExpectedException thrown = ExpectedException.none();
	
	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public ExpectedException getThrown()
	{
		return thrown;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getNameReturnsFirstControlName()
	{
		DefaultControlGroup group = new DefaultControlGroup(asList(
			mockControl("x"),
			mockControl("y"),
			mockControl("z")
		));
		
		assertThat(group.getName(), is("x"));
	}
	
	@Test
	public void getControlsReturnsControls()
	{
		List<Control> controls = asList(mockControl("x"), mockControl("y"), mockControl("z"));
		DefaultControlGroup group = new DefaultControlGroup(controls);
		
		assertThat(group.getControls(), contains(controls.get(0), controls.get(1), controls.get(2)));
	}
	
	@Test
	public void getControlsReturnsImmutableList()
	{
		List<Control> controls = new ArrayList<Control>();
		controls.add(mockControl("x"));
		controls.add(mockControl("y"));
		controls.add(mockControl("z"));
		DefaultControlGroup group = new DefaultControlGroup(controls);
		
		thrown.expect(UnsupportedOperationException.class);
		
		group.getControls().clear();
	}
	
	@Test
	public void getValuesWhenValueReturnsValue()
	{
		DefaultControlGroup group = new DefaultControlGroup(singletonList(mockControl("c", "x")));
		
		assertThat(group.getValues(), contains("x"));
	}
	
	@Test
	public void getValuesWhenNoValueDoesNotReturnValue()
	{
		DefaultControlGroup group = new DefaultControlGroup(singletonList(mockControl("c", "")));
		
		assertThat(group.getValues(), is(empty()));
	}
	
	@Test
	public void getValuesWhenValuesReturnsValues()
	{
		DefaultControlGroup group = new DefaultControlGroup(asList(
			mockControl("c", "x"),
			mockControl("c", "y"),
			mockControl("c", "z")
		));
		
		assertThat(group.getValues(), contains("x", "y", "z"));
	}

	@Test
	public void setValuesWithCheckedValueSetsControlValue()
	{
		Control control = mockCheckableControl("c", "x");
		DefaultControlGroup group = new DefaultControlGroup(singletonList(control));
		
		group.setValues("x");
		
		verify(control).setValue("x");
	}
	
	@Test
	public void setValuesWithDifferentValueUnsetsControlValue()
	{
		Control control = mockCheckableControl("c", "x");
		DefaultControlGroup group = new DefaultControlGroup(asList(control, mockCheckableControl("c", "y")));
		
		group.setValues("y");
		
		verify(control).setValue("");
	}
	
	@Test
	public void setValuesWithInvalidValueThrowsException()
	{
		Control control = mockCheckableControl("c", "x");
		DefaultControlGroup group = new DefaultControlGroup(singletonList(control));
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid checkbox values: [y]");
		
		group.setValues("y");
	}
	
	@Test
	public void setValuesWhenUncheckableDoesNotSetControlValue()
	{
		Control control = mockControl("c");
		DefaultControlGroup group = new DefaultControlGroup(asList(control, mockCheckableControl("c", "x")));
		
		group.setValues("x");
		
		verify(control, never()).setValue(anyString());
	}
	
	@Test
	public void setValuesWithValuesSetsControlValues()
	{
		List<Control> controls = asList(
			mockCheckableControl("c", "x"),
			mockCheckableControl("c", "y"),
			mockCheckableControl("c", "z")
		);
		DefaultControlGroup group = new DefaultControlGroup(controls);
		
		group.setValues("x", "y", "z");
		
		verify(controls.get(0)).setValue("x");
		verify(controls.get(1)).setValue("y");
		verify(controls.get(2)).setValue("z");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Control mockControl(String name)
	{
		Control control = mock(Control.class);
		when(control.getName()).thenReturn(name);
		return control;
	}
	
	private static Control mockControl(String name, String value)
	{
		Control control = mockControl(name);
		when(control.getValue()).thenReturn(value);
		return control;
	}

	private static Control mockCheckableControl(String name, String checkedValue)
	{
		CheckableControl control = mock(CheckableControl.class);
		when(control.getName()).thenReturn(name);
		when(control.getCheckedValue()).thenReturn(checkedValue);
		return control;
	}
}
