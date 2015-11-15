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
package org.hobsoft.microbrowser.tck;

import java.net.MalformedURLException;

import org.hamcrest.Matcher;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.junit.Test;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.microbrowser.tck.support.MicrobrowserMatchers.item;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerMatchers.get;
import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerUtils.url;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Form.submit} using the GET method.
 */
public abstract class FormGetTck extends FormMethodTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void submitWhenGetReturnsResponse() throws MalformedURLException
	{
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'/>"
			+ "</form>"
			+ "</body></html>"));
		server().enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='http://i' itemid='http://x'/>"
			+ "</body></html>"));
		
		MicrodataDocument actual = newBrowser().get(url(server()))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("http://i"), is(item("http://x")));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// FormMethodTck methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected String getMethod()
	{
		return "get";
	}
	
	@Override
	protected Matcher<RecordedRequest> method(String expectedPath)
	{
		return get(expectedPath);
	}
	
	@Override
	protected Matcher<RecordedRequest> method(String expectedPath, String expectedData)
	{
		if (expectedData.isEmpty())
		{
			return get(expectedPath);
		}
		
		return get(expectedPath + "?" + expectedData);
	}
}
