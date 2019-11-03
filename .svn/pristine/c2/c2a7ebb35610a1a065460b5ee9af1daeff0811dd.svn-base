package spark.servlet;

import java.util.Set;
import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import spark.annotation.WSAccess;
import spark.utils.ScanUtil;

@SuppressWarnings("serial")
public class WebServiceServlet extends CXFNonSpringServlet {
	private static org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(WebServiceServlet.class);
	@Override
	protected void loadBus(ServletConfig sc) {
		super.loadBus(sc);
		BusFactory.setDefaultBus(getBus());
		String scanPath = sc.getInitParameter("scan");
		LOG.debug("scan WebService path:"+scanPath);
		if (scanPath != null) {
			Set<Class<?>> classes = ScanUtil.getClasses(scanPath);
			for (Class<?> c : classes) {
				if (!c.isInterface()) {
					WSAccess access = c.getAnnotation(WSAccess.class);
					if (access != null) {
						try {
							Endpoint.publish(access.path(), c.newInstance());
							LOG.debug("publish WebService:"+access.path()+" "+c.getName());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
