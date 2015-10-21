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
package org.hobsoft.microbrowser.jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.hobsoft.microbrowser.Control;
import org.hobsoft.microbrowser.ControlGroup;
import org.hobsoft.microbrowser.ControlNotFoundException;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.spi.DefaultControlGroup;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static org.hobsoft.microbrowser.spi.Urls.newUrlOrNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * {@code Form} adapter to a jsoup {@code Element}.
 */
class JsoupForm implements Form
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final JsoupMicrodataDocument document;
	
	private final FormElement element;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupForm(JsoupMicrodataDocument document, FormElement element)
	{
		this.document = checkNotNull(document, "document");
		this.element = checkNotNull(element, "element");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form methods
	// ----------------------------------------------------------------------------------------------------------------
	
	public String getName()
	{
		return element.attr("name");
	}
	
	public Control getControl(String name)
	{
		Elements elements = element.select(byControl(name));
		
		if (elements.isEmpty())
		{
			throw new ControlNotFoundException(name);
		}
		
		return newControl(elements.first());
	}

	public String getControlValue(String name)
	{
		checkNotNull(name, "name");
		
		return getControl(name).getValue();
	}

	public Form setControlValue(String name, String value)
	{
		checkNotNull(name, "name");
		checkNotNull(value, "value");
		
		getControl(name).setValue(value);
		
		return this;
	}
	
	public ControlGroup getControlGroup(String name)
	{
		Elements elements = element.select(byControl(name));
		
		if (elements.isEmpty())
		{
			throw new ControlNotFoundException(name);
		}
		
		return newControlGroup(elements);
	}

	public MicrodataDocument submit()
	{
		getSubmit();
		checkArgument(getAction() != null, "Invalid action: " + element.attr("action"));
		
		JsoupMicrodataDocument nextDocument;
		
		try
		{
			nextDocument = new JsoupMicrodataDocument(document.getCookies(), getConnection().execute());
		}
		catch (IOException exception)
		{
			throw new MicrobrowserException("Error submitting form", exception);
		}
		
		return nextDocument;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// Unwrappable methods
	// ----------------------------------------------------------------------------------------------------------------

	public <T> T unwrap(Class<T> type)
	{
		checkArgument(Element.class.equals(type), "Cannot unwrap to: %s", type);
		
		return type.cast(element);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private static String byControl(String name)
	{
		return String.format("input[name=%s]", name);
	}
	
	private static ControlGroup newControlGroup(Elements elements)
	{
		List<Control> controls = Lists.transform(elements, new Function<Element, Control>()
		{
			public Control apply(Element element)
			{
				return newControl(element);
			}
		});
		
		return new DefaultControlGroup(controls);
	}
	
	private static Control newControl(Element element)
	{
		Control control;
		String type = element.attr("type");
		
		if ("hidden".equals(type))
		{
			control = new JsoupHiddenControl(element);
		}
		else if ("checkbox".equals(type))
		{
			control = new JsoupCheckboxControl(element);
		}
		else if ("radio".equals(type))
		{
			control = new JsoupRadioControl(element);
		}
		else
		{
			control = new JsoupTextControl(element);
		}
		
		return control;
	}

	private Connection getConnection()
	{
		return element.submit()
			.cookies(document.getCookies());
	}
	
	private URL getAction()
	{
		String action;
		
		if (element.hasAttr("action"))
		{
			action = element.absUrl("action");
		}
		else
		{
			action = element.baseUri();
		}
		
		return newUrlOrNull(action);
	}

	private Element getSubmit()
	{
		Elements elements = element.select(bySubmit());
		checkState(!elements.isEmpty(), "Missing form submit button");
		
		return elements.first();
	}
	
	private static String bySubmit()
	{
		return "input[type=submit], button[type=submit], button:not([type])";
	}
}
