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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hobsoft.microbrowser.Form;
import org.hobsoft.microbrowser.Link;
import org.hobsoft.microbrowser.Microbrowser;
import org.hobsoft.microbrowser.MicrodataDocument;
import org.hobsoft.microbrowser.MicrodataItem;
import org.hobsoft.microbrowser.MicrodataProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.mockwebserver.MockResponse;
import com.google.mockwebserver.MockWebServer;
import com.google.mockwebserver.RecordedRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hobsoft.microbrowser.tck.RecordedRequestMatcher.get;
import static org.hobsoft.microbrowser.tck.RecordedRequestMatcher.post;
import static org.junit.Assert.assertThat;

/**
 * TCK for {@code Microbrowser}.
 */
public abstract class MicrobrowserTck
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private MockWebServer server;
	
	// ----------------------------------------------------------------------------------------------------------------
	// test case methods
	// ----------------------------------------------------------------------------------------------------------------

	@Before
	public final void setUpTck()
	{
		Logger.getLogger("com.google.mockwebserver").setLevel(Level.WARNING);
		
		server = new MockWebServer();
	}

	@After
	public final void tearDownTck() throws IOException
	{
		server.shutdown();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Microbrowser.get tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void getRequestsPath() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(server.getUrl("/x").toString());
		
		assertThat("request path", server.takeRequest().getPath(), is("/x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getItem tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetItemReturnsItem() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='x'/>"
			+ "</body></html>"));
		server.play();
		
		MicrodataItem actual = newBrowser().get(url(server))
			.getItem("x");
		
		assertThat("item", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetItemWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body/></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getItem("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem.getType tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void itemGetTypeReturnsType() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("x")
			.getType();
		
		assertThat("item type", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataItem.getProperty tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void itemGetPropertyReturnsProperty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataProperty actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("x");
		
		assertThat("item property", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void itemGetPropertyWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getItem("i")
			.getProperty("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataProperty.getName tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void propertyGetNameReturnsName() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("x")
			.getName();
		
		assertThat("item property name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataProperty.getValue tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void propertyGetValueWhenMetaReturnsContent() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meta itemprop='p' content='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenMetaAndNoContentReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meta itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenAudioReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void propertyGetValueWhenAudioAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<audio itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenEmbedReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void propertyGetValueWhenEmbedAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<embed itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenIframeReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}
	
	@Test
	public void propertyGetValueWhenIframeAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<iframe itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenImgReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}
	
	@Test
	public void propertyGetValueWhenImgAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<img itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenSourceReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}
	
	@Test
	public void propertyGetValueWhenSourceAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<source itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenTrackReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}
	
	@Test
	public void propertyGetValueWhenTrackAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<track itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenVideoReturnsSrc() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p' src='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}
	
	@Test
	public void propertyGetValueWhenVideoAndNoSrcReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<video itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenAnchorReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void propertyGetValueWhenAnchorAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<a itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenAreaReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void propertyGetValueWhenAreaAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<area itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenLinkReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p' href='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void propertyGetValueWhenLinkAndNoHrefReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<link itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenObjectReturnsAbsoluteUrl() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<object itemprop='p' data='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, equalToIgnoringCase(server.getUrl("/x").toString()));
	}

	@Test
	public void propertyGetValueWhenObjectAndNoDataReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<object itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenDataReturnsValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<data itemprop='p' value='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	@Test
	public void propertyGetValueWhenDataAndNoValueReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<data itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}

	@Test
	public void propertyGetValueWhenMeterReturnsValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meter itemprop='p' value='x'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}
	
	@Test
	public void propertyGetValueWhenMeterAndNoValueReturnsEmpty() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<meter itemprop='p'/>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, isEmptyString());
	}
	
	@Test
	public void propertyGetValueWhenUnknownReturnsText() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getItem("i")
			.getProperty("p")
			.getValue();
		
		assertThat("item property value", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getLink tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetLinkWhenAnchorReturnsLink() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		Link actual = newBrowser().get(url(server))
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test
	public void documentGetLinkWhenLinkReturnsLink() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		Link actual = newBrowser().get(url(server))
			.getLink("x");
		
		assertThat("link", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetLinkWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body/></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getLink("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Link.getRel tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkGetRelWhenAnchorReturnsRelationship() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	@Test
	public void linkGetRelWhenLinkReturnsRelationship() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<link rel='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getLink("x")
			.getRel();
		
		assertThat("link rel", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Link.follow tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void linkFollowWhenAnchorSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/x'>a</a>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getLink("r")
			.follow();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test
	public void linkFollowWhenAnchorReturnsResponse() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<a rel='r' href='/a'>a</a>"
			+ "</body></html>"));
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataDocument actual = newBrowser().get(url(server))
			.getLink("r")
			.follow();
		
		server.takeRequest();
		assertThat("request", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// MicrodataDocument.getForm tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void documentGetFormReturnsForm() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server.play();
		
		Form actual = newBrowser().get(url(server))
			.getForm("x");
		
		assertThat("form", actual, is(notNullValue()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void documentGetFormWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body/></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.getName tests
	// ----------------------------------------------------------------------------------------------------------------

	public void formGetNameReturnsName() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='x'/>"
			+ "</body></html>"));
		server.play();
		
		String actual = newBrowser().get(url(server))
			.getForm("x")
			.getName();
		
		assertThat("form name", actual, is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.getParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test(expected = IllegalArgumentException.class)
	public void formGetParameterWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.getParameter("x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.setParameter tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void formSetParameterSetsValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'>"
			+ "<input type='text' name='p'/>"
			+ "</form>"
			+ "</body></html>"));
		server.play();
		
		Form form = newBrowser().get(url(server))
			.getForm("f");
		form.setParameter("p", "x");
		
		assertThat("form parameter value", form.getParameter("p"), is("x"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void formSetParameterWhenNotFoundThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.setParameter("p", "x");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Form.submit tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void formSubmitWhenSubmitInputSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test
	public void formSubmitWhenSubmitButtonSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test
	public void formSubmitWhenDefaultButtonSubmitsRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<button/>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server).getPath(), is("/x"));
	}

	@Test(expected = IllegalStateException.class)
	public void formSubmitWhenNoSubmitButtonThrowsException() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f'/>"
			+ "</body></html>"));
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
	}

	@Test
	public void formSubmitWhenNoMethodSubmitsGetRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(get("/x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsGetRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(get("/x")));
	}

	@Test
	public void formSubmitWhenGetSubmitsTextControlValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='text' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(get("/a?p=x")));
	}

	@Test
	public void formSubmitWhenGetReturnsResponse() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='get' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataDocument actual = newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}

	@Test
	public void formSubmitWhenPostSubmitsPostRequest() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/x'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		server.takeRequest();
		assertThat("request", takeRequest(server), is(post("/x")));
	}

	@Test
	public void formSubmitWhenPostSubmitsTextControlValue() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='text' name='p'/>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse());
		server.play();
		
		newBrowser().get(url(server))
			.getForm("f")
			.setParameter("p", "x")
			.submit();
		
		server.takeRequest();
		assertThat("request body", body(takeRequest(server)), is("p=x"));
	}

	@Test
	public void formSubmitWhenPostReturnsResponse() throws IOException, InterruptedException
	{
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<form name='f' method='post' action='/a'>"
			+ "<input type='submit'>"
			+ "</form>"
			+ "</body></html>"));
		server.enqueue(new MockResponse().setBody("<html><body>"
			+ "<div itemscope='itemscope' itemtype='i'>"
			+ "<p itemprop='p'>x</p>"
			+ "</div>"
			+ "</body></html>"));
		server.play();
		
		MicrodataDocument actual = newBrowser().get(url(server))
			.getForm("f")
			.submit();
		
		assertThat("response", actual.getItem("i").getProperty("p").getValue(), is("x"));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// protected methods
	// ----------------------------------------------------------------------------------------------------------------

	protected abstract Microbrowser newBrowser();
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static String url(MockWebServer server)
	{
		return server.getUrl("/").toString();
	}
	
	/**
	 * Workaround MockWebServer issue #11.
	 * 
	 * @see https://code.google.com/p/mockwebserver/issues/detail?id=11
	 */
	private static RecordedRequest takeRequest(MockWebServer server) throws InterruptedException
	{
		RecordedRequest request = server.takeRequest();
		
		while ("GET /favicon.ico HTTP/1.1".equals(request.getRequestLine()))
		{
			request = server.takeRequest();
		}
		
		return request;
	}
	
	private static String body(RecordedRequest request)
	{
		return new String(request.getBody(), Charset.forName("ISO-8859-1"));
	}
}
