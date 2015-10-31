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

import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.tck.MicrodataDocumentTck;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * Integration test that executes the {@code MicrodataDocument} TCK against {@code JsoupMicrobrowser}.
 */
public class JsoupMicrodataDocumentIT extends MicrodataDocumentTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// AbstractMicrobrowserTest methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected Microbrowser newBrowser()
	{
		return new JsoupMicrobrowser();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// unwrap tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void unwrapWithDocumentTypeReturnsDocument()
	{
		server().enqueue(new MockResponse().setBody("<html><body/></html>"));
		
		Document actual = newBrowser().get(url(server()))
			.unwrap(Document.class);
		
		assertThat("document provider", actual, is(instanceOf(Document.class)));
	}
}
