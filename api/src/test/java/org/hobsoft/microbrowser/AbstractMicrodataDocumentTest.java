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
import java.util.List;

import org.hobsoft.microbrowser.support.FakeMicrodataDocument;
import org.junit.Test;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

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
		document = new FakeMicrodataDocument()
		{
			@Override
			public List<MicrodataItem> getItems(String type)
			{
				return "x".equals(type) ? asList(item) : null;
			}
		};
		
		assertThat(document.getItem("x"), is(item));
	}
	
	@Test
	public void getItemWhenItemsReturnsFirstItem()
	{
		final MicrodataItem item1 = mock(MicrodataItem.class);
		final MicrodataItem item2 = mock(MicrodataItem.class);
		document = new FakeMicrodataDocument()
		{
			@Override
			public List<MicrodataItem> getItems(String type)
			{
				return "x".equals(type) ? asList(item1, item2) : null;
			}
		};
		
		assertThat(document.getItem("x"), is(item1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getItemWhenNoItemsThrowsException()
	{
		document = new FakeMicrodataDocument()
		{
			@Override
			public List<MicrodataItem> getItems(String type)
			{
				return "x".equals(type) ? Collections.<MicrodataItem>emptyList() : null;
			}
		};
		
		document.getItem("x");
	}
}
