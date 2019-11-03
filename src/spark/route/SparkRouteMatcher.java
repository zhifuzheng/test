package spark.route;

import java.util.List;
import java.util.Map;
import javolution.util.FastMap;
import spark.utils.SparkUtils;

public class SparkRouteMatcher implements IRouteMatcher {

	private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(SparkRouteMatcher.class);

	private Map<String, RouteEntry> routeMap;

	private static class RouteEntry {

		private HttpMethod httpMethod;
		private String path;
		private Object target;

		private boolean matches(HttpMethod httpMethod, String path) {
			if ((httpMethod == HttpMethod.before || httpMethod == HttpMethod.after) && (this.httpMethod == httpMethod)
					&& this.path.equals(SparkUtils.ALL_PATHS)) {
				// Is filter and matches all
				return true;
			}
			boolean match = false;
			if (this.httpMethod == httpMethod) {
				match = matchPath(path);
			}
			return match;
		}

		private boolean matchPath(String path) {
			if (!this.path.endsWith("*")
					&& ((path.endsWith("/") && !this.path.endsWith("/")) || (this.path.endsWith("/") && !path.endsWith("/")))) {
				// One and not both ends with slash
				return false;
			}
			if (this.path.equals(path)) {
				// Paths are the same
				return true;
			}

			// check params
			List<String> thisPathList = SparkUtils.convertRouteToList(this.path);
			List<String> pathList = SparkUtils.convertRouteToList(path);

			int thisPathSize = thisPathList.size();
			int pathSize = pathList.size();

			if (thisPathSize == pathSize) {
				for (int i = 0; i < thisPathSize; i++) {
					String thisPathPart = thisPathList.get(i);
					String pathPart = pathList.get(i);

					if ((i == thisPathSize - 1)) {
						if (thisPathPart.equals("*") && this.path.endsWith("*")) {
							// wildcard match
							return true;
						}
					}

					if (!thisPathPart.startsWith(":") && !thisPathPart.equals(pathPart)) {
						return false;
					}
				}
				// All parts matched
				return true;
			} else {
				// Number of "path parts" not the same
				// check wild card:
				if (this.path.endsWith("*")) {
					if (pathSize == (thisPathSize - 1) && (path.endsWith("/"))) {
						// Hack for making wildcards work with trailing slash
						pathList.add("");
						pathList.add("");
						pathSize += 2;
					}

					if (thisPathSize < pathSize) {
						for (int i = 0; i < thisPathSize; i++) {
							String thisPathPart = thisPathList.get(i);
							String pathPart = pathList.get(i);
							if (thisPathPart.equals("*") && (i == thisPathSize - 1) && this.path.endsWith("*")) {
								// wildcard match
								return true;
							}
							if (!thisPathPart.startsWith(":") && !thisPathPart.equals(pathPart)) {
								return false;
							}
						}
						// All parts matched
						return true;
					}
					// End check wild card
				}
				return false;
			}
		}

		public String toString() {
			return httpMethod.name() + ", " + path + ", " + target;
		}
	}

	public SparkRouteMatcher() {
		routeMap = new FastMap<String, RouteEntry>();
	}

	@Override
	public RouteMatch findTargetForRequestedRoute(HttpMethod httpMethod, String path) {
		RouteEntry entry = routeMap.get(httpMethod.name() + path);
		if (null != entry) {
			if (entry.matches(httpMethod, path)) {
				return new RouteMatch(httpMethod, entry.target, entry.path, path);
			}
		}
		return null;
	}

	@Override
	public void parseValidateAddRoute(String route, Object target) {
		try {
			int singleQuoteIndex = route.indexOf(SINGLE_QUOTE);
			String httpMethod = route.substring(0, singleQuoteIndex).trim().toLowerCase();
			String url = route.substring(singleQuoteIndex + 1, route.length() - 1).trim().toLowerCase();

			// Use special enum stuff to get from value
			HttpMethod method;
			try {
				method = HttpMethod.valueOf(httpMethod);
			} catch (IllegalArgumentException e) {
				LOG.debug("The @Route value: " + route + " has an invalid HTTP method part: " + httpMethod + ".");
				return;
			}
			addRoute(method, url, target);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("The @Route value: " + route + " is not in the correct format");
		}

	}

	private void addRoute(HttpMethod method, String url, Object target) {
		RouteEntry entry = new RouteEntry();
		entry.httpMethod = method;
		entry.path = url;
		entry.target = target;
		LOG.debug("Adds route: " + entry);
		// Adds to end of list

		switch (method) {
		case before:
		case after:
			routeMap.put(method.name(), entry);
			break;
		default:
			routeMap.put(method.name() + url, entry);
			break;
		}
		
	}

	@Override
	public void clearRoutes() {
		routeMap.clear();
	}

}
