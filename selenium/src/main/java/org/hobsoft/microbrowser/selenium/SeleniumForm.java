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
package org.hobsoft.microbrowser.selenium;

import java.net.URL;
import java.util.List;

import org.hobsoft.microbrowser.Control;
import org.hobsoft.microbrowser.ControlGroup;
import org.hobsoft.microbrowser.ControlNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.spi.DefaultControlGroup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static org.hobsoft.microbrowser.spi.Urls.newUrlOrNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * {@code Form} adapter to a Selenium {@code WebElement}.
 */
class SeleniumForm implements Form
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final WebDriver driver;
	
	private final WebElement element;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public SeleniumForm(WebDriver driver, WebElement element)
	{
		this.driver = checkNotNull(driver, "driver");
		this.element = checkNotNull(element, "element");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form methods
	// ----------------------------------------------------------------------------------------------------------------
	
	public String getName()
	{
		return element.getAttribute("name");
	}
	
	public Control getControl(String name)
	{
		List<WebElement> elements = element.findElements(byControl(name));
		
		if (elements.isEmpty())
		{
			throw new ControlNotFoundException(name);
		}
		
		return newControl(elements.iterator().next());
	}
	
	public String getControlValue(String name)
	{
		return getControl(name).getValue();
	}

	public Form setControlValue(String name, String value)
	{
		getControl(name).setValue(value);
		
		return this;
	}
	
	public ControlGroup getControlGroup(String name)
	{
		List<WebElement> elements = element.findElements(byControl(name));
		
		if (elements.isEmpty())
		{
			throw new ControlNotFoundException(name);
		}
		
		return newControlGroup(elements);
	}

	public MicrodataDocument submit()
	{
		WebElement submit = getSubmit();
		checkArgument(getAction() != null, "Invalid action: " + element.getAttribute("action"));
		
		submit.click();
		
		return new SeleniumMicrodataDocument(driver);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Unwrappable methods
	// ----------------------------------------------------------------------------------------------------------------

	public <T> T unwrap(Class<T> type)
	{
		checkArgument(WebElement.class.equals(type), "Cannot unwrap to: %s", type);
		
		return type.cast(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private URL getAction()
	{
		String action = element.getAttribute("action");
		
		if (action.isEmpty())
		{
			action = driver.getCurrentUrl();
		}
		
		return newUrlOrNull(action);
	}
	
	private static By byControl(String name)
	{
		return By.cssSelector(String.format("input[name='%s']", name));
	}

	private static ControlGroup newControlGroup(List<WebElement> elements)
	{
		List<Control> controls = Lists.transform(elements, new Function<WebElement, Control>()
		{
			public Control apply(WebElement element)
			{
				return newControl(element);
			}
		});
		
		return new DefaultControlGroup(controls);
	}
	
	private static Control newControl(WebElement element)
	{
		Control control;
		String type = element.getAttribute("type");
		
		if ("hidden".equals(type))
		{
			control = new SeleniumHiddenControl(element);
		}
		else if ("checkbox".equals(type))
		{
			control = new SeleniumCheckboxControl(element);
		}
		else if ("radio".equals(type))
		{
			control = new SeleniumRadioControl(element);
		}
		else
		{
			control = new SeleniumTextControl(element);
		}
		
		return control;
	}

	private WebElement getSubmit()
	{
		List<WebElement> elements = element.findElements(bySubmit());
		checkState(!elements.isEmpty(), "Missing form submit button");
		
		return elements.iterator().next();
	}

	private static By bySubmit()
	{
		return By.cssSelector("input[type='submit'], button[type='submit'], button:not([type])");
	}
}
