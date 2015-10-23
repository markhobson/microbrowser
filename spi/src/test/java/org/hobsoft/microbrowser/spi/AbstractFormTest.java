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
import org.hobsoft.microbrowser.Form;
import org.junit.Test;

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
	public void getControlValueReturnsControlValue()
	{
		AbstractForm form = mock(AbstractForm.class);
		Control control = newControl("y");
		when(form.getControl("x")).thenReturn(control);
		
		assertThat(form.getControlValue("x"), is("y"));
	}
	
	@Test
	public void setControlValueSetsControlValue()
	{
		AbstractForm form = mock(AbstractForm.class);
		Control control = mock(Control.class);
		when(form.getControl("x")).thenReturn(control);
		
		form.setControlValue("x", "y");
		
		verify(control).setValue("y");
	}
	
	@Test
	public void setControlValueReturnsForm()
	{
		AbstractForm form = mock(AbstractForm.class);
		when(form.getControl(anyString())).thenReturn(mock(Control.class));
		
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
}
