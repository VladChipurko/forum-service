package telran.java2022.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.account.dao.UserRepository;
import telran.java2022.forum.dao.ForumRepository;

@Component
@RequiredArgsConstructor
@Order(50)
public class AddPostAndCommentFilter implements Filter {

	final ForumRepository forumRepository;
	final UserRepository userRepository;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if(checkEndPoint(request.getMethod(), request.getServletPath())) {
			String[] path = request.getServletPath().split("/");
			if(!request.getUserPrincipal().getName().equalsIgnoreCase(path[path.length - 1])) {
				response.sendError(403, "author don't equals with login");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return "POST".equalsIgnoreCase(method)&& servletPath.matches("/forum/post/\\w+/?") 
				|| "PUT".equalsIgnoreCase(method)&& servletPath.matches("/forum/post/\\w+/comment/\\w+/?");
	}

}
