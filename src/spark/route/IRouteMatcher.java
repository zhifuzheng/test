
package spark.route;


public interface IRouteMatcher {
    
    public static final String ROOT = "/";
    public static final char SINGLE_QUOTE = '\'';
    
    /**
     * Parses, validates and adds a route
     * 
     * @param route
     * @param target
     */
    void parseValidateAddRoute(String route, Object target);
    
    /**
     * Finds the a target route for the requested route path
     * 
     * @param httpMethod
     * @param route
     * @return
     */
    RouteMatch findTargetForRequestedRoute(HttpMethod httpMethod, String route);


    /**
     * Clear all routes
     */
    void clearRoutes();
}
