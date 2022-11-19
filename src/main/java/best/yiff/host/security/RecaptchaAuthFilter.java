/**
 * 
 */
package best.yiff.host.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import de.triology.recaptchav2java.ReCaptcha;

/**
 * @author DistastefulBannock
 *
 */
public class RecaptchaAuthFilter implements Filter {
	
	@Autowired
	private ReCaptcha reCaptcha;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if (!reCaptcha.isValid(request.getParameter("g-recaptcha-response"))) {
			((HttpServletResponse)response).sendError(HttpStatus.BAD_REQUEST.value(), "Bad captcha");
			return;
		}
		
		chain.doFilter(request, response);
	}
	
}
