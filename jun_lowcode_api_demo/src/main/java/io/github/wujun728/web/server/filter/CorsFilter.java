package io.github.wujun728.web.server.filter;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 * @version 1.0
 * @date 2024-05-06
 */
public class CorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
						 FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods","POST, GET, DELETE, PUT");
		//Origin, X-Requested-With, Content-Type, User-Agent Accept, Authorization, Lang
		response.addHeader("Access-Control-Allow-Headers","*");
		response.setHeader("Access-Control-Max-Age", "1728000");

		if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())){
			response.setStatus(HttpStatus.NO_CONTENT.value());
			return;
		}

		filterChain.doFilter(request, response);
	}
}
