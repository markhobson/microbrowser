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

import java.util.Collections;

import org.hobsoft.microbrowser.support.FakeMicrodataDocument;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
	
	private ExpectedException thrown = ExpectedException.none();
	
	// ----------------------------------------------------------------------------------------------------------------
	// test methods
	// ----------------------------------------------------------------------------------------------------------------

	@Before
	public void setUp()
	{
		document = mock(FakeMicrodataDocument.class);
	}

	@Rule
	public ExpectedException getThrown()
	{
		return thrown;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getItemWhenItemReturnsItem()
	{
		final MicrodataItem item = mock(MicrodataItem.class);
		when(document.getItems("http://x")).thenReturn(asList(item));
		
		assertThat(document.getItem("http://x"), is(item));
	}
	
	@Test
	public void getItemWhenItemsReturnsFirstItem()
	{
		final MicrodataItem item1 = mock(MicrodataItem.class);
		final MicrodataItem item2 = mock(MicrodataItem.class);
		when(document.getItems("http://x")).thenReturn(asList(item1, item2));
		
		assertThat(document.getItem("http://x"), is(item1));
	}

	@Test
	public void getItemWhenNoItemsThrowsException()
	{
		when(document.getItems("http://x")).thenReturn(Collections.<MicrodataItem>emptyList());
		
		thrown.expect(MicrodataItemNotFoundException.class);
		thrown.expectMessage("http://x");
		
		document.getItem("http://x");
	}
	
	@Test
	public void getItemWhenInvalidUrlThrowsException()
	{
		when(document.getItems("x")).thenThrow(new IllegalArgumentException("y"));
		
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("y");
		
		document.getItem("x");
	}
}
