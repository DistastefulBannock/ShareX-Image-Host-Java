/**
 * 
 */
package best.yiff.host.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.security.AccountUserDetails;
import best.yiff.host.service.account.AccountService;
import best.yiff.host.service.account.AccountServiceException;

/**
 * @author DistastefulBannock
 *
 */
@Controller
@RequestMapping(value = "/user")
@Secured(value = "ROLE_USER")
public class UserController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping(value = "/")
	public String index(Model model, HttpServletResponse response) {
		try {
			response.sendRedirect("panel");
		} catch (IOException e) {
			
		}
		return "empty";
	}
	
	@GetMapping(value = "/panel")
	public String panel(Model model) {
		
		// Hand a reference for the logged in user to thymeleaf
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			ModelAccount user = accountUserDetails.getAccount();
			model.addAttribute("user", user);
		} catch (Exception e) {}
		
		return "user/panel";
	}
	
	@PostMapping(value = "/regenerateUploadKey")
	public void regenerateUploadKey(HttpServletResponse response) {
		AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		ModelAccount user = accountUserDetails.getAccount();
		try {
			accountService.regenerateUploadKey(user);
		} catch (AccountServiceException e) {
			try {
				response.sendRedirect("panel?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
			} catch (IOException e1) {}
		}
		try {
			response.sendRedirect("panel");
		} catch (IOException e1) {}
	}
	
}
