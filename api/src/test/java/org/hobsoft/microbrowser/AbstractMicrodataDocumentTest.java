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
package org.hobsoft.microbrowser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import org.hobsoft.microbrowser.support.FakeMicrodataDocument;
import org.junit.Test;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@code AbstractMicrodataDocument}.
 */
public class AbstractMicrodataDocumentTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private AbstractMicrodataDocument document;
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getItemWhenItemReturnsItem()
	{
		final MicrodataItem item = mock(MicrodataItem.class);
		document = mock(FakeMicrodataDocument.class);
		when(document.getItems("http://x")).thenReturn(asList(item));
		
		assertThat(document.getItem("http://x"), is(item));
	}
	
	@Test
	public void getItemWhenItemsReturnsFirstItem()
	{
		final MicrodataItem item1 = mock(MicrodataItem.class);
		final MicrodataItem item2 = mock(MicrodataItem.class);
		document = mock(FakeMicrodataDocument.class);
		when(document.getItems("http://x")).thenReturn(asList(item1, item2));
		
		assertThat(document.getItem("http://x"), is(item1));
	}

	@Test(expected = MicrodataItemNotFoundException.class)
	public void getItemWhenNoItemsThrowsException()
	{
		document = mock(FakeMicrodataDocument.class);
		when(document.getItems("http://x")).thenReturn(Collections.<MicrodataItem>emptyList());
		
		document.getItem("http://x");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getItemWhenInvalidUrlThrowsException()
	{
		document = mock(FakeMicrodataDocument.class);
		when(document.getItems("x")).thenThrow(new IllegalArgumentException());
		
		document.getItem("x");
	}
	
	@Test
	public void urlWithUrlReturnsUrl() throws MalformedURLException
	{
		document = mock(FakeMicrodataDocument.class);
		
		assertThat(document.url("http://x"), is(new URL("http://x")));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void urlWithInvalidUrlThrowsException()
	{
		document = mock(FakeMicrodataDocument.class);
		
		document.url("x");
	}
}
