package telran.java2022.security.filter;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.account.dao.UserRepository;
import telran.java2022.account.dto.exceptions.UserNotFoundException;
import telran.java2022.account.model.User;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {
	
	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if(checkEndPoint(request.getMethod(), request.getServletPath())) {
			String token = request.getHeader("Authorization");
			if(token == null) {
				response.sendError(401);
				return;
			}
			String[] credentials = getCredentialsFromToken(token);
			if(!userRepository.existsById(credentials[0])) {
				response.sendError(404, "User with login " + credentials[0] + " not exist");
				return;
			}
			User user = userRepository.findById(credentials[0])
					.orElseThrow(() -> new UserNotFoundException(credentials[0]));
			if(!user.getPassword().equals(credentials[1])) {
				response.sendError(401, "Wrong password");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private String[] getCredentialsFromToken(String token) {
		String[] basicAuth = token.split(" ");
		String decode = new String(Base64.getDecoder().decode(basicAuth[1]));
		String[] credentials = decode.split(":");
		return credentials;
	}

	private boolean checkEndPoint(String method, String servletPath) {
		return !("POST".equalsIgnoreCase(method) && servletPath.equals("/account/register"));
	}

}
