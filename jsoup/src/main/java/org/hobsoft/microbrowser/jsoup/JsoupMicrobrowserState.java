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

import java.util.Collections;
import java.util.Map;

import org.jsoup.nodes.Document;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Holds the state of a {@code JsoupMicrobrowser} which consists of cookies and a document. 
 */
final class JsoupMicrobrowserState
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	private static final Map<String, String> NO_COOKIES = Collections.<String, String>emptyMap();
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Map<String, String> cookies;
	
	private final Document document;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------
	
	public JsoupMicrobrowserState()
	{
		this(new Document("microbrowser://"));
	}

	public JsoupMicrobrowserState(Document document)
	{
		this(NO_COOKIES, document);
	}

	public JsoupMicrobrowserState(Map<String, String> cookies, Document document)
	{
		this.cookies = checkNotNull(cookies, "cookies");
		this.document = checkNotNull(document, "document");
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public Map<String, String> getCookies()
	{
		return cookies;
	}
	
	public Document getDocument()
	{
		return document;
	}
}
