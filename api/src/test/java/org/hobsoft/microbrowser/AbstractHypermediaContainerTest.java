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

import java.util.Iterator;

import org.hobsoft.microbrowser.support.FakeHypermediaContainer;
import org.junit.Test;

import static java.util.Arrays.asList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@code AbstractHypermediaContainer}.
 */
public class AbstractHypermediaContainerTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private AbstractHypermediaContainer container;
	
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getFormReturnsForm()
	{
		final Form form = mock(Form.class);
		container = new FakeHypermediaContainer()
		{
			@Override
			protected Form newForm(String name)
			{
				return "x".equals(name) ? form : null;
			}
		};
		
		assertThat(container.getForm("x"), is(form));
	}
	
	@Test
	public void getFormCachesForm()
	{
		Form form = mock(Form.class);
		final Iterator<Form> forms = asList(form, mock(Form.class)).iterator();
		container = new FakeHypermediaContainer()
		{
			@Override
			protected Form newForm(String name)
			{
				return "x".equals(name) ? forms.next() : null;
			}
		};
		
		container.getForm("x");
		assertThat(container.getForm("x"), is(form));
	}
}
