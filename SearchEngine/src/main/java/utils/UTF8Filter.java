package utils;

import javax.servlet.*;
import java.io.IOException;
 
public class UTF8Filter implements Filter {
 
    public void init(FilterConfig filterConfig) {
    } 
 
    public void destroy() {
    }
 
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}