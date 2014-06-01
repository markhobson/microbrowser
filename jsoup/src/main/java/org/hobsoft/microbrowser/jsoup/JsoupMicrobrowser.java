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
import java.util.Collections;

import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.MicrobrowserException;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import static org.hobsoft.microbrowser.Urls.newUrl;

/**
 * {@code Microbrowser} implementation that uses jsoup.
 */
public class JsoupMicrobrowser implements Microbrowser
{
	// ----------------------------------------------------------------------------------------------------------------
	// Microbrowser methods
	// ----------------------------------------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	public MicrodataDocument get(String url)
	{
		newUrl(url);
		
		try
		{
			Response response = Jsoup.connect(url)
				.method(Method.GET)
				.ignoreHttpErrors(true)
				.execute();
			
			return new JsoupMicrodataDocument(Collections.<String, String>emptyMap(), response);
		}
		catch (IOException exception)
		{
			throw new MicrobrowserException("Error fetching page: " + url, exception);
		}
	}
}
