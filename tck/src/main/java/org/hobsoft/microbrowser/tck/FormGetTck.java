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

import org.hamcrest.Matcher;

import com.squareup.okhttp.mockwebserver.RecordedRequest;

import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerMatchers.get;

/**
 * TCK for {@code Form.submit} using the GET method.
 */
public abstract class FormGetTck extends FormMethodTck
{
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
