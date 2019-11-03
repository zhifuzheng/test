package com.xryb.zhtc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
/**
 * 用户session过期判断
 * @author hshzh
 *
 */
public class SessionFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String url = req.getRequestURI();
		String queryParam = req.getQueryString();
		if(queryParam == null) {
			queryParam = "";
		}
		String path = req.getContextPath();
		if (url.indexOf("/Public/") > 0 || url.indexOf("/static/") > 0 || url.indexOf("/mobile/") > 0) {
			//外部调用，不用session过滤,如有需要的话
			chain.doFilter(request, response);		
		}else if(req.getSession().getAttribute("userinfo") == null && req.getSession().getAttribute("vipinfo") == null) {
			if(!url.endsWith(path + "/") && 
			   !url.endsWith("/admin/login_bg.jpeg") && 
			   !url.endsWith("/admin/logout.jsp") &&
			   !url.endsWith("/admin/login.jsp") &&
			   !url.endsWith("/system/user/userLogin") &&
			   !url.endsWith("/system/vip/vipLoginByPc") &&
			   !url.endsWith("/system/img/getImg") 			   
			){
				//转发到logout.jsp
				request.getRequestDispatcher("/admin/logout.jsp").forward(request, response);//session超期
				return;
			} else {
				chain.doFilter(request, response);
			}
		} else{
				chain.doFilter(request, response);
		}
	}
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
