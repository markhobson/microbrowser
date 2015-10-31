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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
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
		List<Control> controls = asList(newControl("x"), newControl("y"), newControl("z"));
		DefaultControlGroup group = new DefaultControlGroup(controls);
		
		assertThat(group.getName(), is("x"));
	}
	
	@Test
	public void getControlsReturnsControls()
	{
		Control[] controls = new Control[] {newControl("x"), newControl("y"), newControl("z")};
		DefaultControlGroup group = new DefaultControlGroup(asList(controls));
		
		assertThat(group.getControls(), contains(controls[0], controls[1], controls[2]));
	}
	
	@Test
	public void getControlsReturnsImmutableList()
	{
		List<Control> controls = new ArrayList<Control>();
		controls.add(newControl("x"));
		controls.add(newControl("y"));
		controls.add(newControl("z"));
		DefaultControlGroup group = new DefaultControlGroup(controls);
		
		thrown.expect(UnsupportedOperationException.class);
		
		group.getControls().clear();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Control newControl(String name)
	{
		Control control = mock(Control.class);
		when(control.getName()).thenReturn(name);
		return control;
	}
}
