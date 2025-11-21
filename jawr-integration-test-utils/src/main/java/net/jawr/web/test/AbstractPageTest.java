/**
 *
 */
package net.jawr.web.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.htmlunit.BrowserVersion;
import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.Page;
import org.htmlunit.TextPage;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlImageInput;
import org.htmlunit.html.HtmlLink;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlScript;

import net.jawr.web.test.utils.Utils;

/**
 * The base class for integration tests in Jawr. This class is responsible of
 * creating the Jetty server at the beginning of the tests defined in the
 * current class. The configuration files (Web.xml and jawr.properties) are
 * updated before the start of the tests.
 *
 * To launch the tests, we use the maven command : mvn integration-test The root
 * folder of the application is defined in "target/jawr-integration-test".
 *
 * @author Ibrahim Chaehoi
 */
public abstract class AbstractPageTest {

	/** The logger */
	private static Logger LOGGER = LoggerFactory.getLogger(AbstractPageTest.class);

	/** The web client */
	protected WebClient webClient;

	/** The list of alerts collected */
	protected List<String> collectedAlerts;

	/** The HTML page */
	protected HtmlPage page;

	@BeforeClass
	public static void setInitFlag() throws IOException {

		LOGGER.debug("****** Start Test case *********");
		JawrIntegrationServer.getInstance().initBeforeTestCase();
	}

	@Before
	public void setup() throws Exception {

		LOGGER.debug("****** Start Test "+getClass()+" *********");
		initConfigFile();
		JawrIntegrationServer.getInstance().setup();

		webClient = createWebClient();
		collectedAlerts = new ArrayList<String>();
		webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
		page = webClient.getPage(getPageUrl());

	}

	/**
	 * Initialize the web app config files
	 */
	protected void initConfigFile(){

		JawrTestConfigFiles annotationConfig = (JawrTestConfigFiles) getClass()
				.getAnnotation(JawrTestConfigFiles.class);
		try{
			String currentJawrConfigPath = annotationConfig.jawrConfig();
			String webappRootDir = JawrIntegrationServer.getInstance().getWebAppRootDir();

			OutputStream outFile = new FileOutputStream(new File(webappRootDir, "/WEB-INF/classes/jawr.properties"));
			IOUtils.copy(getClass().getClassLoader().getResourceAsStream(
					currentJawrConfigPath), outFile);
			IOUtils.closeQuietly(outFile);

			String currentWebXmlPath = annotationConfig.webXml();
			outFile = new FileOutputStream(new File(webappRootDir, "/WEB-INF/web.xml"));
			IOUtils.copy(getClass().getClassLoader().getResourceAsStream(
					currentWebXmlPath), outFile);
			IOUtils.closeQuietly(outFile);
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	@After
	public void teardown(){
		LOGGER.debug("****** End Test "+getClass()+" *********");
	}

	/**
	 * Returns the webapp context path
	 * @return the webapp context path
	 */
	protected String getUrlPrefix(){
		return JawrIntegrationServer.getInstance().getContextPath();
	}

	/**
	 * Returns the server url prefix
	 * @return the server url prefix
	 */
	protected String getServerUrlPrefix() {
		return JawrIntegrationServer.getInstance().getServerUrlPrefix();
	}

	/**
	 * Creates the web client
	 *
	 * @return the web client
	 */
	protected WebClient createWebClient() {
		
		WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
		// Defines the accepted language for the web client.
		webClient.addRequestHeader("Accept-Language", getAcceptedLanguage());
		return webClient;
	}

	/**
	 * Resets the test configuration.
	 *
	 * @throws Exception
	 *             if an exception occurs
	 */
	@AfterClass
	public static void resetTestConfiguration() throws Exception {

		JawrIntegrationServer.getInstance().resetTestConfiguration();
		LOGGER.debug("****** End Test case *********");

	}

	/**
	 * Returns the locale used for the test
	 *
	 * @return the locale used for the test
	 */
	public String getAcceptedLanguage() {
		return "en-us";
	}

	/**
	 * Returns the url of the page to test
	 *
	 * @return the url of the page to test
	 */
	protected abstract String getPageUrl();

	/**
	 * Assert that the content of the file name equals to the response of the
	 * page.
	 *
	 * @param fileName
	 *            the file name
	 * @param page
	 *            the web page
	 * @throws Exception
	 *             if an exception occurs.
	 */
	protected void assertContentEquals(String fileName, Page page)
			throws Exception {
		Utils.assertContentEquals(getClass(), fileName, page);
	}

	/**
	 * Returns the list of HTML script tags which have an src attribute.
	 *
	 * @return the list of HTML script tag.
	 */
	@SuppressWarnings("unchecked")
	protected List<HtmlScript> getJsScriptTags() {
		List<?> elements = page.getByXPath("html/head/script[@src]");
		return (List<HtmlScript>) elements;
	}

	/**
	 * Returns the list of HTML link tags.
	 *
	 * @return the list of HTML script tags.
	 */
	@SuppressWarnings("unchecked")
	protected List<HtmlLink> getHtmlLinkTags() {
		List<?> elements = page.getByXPath("html/head/link");
		return (List<HtmlLink>) elements;
	}

	/**
	 * Returns the list of HTML link tags.
	 *
	 * @return the list of HTML script tags.
	 */
	@SuppressWarnings("unchecked")
	protected List<HtmlImage> getHtmlImageTags() {
		List<?> elements = page.getByXPath("//img");
		return (List<HtmlImage>) elements;
	}

	/**
	 * Returns the list of HTML link tags.
	 *
	 * @return the list of HTML script tags.
	 */
	@SuppressWarnings("unchecked")
	protected List<HtmlImageInput> getHtmlImageInputTags() {
		List<?> elements = page.getByXPath("//input[@type='image']");
		return (List<HtmlImageInput>) elements;
	}

	/**
	 * Call the webserver to retrieve the javascript page associated to the Html
	 * script object.
	 *
	 * @param script
	 *            the Html script
	 * @return the javascript page as TextPage
	 * @throws IOException
	 *             if an IOException occurs
	 * @throws MalformedURLException
	 *             if a MalformedURLException occurs
	 */
	protected TextPage getJavascriptPage(final HtmlScript script)
			throws IOException, MalformedURLException {
		return webClient.getPage(JawrIntegrationServer.SERVER_URL + script.getSrcAttribute());
	}

	/**
	 * Call the webserver to retrieve the css page associated to the Html link
	 * object.
	 *
	 * @param css
	 *            the Html link
	 * @return the css page
	 * @throws IOException
	 *             if an IOException occurs
	 * @throws MalformedURLException
	 *             if a MalformedURLException occurs
	 */
	protected TextPage getCssPage(final HtmlLink css) throws IOException,
			MalformedURLException {
		return webClient.getPage(JawrIntegrationServer.SERVER_URL + css.getHrefAttribute());
	}

}
