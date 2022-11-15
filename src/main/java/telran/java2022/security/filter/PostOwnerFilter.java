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
//import telran.java2022.account.dao.UserRepository;
//import telran.java2022.account.model.User;
import telran.java2022.forum.dao.ForumRepository;
import telran.java2022.forum.model.Post;
import telran.java2022.security.context.SecurityContext;

@Component
@RequiredArgsConstructor
@Order(40)
public class PostOwnerFilter implements Filter {

	final ForumRepository forumRepository;
//	final UserRepository userRepository;
	final SecurityContext securityContext;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if(checkEndPoint(request.getMethod(), request.getServletPath())) {
			String[] path = request.getServletPath().split("/");
//			User user = userRepository.findById(request.getUserPrincipal()
//					.getName()).get();
			telran.java2022.security.context.User user = securityContext.getUser(request.getUserPrincipal().getName());
			Post post = forumRepository.findById(path[path.length - 1]).orElse(null);
			if(post == null) {
				response.sendError(401, "post not exist");
				return;
			}
			if("PUT".equalsIgnoreCase(request.getMethod()) 
					&& (!user.getUserName().equalsIgnoreCase(post.getAuthor()))) {
				response.sendError(403, "not owner");
				return;
			}
			if("DELETE".equalsIgnoreCase(request.getMethod()) 
					&&(!user.getUserName().equalsIgnoreCase(post.getAuthor()) 
					&& !user.getRoles().contains("Moderator".toUpperCase()))) {
				response.sendError(403, "not owner or moderator");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return ("DELETE".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) 
				&& servletPath.matches("/forum/post/\\w+/?");
	}

}
