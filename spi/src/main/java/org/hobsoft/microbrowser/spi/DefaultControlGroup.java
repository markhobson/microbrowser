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
import org.hobsoft.microbrowser.ControlGroup;
import org.hobsoft.microbrowser.ControlNotFoundException;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * Default {@code ControlGroup} implementation.
 */
public class DefaultControlGroup implements ControlGroup
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final String UNCHECKED_VALUE = "";

	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final List<Control> controls;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public DefaultControlGroup(List<Control> controls)
	{
		this.controls = unmodifiableList(controls);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Controls methods
	// ----------------------------------------------------------------------------------------------------------------

	public String getName()
	{
		return controls.get(0).getName();
	}
	
	public List<Control> getControls()
	{
		return controls;
	}
	
	public Control getControl(String value)
	{
		for (Control control : controls)
		{
			if (value.equals(getCheckedValue(control)))
			{
				return control;
			}
		}
		
		throw new ControlNotFoundException(getName(), value);
	}
	
	public List<String> getValues()
	{
		List<String> values = new ArrayList<String>();
		
		for (Control control : controls)
		{
			String value = control.getValue();
			
			if (!value.isEmpty())
			{
				values.add(value);
			}
		}
		
		return values;
	}
	
	public void setValues(String... values)
	{
		List<String> valuesList = new ArrayList<String>(asList(values));
		
		for (Control control : controls)
		{
			if (control instanceof CheckableControl)
			{
				CheckableControl checkableControl = (CheckableControl) control;
				String checkedValue = checkableControl.getCheckedValue();
				boolean check = valuesList.remove(checkedValue);
				
				if (check)
				{
					control.setValue(checkedValue);
				}
				else if (checkableControl.isUncheckable())
				{
					control.setValue(UNCHECKED_VALUE);
				}
			}
		}
		
		if (!valuesList.isEmpty())
		{
			throw new IllegalArgumentException("Invalid control values: " + valuesList);
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static String getCheckedValue(Control control)
	{
		if (control instanceof CheckableControl)
		{
			return ((CheckableControl) control).getCheckedValue();
		}
		
		return control.getValue();
	}
}
