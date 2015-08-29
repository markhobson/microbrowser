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

import java.util.Collections;

import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.LinkNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@code AbstractHypermedia}.
 */
public class AbstractHypermediaTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private AbstractHypermedia hypermedia;
	
	private ExpectedException thrown = ExpectedException.none();
	
	// ----------------------------------------------------------------------------------------------------------------
	// test methods
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
	public void getLinkReturnsFirstLink()
	{
		Link link = mock(Link.class);
		hypermedia = mock(AbstractHypermedia.class);
		when(hypermedia.getLinks("x")).thenReturn(asList(link, mock(Link.class)));
		
		assertThat(hypermedia.getLink("x"), is(link));
	}
	
	@Test
	public void getLinkWhenNotFoundThrowsException()
	{
		hypermedia = mock(AbstractHypermedia.class);
		when(hypermedia.getLinks("x")).thenReturn(Collections.<Link>emptyList());
		
		thrown.expect(LinkNotFoundException.class);
		thrown.expectMessage("x");
		
		hypermedia.getLink("x");
	}
}
