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
import java.util.Map.Entry;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code Form} adapter to a jsoup {@code Element}.
 */
public class JsoupForm implements Form
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Element element;
	
	private final Map<String, String> parameterValuesByName;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupForm(Element element)
	{
		this.element = checkNotNull(element, "element");
		
		parameterValuesByName = new HashMap<String, String>();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public Form setParameter(String name, String value)
	{
		checkNotNull(name, "name");
		checkNotNull(value, "value");
		checkArgument(!element.select(byControl(name)).isEmpty(), "Cannot find form control: %s", name);
		
		parameterValuesByName.put(name, value);
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument submit()
	{
		Document document;
		
		try
		{
			document = getConnection().execute().parse();
		}
		catch (IOException exception)
		{
			// TODO: decide on exception policy
			throw new RuntimeException(exception);
		}
		
		return new JsoupMicrodataDocument(document);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------
	
	private Connection getConnection()
	{
		Connection connection = Jsoup.connect(getAction());
		
		connection.method(getMethod());
		
		for (Entry<String, String> entry : parameterValuesByName.entrySet())
		{
			connection.data(entry.getKey(), entry.getValue());
		}

		return connection;
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
	
	private static String byControl(String name)
	{
		return String.format("input[name=%s]", name);
	}
}
