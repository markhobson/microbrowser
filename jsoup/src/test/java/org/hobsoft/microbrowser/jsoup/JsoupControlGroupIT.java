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
package org.hobsoft.microbrowser.jsoup;

import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.tck.ControlGroupTck;

/**
 * Integration test that executes the {@code ControlGroup} TCK against {@code JsoupMicrobrowser}.
 */
public class JsoupControlGroupIT extends ControlGroupTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// AbstractMicrobrowserTest methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected Microbrowser newBrowser()
	{
		return new JsoupMicrobrowser();
	}
}
