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
import org.hobsoft.microbrowser.tck.MicrodataItemTck;
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * Integration test that executes the {@code MicrodataItem} TCK against {@code JsoupMicrobrowser}.
 */
public class JsoupMicrodataItemIT extends MicrodataItemTck
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
	public void unwrapWithElementTypeReturnsElement()
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i'/>"
			+ "</body></html>"));
		
		Element actual = newBrowser().get(url(server()))
			.getItem("http://i")
			.unwrap(Element.class);
		
		assertThat("item provider", actual, is(instanceOf(Element.class)));
	}
}
