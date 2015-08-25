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
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
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
	
	private final Element element;
	
	private final Map<String, String> parameterValuesByName;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupForm(JsoupMicrodataDocument document, Element element)
	{
		this.document = checkNotNull(document, "document");
		this.element = checkNotNull(element, "element");
		
		parameterValuesByName = getInitialParameters(element);
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
		checkControl(name);
		
		return parameterValuesByName.get(name);
	}

	public Form setParameter(String name, String value)
	{
		checkNotNull(name, "name");
		checkNotNull(value, "value");
		checkControl(name);
		
		parameterValuesByName.put(name, value);
		
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
	
	private static Map<String, String> getInitialParameters(Element element)
	{
		Map<String, String> parameterValuesByName = new HashMap<String, String>();
		
		for (Element control : element.select(byControls()))
		{
			String name = control.attr("name");
			String value = control.val();
			
			parameterValuesByName.put(name, value);
		}
		
		return parameterValuesByName;
	}
	
	private static String byControls()
	{
		return "input[type=text], input[type=password], input[type=hidden]";
	}
	
	private void checkControl(String name)
	{
		if (!parameterValuesByName.containsKey(name))
		{
			throw new ParameterNotFoundException(name);
		}
	}
	
	private Connection getConnection()
	{
		return Jsoup.connect(getAction().toString())
			.method(getMethod())
			.data(parameterValuesByName)
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

	private Method getMethod()
	{
		String value = element.attr("method").toUpperCase();
		
		if (value.isEmpty())
		{
			return Method.GET;
		}
		
		return Method.valueOf(value);
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
