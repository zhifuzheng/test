
package spark;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class Response {

    private HttpServletResponse response;
    private String body;
    public boolean isRedirect = false;
    
    protected Response() {
    }
    
    Response(HttpServletResponse response) {
        this.response = response;
    }
    
    
    /**
     * Sets the status code for the response
     */
    public void status(int statusCode) {
        response.setStatus(statusCode);
        
    }
    
    /**
     * Sets the content type for the response
     */
    public void type(String contentType) {
        response.setContentType(contentType);
    }
    
    /**
     * Sets the body
     */
    public void body(String body) {
       this.body = body;
    }
    
    public String body() {
       return this.body;
    }
    
    /**
     * Gets the raw response object handed in by Jetty
     */
    public HttpServletResponse raw() {
        return response;
    }

    /**
     *  Trigger a browser redirect
     * 
     * @param location Where to redirect
     */
    public void redirect(String location) {
        try {
        	this.isRedirect = true;
            response.sendRedirect(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Adds/Sets a response header
     */
    public void header(String header, String value) {
        response.addHeader(header, value);
    }
}
