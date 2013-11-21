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

import java.util.List;

import org.hobsoft.microbrowser.AbstractMicrodataDocument;
import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.MicrodataItem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code MicrodataDocument} adapter to a jsoup {@code Document}.
 */
class JsoupMicrodataDocument extends AbstractMicrodataDocument
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Document document;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public JsoupMicrodataDocument(Document document)
	{
		this.document = checkNotNull(document, "document");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public List<MicrodataItem> getItems(String itemType)
	{
		Elements elements = document.select(byItemType(itemType));
		
		return Lists.transform(elements, new Function<Element, MicrodataItem>()
		{
			public MicrodataItem apply(Element element)
			{
				return new JsoupMicrodataItem(element);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Form getForm(String name)
	{
		Element element = document.select(byForm(name)).first();
		
		return new JsoupForm(element);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static String byItemType(String itemType)
	{
		return String.format("[itemscope][itemtype=%s]", itemType);
	}

	private static String byForm(String name)
	{
		return String.format("form[name=%s]", name);
	}
}
