
package spark.utils;

import java.util.List;

import javolution.util.FastTable;

public class SparkUtils {

    public static final String ALL_PATHS = "+/*paths";
    
    public static List<String> convertRouteToList(String route) {
        String[] pathArray = route.split("/");
        List<String> path = new FastTable<String>();
        for (String p : pathArray) {
            if (p.length() > 0) {
                path.add(p);
            }
        }
        return path;
    }
    
    public static boolean isParam(String routePart) {
        return routePart.startsWith(":");
    }
 
}
