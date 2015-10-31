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

import org.hobsoft.microbrowser.Control;
import org.hobsoft.microbrowser.ControlGroup;
import org.hobsoft.microbrowser.Form;
import org.junit.Test;

import static java.util.Arrays.asList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@code AbstractForm}.
 */
public class AbstractFormTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getControlReturnsFirstControl()
	{
		AbstractForm form = mock(AbstractForm.class);
		Control control = newControl("y");
		ControlGroup group = newControlGroup(control, newControl("z"));
		when(form.getControlGroup("x")).thenReturn(group);
		
		assertThat(form.getControl("x"), is(control));
	}

	@Test
	public void getControlValueReturnsControlValue()
	{
		AbstractForm form = mock(AbstractForm.class);
		Control control = newControl("y");
		ControlGroup group = newControlGroup(control);
		when(form.getControlGroup("x")).thenReturn(group);
		
		assertThat(form.getControlValue("x"), is("y"));
	}
	
	@Test
	public void setControlValueSetsControlValue()
	{
		AbstractForm form = mock(AbstractForm.class);
		Control control = mock(Control.class);
		ControlGroup group = newControlGroup(control);
		when(form.getControlGroup("x")).thenReturn(group);
		
		form.setControlValue("x", "y");
		
		verify(control).setValue("y");
	}
	
	@Test
	public void setControlValueReturnsForm()
	{
		AbstractForm form = mock(AbstractForm.class);
		ControlGroup group = newControlGroup(mock(Control.class));
		when(form.getControlGroup(anyString())).thenReturn(group);
		
		Form actual = form.setControlValue("x", "y");
		
		assertThat(actual, is((Form) form));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Control newControl(String value)
	{
		Control control = mock(Control.class);
		when(control.getValue()).thenReturn(value);
		return control;
	}

	private static ControlGroup newControlGroup(Control... controls)
	{
		ControlGroup group = mock(ControlGroup.class);
		when(group.getControls()).thenReturn(asList(controls));
		return group;
	}
}
