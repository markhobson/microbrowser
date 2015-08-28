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
import java.util.HashMap;
import java.util.Map;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.ParameterNotFoundException;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

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
	
	private final Map<String, JsoupControl> controlsByName;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupForm(JsoupMicrodataDocument document, FormElement element)
	{
		this.document = checkNotNull(document, "document");
		this.element = checkNotNull(element, "element");
		
		controlsByName = getControlsByName(element);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form methods
	// ----------------------------------------------------------------------------------------------------------------
	
	public String getName()
	{
		return element.attr("name");
	}
	
	public String getParameter(String name)
	{
		checkNotNull(name, "name");
		
		return getControl(name).getValue();
	}

	public Form setParameter(String name, String value)
	{
		checkNotNull(name, "name");
		checkNotNull(value, "value");
		
		getControl(name).setValue(value);
		
		return this;
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
	
	private static Map<String, JsoupControl> getControlsByName(Element element)
	{
		Map<String, JsoupControl> controlsByName = new HashMap<String, JsoupControl>();
		
		for (Element controlElement : element.select(byControls()))
		{
			JsoupControl control = new JsoupControl(controlElement);
			
			controlsByName.put(control.getName(), control);
		}
		
		return controlsByName;
	}
	
	private JsoupControl getControl(String name)
	{
		if (!controlsByName.containsKey(name))
		{
			throw new ParameterNotFoundException(name);
		}
		
		return controlsByName.get(name);
	}

	private static String byControls()
	{
		return "input[type=hidden], input[type=text], input[type=password]";
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
			action = element.ownerDocument().baseUri();
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
