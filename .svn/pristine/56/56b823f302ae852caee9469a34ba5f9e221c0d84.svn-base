package spark.servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import dbengine.util.EntityToTableUtil;

import javolution.util.FastMap;
import spark.Access;
import spark.annotation.Auto;
import spark.route.RouteMatcherFactory;
import spark.webserver.MatcherFilter;

public class SparkFilter implements Filter {

	public static final String APPLICATION_CLASS_PARAM = "applicationClass";
	public static final String IGNORE = "ignore";

	private String filterPath;

	private MatcherFilter matcherFilter;

	private final static Map<String, Integer> ignoreMap = new FastMap<String, Integer>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Access.runFromServlet();

		final ISparkApplication app = getApplication(filterConfig);
		app.run();

		filterPath = FilterTools.getFilterPath(filterConfig);
		matcherFilter = new MatcherFilter(RouteMatcherFactory.get(), true);

		String ignoreArr[] = filterConfig.getInitParameter(IGNORE).split("\\;");
		if (null != ignoreArr && ignoreArr.length > 0) {
			for (String k : ignoreArr)
				ignoreMap.put(k, 0);
		}
		EntityToTableUtil.EntityToTable("jdbcwrite","com.xryb.zhtc.entity");
	}

	public static boolean isIgnore(String key) {
		return ignoreMap.containsKey(key);
	}

	/**
	 * Returns an instance of {@link ISparkApplication} which on which
	 * {@link ISparkApplication#init() init()} will be called. Default
	 * implementation looks up the class name in the filterConfig using the key
	 * {@link #APPLICATION_CLASS_PARAM}. Subclasses can override this method to
	 * use different techniques to obtain an instance (i.e. dependency
	 * injection).
	 *
	 * @param filterConfig
	 *            the filter configuration for retrieving parameters passed to
	 *            this filter.
	 * @return the spark application containing the configuration.
	 * @throws ServletException
	 *             if anything went wrong.
	 */
	protected ISparkApplication getApplication(FilterConfig filterConfig) throws ServletException {
		try {
			String applicationClassName = filterConfig.getInitParameter(APPLICATION_CLASS_PARAM);
			Class<?> applicationClass = Class.forName(applicationClassName.trim());
			ISparkApplication application = (ISparkApplication) this.autowired(applicationClass);
			return application;
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		final String relativePath = FilterTools.getRelativePath(httpRequest, filterPath);

		httpRequest.setCharacterEncoding("UTF-8");

		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpRequest) {
			@Override
			public String getRequestURI() {
				return relativePath;
			}
		};
		matcherFilter.doFilter(requestWrapper, response, chain);
	}

	@Override
	public void destroy() {
	}

	/**
	 * 
	 * @param cls
	 * @return
	 */
	private Object autowired(Class<?> cls) {
		Object obj = null;
		try {
			obj = cls.newInstance();
			Field[] fields = cls.getDeclaredFields();
			Auto auto = null;
			Object diObj = null;
			for (Field fid : fields) {
				auto = fid.getAnnotation(Auto.class);
				if (auto != null) {
					Class<?> diClass = auto.name();
					if (diClass == null)
						throw new Exception("It's must set DICalss!");
					if (this.check(diClass))
						diObj = autowired(diClass);
					else
						diObj = diClass.newInstance();
					fid.setAccessible(true);
					fid.set(obj, diObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 
	 * @param cls
	 */
	private boolean check(Class<?> cls) {
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields) {
			if (null != f.getAnnotation(Auto.class))
				return true;
		}
		return false;
	}
}
