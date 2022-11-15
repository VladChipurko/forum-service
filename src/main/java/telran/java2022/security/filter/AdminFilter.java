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
import telran.java2022.security.context.SecurityContext;
@Component
@Order(20)
@RequiredArgsConstructor
public class AdminFilter implements Filter {
	
//	final UserRepository userRepository;
	final SecurityContext securityContext;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if(checkEndPoint(request.getMethod(), request.getServletPath())) {
//			User user = userRepository.findById(request.getUserPrincipal()
//					.getName()).get();
			telran.java2022.security.context.User user = securityContext.getUser(request.getUserPrincipal().getName());
			if(!user.getRoles().contains("Admin".toUpperCase())) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return servletPath.matches("/account/user/\\w+/role/\\w+/?");
	}

}
