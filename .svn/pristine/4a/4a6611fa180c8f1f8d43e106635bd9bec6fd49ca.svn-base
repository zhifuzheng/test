package spark;


/**
 * A Route is built up by a path (for url-matching) and the implementation of the 'handle' method.
 * When a request is made, if present, the matching routes 'handle' method is invoked. The object
 * that is returned from 'handle' will be set to the response body (toString()).
 *
 * @author Per Wendel
 */
public abstract class Route extends AbstractRoot {

    private String path;
    private boolean useTransaction;
    private String dataSourceId;
    
    /**
     * Constructor
     * 
     * @param path The route path which is used for matching. (e.g. /hello, users/:name) 
     */
    protected Route(String path) {
        this.path = path;
        this.useTransaction = false;
    }
    
    /**
     * Constructor 
     * @param path The route path which is used for matching. (e.g. /hello, users/:name) 
     * @param useTransaction used transaction support
     * @param sourceId dataSourceId id
     */
    protected Route(String path,boolean useTransaction,String dataSourceId) {
        this.path = path;
        this.useTransaction = useTransaction;
        this.dataSourceId = dataSourceId;
    }
    
    /**
     * 
     * @return
     */
    public boolean isTransaction() {
    	return this.useTransaction;
    }
    
    public String getDataSourceId() {
    	return this.dataSourceId;
    }
    
    /**
     * Invoked when a request is made on this route's corresponding path e.g. '/hello'
     * 
     * @param request The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     * 
     * @return The content to be set in the response
     * @throws Exception 
     */
    public abstract Object handle(Request request, Response response) throws Exception;

    /**
     * Returns this route's path
     */
    String getPath() {
        return this.path;
    }
    
}
