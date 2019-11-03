
package spark;

import spark.route.HttpMethod;
import spark.route.IRouteMatcher;
import spark.route.RouteMatcherFactory;


public class Spark {

    private static boolean initialized = false;

    private static IRouteMatcher routeMatcher;
    

    /**
     * Map the route for HTTP GET requests
     * 
     * @param route The route
     */
    public static void get(Route route) {
        addRoute(HttpMethod.get.name(), route);
    }

    /**
     * Map the route for HTTP POST requests
     * 
     * @param route The route
     */
    public static void post(Route route) {
        addRoute(HttpMethod.post.name(), route);
    }

    /**
     * Map the route for HTTP PUT requests
     * 
     * @param route The route
     */
    public static void put(Route route) {
        addRoute(HttpMethod.put.name(), route);
    }

    /**
     * Map the route for HTTP DELETE requests
     * 
     * @param route The route
     */
    public static void delete(Route route) {
        addRoute(HttpMethod.delete.name(), route);
    }

    /**
     * Map the route for HTTP HEAD requests
     * 
     * @param route The route
     */
    public static void head(Route route) {
        addRoute(HttpMethod.head.name(), route);
    }

    /**
     * Map the route for HTTP TRACE requests
     * 
     * @param route The route
     */
    public static void trace(Route route) {
        addRoute(HttpMethod.trace.name(), route);
    }

    /**
     * Map the route for HTTP CONNECT requests
     * 
     * @param route The route
     */
    public static void connect(Route route) {
        addRoute(HttpMethod.connect.name(), route);
    }
    
    /**
     * Map the route for HTTP OPTIONS requests
     * 
     * @param route The route
     */
    public static void options(Route route) {
        addRoute(HttpMethod.options.name(), route);
    }
    
    /**
     * Maps a filter to be executed before any matching routes
     * 
     * @param filter The filter
     */
    public static void before(Filter filter) {
        addFilter(HttpMethod.before.name(), filter);
    }
    
    /**
     * Maps a filter to be executed after any matching routes
     * 
     * @param filter The filter
     */
    public static void after(Filter filter) {
        addFilter(HttpMethod.after.name(), filter);
    }
    
    private static void addRoute(String httpMethod, Route route) {
        routeMatcher.parseValidateAddRoute(httpMethod + " '" + route.getPath() + "'", route);
    }
    
    private static void addFilter(String httpMethod, Filter filter) {
        routeMatcher.parseValidateAddRoute(httpMethod + " '" + filter.getPath() + "'", filter);
    }

    synchronized static void runFromServlet() {
        if (!initialized) {
            routeMatcher = RouteMatcherFactory.get();
            initialized = true;
        }
    }
    
    // WARNING, used for jUnit testing only!!!
    synchronized static void clearRoutes() {
        routeMatcher.clearRoutes();
    }
  
}