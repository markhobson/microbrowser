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
import java.util.HashMap;
import java.util.Map;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
		
		parameterValuesByName = new HashMap<String, String>();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form methods
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return element.attr("name");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getParameter(String name)
	{
		checkNotNull(name, "name");
		Element control = getControl(name);
		
		String value;
		
		if (parameterValuesByName.containsKey(name))
		{
			value = parameterValuesByName.get(name);
		}
		else
		{
			value = control.val();
		}
		
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	public Form setParameter(String name, String value)
	{
		checkNotNull(name, "name");
		checkNotNull(value, "value");
		getControl(name);
		
		parameterValuesByName.put(name, value);
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument submit()
	{
		getSubmit();
		
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
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private Connection getConnection()
	{
		return Jsoup.connect(getAction())
			.method(getMethod())
			.data(parameterValuesByName)
			.cookies(document.getCookies());
	}
	
	private String getAction()
	{
		return element.absUrl("action");
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
	
	private Element getControl(String name)
	{
		Elements elements = element.select(byControl(name));
		checkArgument(!elements.isEmpty(), "Cannot find form control: %s", name);
		
		return elements.first();
	}
	
	private static String byControl(String name)
	{
		return String.format("input[name=%s]", name);
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
