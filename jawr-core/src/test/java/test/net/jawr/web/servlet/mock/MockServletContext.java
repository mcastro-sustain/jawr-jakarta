/**
 * Copyright 2009-2013 Ibrahim Chaehoi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package test.net.jawr.web.servlet.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines a mock servlet container.
 * 
 * @author Ibrahim Chaehoi
 */
public class MockServletContext implements ServletContext {

	/** The logger */
	private static Logger logger = LoggerFactory
			.getLogger(MockServletContext.class);

	/** The base directory */
	private String baseDir;

	/** The map attributes */
	private Map<String, String> initParameters = new HashMap<String, String>();

	/** The map attributes */
	private Map<String, Object> attributes = new HashMap<String, Object>();

	/**
	 * Constructor
	 */
	public MockServletContext() {

	}

	/**
	 * Constructor
	 */
	public MockServletContext(String baseDir, String tempDir) {
		this.baseDir = baseDir.replace('/', File.separatorChar);
		this.baseDir = this.baseDir.replaceAll("%20", " ");

		tempDir = tempDir.replace('/', File.separatorChar);
		tempDir = tempDir.replaceAll("%20", " ");
		setAttribute("jakarta.servlet.context.tempdir", new File(tempDir));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getAttributeNames()
	 */
	@SuppressWarnings("unchecked")
	public Enumeration<String> getAttributeNames() {
		return new IteratorEnumeration(attributes.keySet().iterator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getContext(java.lang.String)
	 */
	public ServletContext getContext(String uripath) {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
	 */
	public String getInitParameter(String name) {
		return (String) initParameters.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getInitParameterNames()
	 */
	@SuppressWarnings("unchecked")
	public Enumeration<String> getInitParameterNames() {
		return new IteratorEnumeration(attributes.keySet().iterator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMajorVersion()
	 */
	public int getMajorVersion() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
	 */
	public String getMimeType(String file) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getMinorVersion()
	 */
	public int getMinorVersion() {
		return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
	 */
	public RequestDispatcher getNamedDispatcher(String name) {
		throw new RuntimeException("operation not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
	 */
	public String getRealPath(String path) {

		return baseDir + path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String path) {
		throw new RuntimeException("operation not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResource(java.lang.String)
	 */
	public URL getResource(String path) throws MalformedURLException {

		URL url = null;
		File file = new File(baseDir + path);
		if (file.exists()) {
			url = file.toURI().toURL();
		}
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
	 */
	public InputStream getResourceAsStream(String path) {

		path = path.replace('/', File.separatorChar);
		InputStream is = null;
		try {
			is = new FileInputStream(new File(baseDir, path));
		} catch (FileNotFoundException e) {
			logger.info("File for path : '" + path + "' not found");
		}

		return is;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
	 */
	public Set<String> getResourcePaths(String path) {

		path = path.replace('/', File.separatorChar);
		File resource = new File(baseDir, path);
		if (!resource.exists()) {
			// throw new InvalidPathException(baseDir + File.separator + path);
			return null;
		}

		// If the path is not valid throw an exception
		String[] resArray = resource.list();
		if (null == resArray)
			return null;

		// Make the returned dirs end with '/', to match a servletcontext
		// behavior.
		for (int i = 0; i < resArray.length; i++) {

			resArray[i] = path + resArray[i];
			if (isDirectory(resArray[i]))
				resArray[i] += '/';
		}
		Set<String> ret = new HashSet<String>();
		ret.addAll(Arrays.asList(resArray));

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.jawr.web.resource.bundle.ResourceHandler#isDirectory(java.lang.String
	 * )
	 */
	public boolean isDirectory(String path) {
		path = path.replace('/', File.separatorChar);
		return new File(baseDir, path).isDirectory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServerInfo()
	 */
	public String getServerInfo() {
		throw new RuntimeException("operation not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServlet(java.lang.String)
	 */
	public Servlet getServlet(String name) throws ServletException {
		throw new RuntimeException("operation not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServletContextName()
	 */
	public String getServletContextName() {
		throw new RuntimeException("operation not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServletNames()
	 */
	public Enumeration<String> getServletNames() {
		throw new RuntimeException("operation not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#getServlets()
	 */
	public Enumeration<?> getServlets() {
		throw new RuntimeException("operation not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.String)
	 */
	public void log(String msg) {
		logger.info(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.Exception,
	 * java.lang.String)
	 */
	public void log(Exception exception, String msg) {
		logger.info(msg, exception);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#log(java.lang.String,
	 * java.lang.Throwable)
	 */
	public void log(String message, Throwable throwable) {
		logger.info(message, throwable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContext#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.ServletContext#getContextPath()
	 */
	@Override
	public String getContextPath() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#setResponseCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setResponseCharacterEncoding(String encoding) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getResponseCharacterEncoding()
	 */
	@Override
	public String getResponseCharacterEncoding() {
		return "UTF-8";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#setRequestCharacterEncoding(java.lang.String)
	 */
	@Override
	public void setRequestCharacterEncoding(String encoding) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getRequestCharacterEncoding()
	 */
	@Override
	public String getRequestCharacterEncoding() {
		return "UTF-8";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#setSessionTimeout(int)
	 */
	@Override
	public void setSessionTimeout(int sessionTimeout) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getSessionTimeout()
	 */
	@Override
	public int getSessionTimeout() {
		return 30;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getVirtualServerName()
	 */
	@Override
	public String getVirtualServerName() {
		return "mock-server";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#declareRoles(java.lang.String...)
	 */
	@Override
	public void declareRoles(String... roleNames) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getJspConfigDescriptor()
	 */
	@Override
	public jakarta.servlet.descriptor.JspConfigDescriptor getJspConfigDescriptor() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#createListener(java.lang.Class)
	 */
	@Override
	public <T extends java.util.EventListener> T createListener(Class<T> c) throws jakarta.servlet.ServletException {
		try {
			return c.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new jakarta.servlet.ServletException("Cannot create listener", e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#addListener(java.lang.String)
	 */
	@Override
	public void addListener(String className) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#addListener(java.util.EventListener)
	 */
	@Override
	public <T extends java.util.EventListener> void addListener(T t) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#addListener(java.lang.Class)
	 */
	@Override
	public void addListener(Class<? extends java.util.EventListener> listenerClass) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getEffectiveMajorVersion()
	 */
	@Override
	public int getEffectiveMajorVersion() {
		return 6;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getEffectiveMinorVersion()
	 */
	@Override
	public int getEffectiveMinorVersion() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#setSessionTrackingModes(java.util.Set)
	 */
	@Override
	public void setSessionTrackingModes(java.util.Set<jakarta.servlet.SessionTrackingMode> sessionTrackingModes) {
		// Mock implementation - no-op
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getDefaultSessionTrackingModes()
	 */
	@Override
	public java.util.Set<jakarta.servlet.SessionTrackingMode> getDefaultSessionTrackingModes() {
		return java.util.EnumSet.of(jakarta.servlet.SessionTrackingMode.COOKIE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getEffectiveSessionTrackingModes()
	 */
	@Override
	public java.util.Set<jakarta.servlet.SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return java.util.EnumSet.of(jakarta.servlet.SessionTrackingMode.COOKIE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see jakarta.servlet.ServletContext#getSessionCookieConfig()
	 */
	@Override
	public jakarta.servlet.SessionCookieConfig getSessionCookieConfig() {
		return new jakarta.servlet.SessionCookieConfig() {
			@Override
			public void setName(String name) {}
			@Override
			public String getName() { return "JSESSIONID"; }
			@Override
			public void setDomain(String domain) {}
			@Override
			public String getDomain() { return null; }
			@Override
			public void setPath(String path) {}
			@Override
			public String getPath() { return "/"; }
			@Override
			public void setComment(String comment) {}
			@Override
			public String getComment() { return null; }
			@Override
			public void setHttpOnly(boolean httpOnly) {}
			@Override
			public boolean isHttpOnly() { return false; }
			@Override
			public void setSecure(boolean secure) {}
			@Override
			public boolean isSecure() { return false; }
			@Override
			public void setMaxAge(int maxAge) {}
			@Override
			public int getMaxAge() { return -1; }
			@Override
			public void setAttribute(String name, String value) {}
			@Override
			public String getAttribute(String name) { return null; }
			@Override
			public java.util.Map<String, String> getAttributes() { return new java.util.HashMap<>(); }
		};
	}

	@Override
	public jakarta.servlet.ServletRegistration.Dynamic addServlet(String servletName, String className) {
		return null;
	}

	@Override
	public jakarta.servlet.ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
		return null;
	}

	@Override
	public jakarta.servlet.ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		return null;
	}

	@Override
	public jakarta.servlet.ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz) throws jakarta.servlet.ServletException {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new jakarta.servlet.ServletException("Cannot create servlet", e);
		}
	}

	@Override
	public jakarta.servlet.ServletRegistration getServletRegistration(String servletName) {
		return null;
	}

	@Override
	public java.util.Map<String, ? extends jakarta.servlet.ServletRegistration> getServletRegistrations() {
		return new java.util.HashMap<>();
	}

	@Override
	public jakarta.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
		return null;
	}

	@Override
	public jakarta.servlet.FilterRegistration.Dynamic addFilter(String filterName, jakarta.servlet.Filter filter) {
		return null;
	}

	@Override
	public jakarta.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends jakarta.servlet.Filter> filterClass) {
		return null;
	}

	@Override
	public <T extends jakarta.servlet.Filter> T createFilter(Class<T> clazz) throws jakarta.servlet.ServletException {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new jakarta.servlet.ServletException("Cannot create filter", e);
		}
	}

	@Override
	public jakarta.servlet.FilterRegistration getFilterRegistration(String filterName) {
		return null;
	}

	@Override
	public java.util.Map<String, ? extends jakarta.servlet.FilterRegistration> getFilterRegistrations() {
		return new java.util.HashMap<>();
	}

	@Override
	public boolean setInitParameter(String name, String value) {
		return false;
	}

}
