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

import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.tck.support.mockwebserver.MockWebServerRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.squareup.okhttp.mockwebserver.MockWebServer;

/**
 * Base test for Microbrowser TCKs.
 */
public abstract class AbstractMicrobrowserTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private MockWebServerRule serverRule = new MockWebServerRule();
	
	private ExpectedException thrown = ExpectedException.none();
	
	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public final MockWebServerRule getServerRule()
	{
		return serverRule;
	}
	
	@Rule
	public final ExpectedException thrown()
	{
		return thrown;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------
	
	protected final MockWebServer server()
	{
		return serverRule.getServer();
	}

	protected abstract Microbrowser newBrowser();
}
