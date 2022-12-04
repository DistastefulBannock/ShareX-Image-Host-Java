/**
 * 
 */
package best.yiff.host.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.security.AccountUserDetails;
import best.yiff.host.service.account.AccountService;
import best.yiff.host.service.account.AccountServiceException;

/**
 * @author DistastefulBannock
 *
 */
@Controller
public class IndexController {
	
	@Autowired
	private AccountService accountService;
	
	@Value(value = "${recaptcha.site}")
	private String recaptchaSiteKey;
	
	@GetMapping("/")
	public String index(Model model) {
		
		// Hand a reference for the logged in user to thymeleaf
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			ModelAccount user = accountUserDetails.getAccount();
			model.addAttribute("user", user);
		} catch (Exception e) {}
		
		return "index";
	}
	
	@GetMapping(value = "/login")
	public String login(Model model, @RequestParam(name = "error", required = false) String error) {
		
		// Hand a reference for the logged in user to thymeleaf
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			ModelAccount user = accountUserDetails.getAccount();
			model.addAttribute("user", user);
		} catch (Exception e) {}
		
		// Other shit
		model.addAttribute("error", error != null);
		model.addAttribute("errorText", error);
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		
		return "login";
	}
	
	@GetMapping(value = "/register")
	public String register(Model model, @RequestParam(name = "error", required = false) String error) {
		
		// Hand a reference for the logged in user to thymeleaf
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			ModelAccount user = accountUserDetails.getAccount();
			model.addAttribute("user", user);
		} catch (Exception e) {}
		
		// Other shit
		model.addAttribute("error", error != null);
		model.addAttribute("errorText", error);
		model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
		
		return "register";
	}
	
	@PostMapping(value = "/pregister")
	public void processRegister(@RequestParam(name = "username") String username,
			@RequestParam(name = "email") String email, @RequestParam(name = "password") String password,
			@RequestParam(name = "confirmPassword") String confirmPassword, @RequestParam(name = "invite") String invite,
			HttpServletResponse response){
		
		// Make sure the password and confirm password fields match
		if (!password.equals(confirmPassword)) {
			response.setStatus(400);
			try {
				response.sendRedirect("register?error=" + URLEncoder.encode("Passwords don't match", StandardCharsets.UTF_8));
			} catch (IOException e) {}
			return;
		}
		
		// Register the account
		try {
			accountService.registerAccount(username, email, password, invite);
		} catch (AccountServiceException e) {
			response.setStatus(400);
			try {
				response.sendRedirect("register?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
			} catch (IOException e1) {}
			return;
		}
		
		// Send to login page
		response.setStatus(200);
		try {
			response.sendRedirect("login?error=" + URLEncoder.encode("Successfully created account", StandardCharsets.UTF_8));
		} catch (IOException e1) {}
	}
	
	@GetMapping(value = "/403")
	public String forbidden(Model model) {
		return "errors/403";
	}
	
}
