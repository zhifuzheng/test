/*
 * Copyright 2011- Per Wendel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spark.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbengine.jdbc.ConnectionPoolFactory;
import spark.Access;
import spark.HaltException;
import spark.Request;
import spark.RequestResponseFactory;
import spark.Response;
import spark.Route;
import spark.route.HttpMethod;
import spark.route.RouteMatch;
import spark.route.IRouteMatcher;

/**
 * Filter for matching of filters and routes.
 * 
 * @author Per Wendel
 */
public class MatcherFilter implements Filter {

	private IRouteMatcher routeMatcher;
	private boolean isServletContext;

	/** The logger. */
	private org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(getClass());

	/**
	 * Constructor
	 * 
	 * @param routeMatcher
	 *            The route matcher
	 * @param isServletContext
	 *            If true, chain.doFilter will be invoked if request is not
	 *            consumed by Spark.
	 */
	public MatcherFilter(IRouteMatcher routeMatcher, boolean isServletContext) {
		this.routeMatcher = routeMatcher;
		this.isServletContext = isServletContext;
	}

	public void init(FilterConfig filterConfig) {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		long nowtime = System.currentTimeMillis();

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

		String httpMethodStr = httpRequest.getMethod().toLowerCase();
		String uri = httpRequest.getRequestURI().toLowerCase();

		Object bodyContent = null;

		RequestWrapper req = new RequestWrapper();
		ResponseWrapper res = new ResponseWrapper();

		Response response = RequestResponseFactory.create(httpResponse);

		String redirectUrl = null;
		try {
			// BEFORE filters
			RouteMatch matchBefore = routeMatcher.findTargetForRequestedRoute(HttpMethod.before, "");
			if (matchBefore != null) {
				Object filterTarget = matchBefore.getTarget();
				if (filterTarget != null && filterTarget instanceof spark.Filter) {
					Request request = RequestResponseFactory.create(matchBefore, httpRequest);
					spark.Filter filter = (spark.Filter) filterTarget;

					req.setDelegate(request);
					res.setDelegate(response);

					filter.handle(req, res);
					String bodyAfterFilter = Access.getBody(response);
					if (bodyAfterFilter != null) {
						bodyContent = bodyAfterFilter;
					}
				}
			}
			// BEFORE filters, END

			HttpMethod httpMethod = HttpMethod.valueOf(httpMethodStr);

			RouteMatch match = null;
			match = routeMatcher.findTargetForRequestedRoute(HttpMethod.valueOf(httpMethodStr), uri);
			Object target = null;
			if (match != null) {
				target = match.getTarget();
			} else if (httpMethod == HttpMethod.head && bodyContent == null) {
				// See if get is mapped to provide default head mapping
				bodyContent = routeMatcher.findTargetForRequestedRoute(HttpMethod.get, uri) != null ? "" : null;
			}

			if (target != null && !response.isRedirect) {
				try {
					Object result = null;
					if (target instanceof Route) {
						Route route = ((Route) target);
						Request request = RequestResponseFactory.create(match, httpRequest);

						req.setDelegate(request);
						res.setDelegate(response);

						// 开启事务,针对多表操作
						if (route.isTransaction()) {
							Connection conn = ConnectionPoolFactory.getInstance().getConnection(route.getDataSourceId());
							boolean autoCommit = conn.getAutoCommit();
							conn.setAutoCommit(false);
							try {
								result = route.handle(req, res);
								conn.commit();
							} catch (Exception ex) {
								conn.rollback();
								throw new RuntimeException(ex);
							} finally {
								conn.setAutoCommit(autoCommit);
								ConnectionPoolFactory.getInstance().close(conn);
							}
						} else {
							result = route.handle(req, res);
						}
					}
					if (result != null) {
						bodyContent = result;
					}
				} catch (HaltException hEx) {
					throw hEx;
				} catch (Exception e) {
					e.printStackTrace();
					httpResponse.setStatus(500);
					bodyContent = INTERNAL_ERROR;
				}
			}

			// AFTER filters
			RouteMatch matchAfter = routeMatcher.findTargetForRequestedRoute(HttpMethod.after, "");
			if (null != matchAfter) {
				Object filterTarget = matchAfter.getTarget();
				if (filterTarget != null && filterTarget instanceof spark.Filter) {
					Request request = RequestResponseFactory.create(matchAfter, httpRequest);

					req.setDelegate(request);
					res.setDelegate(response);

					spark.Filter filter = (spark.Filter) filterTarget;
					filter.handle(req, res);

					String bodyAfterFilter = Access.getBody(response);
					if (bodyAfterFilter != null) {
						bodyContent = bodyAfterFilter;
					}
				}
			}
			// AFTER filters, END

		} catch (HaltException hEx) {
			LOG.debug("halt performed");
			redirectUrl = hEx.getUrl();

			httpResponse.setStatus(hEx.getStatusCode());
			if (hEx.getBody() != null) {
				bodyContent = hEx.getBody();
			} else {
				bodyContent = "";
			}
		}

		if (response.isRedirect)
			bodyContent = "";
		boolean consumed = bodyContent != null;

		if (!consumed && !isServletContext) {
			httpResponse.setStatus(404);
			bodyContent = NOT_FOUND;
			consumed = true;
		}

		if (consumed) {
			if (httpResponse.isCommitted())
				return;
			if (redirectUrl != null) {
				String path = httpRequest.getContextPath();
				httpResponse.sendRedirect(path + redirectUrl);
				return;
			} else {
				byte[] bytes = null;
				// Write body content
				if (bodyContent instanceof byte[]) {
					bytes = (byte[]) bodyContent;
					httpResponse.setContentType("application/x-msdownload");
				} else {
					bytes = (bodyContent + "").getBytes("UTF-8");
					httpResponse.setContentType("text/html; charset=utf-8");
				}
				OutputStream out = httpResponse.getOutputStream();
				if (bytes != null)
					out.write(bytes);
				out.flush();
				out.close();
			}
		} else if (chain != null) {
			if (res.getResponse() == null || !res.getResponse().isRedirect)
				chain.doFilter(httpRequest, httpResponse);
		}
		LOG.debug((System.currentTimeMillis() - nowtime) + " ms.");
	}

	public void destroy() {
	}

	private static final String NOT_FOUND = "<html><body><h2>404 Not found</h2></body></html>";
	private static final String INTERNAL_ERROR = "<html><body><h2>500 Internal Error</h2></body></html>";
}
