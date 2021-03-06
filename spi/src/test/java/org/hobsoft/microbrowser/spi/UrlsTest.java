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
package org.hobsoft.microbrowser.spi;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code Urls}.
 */
public class UrlsTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// field
	// ----------------------------------------------------------------------------------------------------------------

	private ExpectedException thrown = ExpectedException.none();
	
	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public ExpectedException getThrown()
	{
		return thrown;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void newUrlReturnsUrl() throws MalformedURLException
	{
		assertThat(Urls.newUrl("http://x"), is(new URL("http://x")));
	}
	
	@Test
	public void newUrlWithInvalidUrlThrowsException()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Invalid URL: x");
		
		Urls.newUrl("x");
	}
	
	@Test
	public void newUrlOrNullReturnsUrl() throws MalformedURLException
	{
		assertThat(Urls.newUrlOrNull("http://x"), is(new URL("http://x")));
	}
	
	@Test
	public void newUrlOrNullWithInvalidUrlReturnsNull()
	{
		assertThat(Urls.newUrlOrNull("x"), is(nullValue()));
	}
}
