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
import telran.java2022.account.model.User;

@Component
@RequiredArgsConstructor
@Order(30)
public class AccountOwnerFilter implements Filter {
	
	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if(checkEndPoint(request.getServletPath())) {
			String[] path = request.getServletPath().split("/");
			User user = userRepository.findById(request.getUserPrincipal()
					.getName()).get();
			if("PUT".equalsIgnoreCase(request.getMethod()) 
					&& (!user.getLogin().equalsIgnoreCase(path[path.length - 1]))) {
				response.sendError(403, "not owner");
				return;
			}
			if("DELETE".equalsIgnoreCase(request.getMethod()) 
					&&(!user.getLogin().equalsIgnoreCase(path[path.length - 1]) 
					&& !user.getRoles().contains("Admin".toUpperCase()))) {
				response.sendError(403, "not owner or administrator");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkEndPoint(String servletPath) {
		return servletPath.matches("/account/user/\\w+/?");
	}

}
