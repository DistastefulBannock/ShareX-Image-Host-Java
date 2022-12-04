/**
 * 
 */
package best.yiff.host.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author DistastefulBannock
 *
 */
@Controller
@RequestMapping(value = "/u")
public class UploadController {
	
	@GetMapping(value = "/")
	public String index(HttpServletResponse response) {
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			
		}
		return "empty";
	}
	
	@GetMapping(value = "/{id}")
	public String id(@PathVariable(value = "id", required = true) String id) {
		return "empty";
	}
	
}
