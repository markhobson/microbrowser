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

import static org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerMatchers.post;

/**
 * TCK for {@code Form.submit} using the POST method.
 */
public abstract class FormPostTck extends FormMethodTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// FormMethodTck methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected String getMethod()
	{
		return "post";
	}
	
	@Override
	protected Matcher<RecordedRequest> method(String expectedPath)
	{
		return post(expectedPath);
	}
	
	@Override
	protected Matcher<RecordedRequest> method(String expectedPath, String expectedData)
	{
		return post(expectedPath, expectedData);
	}
}
