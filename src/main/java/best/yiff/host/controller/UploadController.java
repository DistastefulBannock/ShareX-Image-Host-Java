/**
 * 
 */
package best.yiff.host.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.model.ModelUpload;
import best.yiff.host.security.AccountUserDetails;
import best.yiff.host.service.storage.StorageService;
import best.yiff.host.service.storage.StorageServiceException;

/**
 * @author DistastefulBannock
 *
 */
@Controller
@RequestMapping(value = "/u")
public class UploadController {
	
	@Autowired
	private StorageService storageService;
	
	@Value(value = "${s3.bucketName}")
	private String bucketName;
	
	@GetMapping(value = "/")
	public String index(HttpServletResponse response) {
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			
		}
		return "empty";
	}
	
	@GetMapping(value = "/{id}")
	public String id(Model model, HttpServletResponse response, @PathVariable(value = "id", required = true) String id) {
		
		// Load the upload from the storage service
		ModelUpload upload;
		try {
			upload = storageService.load(id);
		} catch (StorageServiceException e1) {
			upload = null;
		}
		
		// If there is no upload with this id then redirect to the home page
		if (upload == null) {
			try {
				response.sendRedirect("/");
			} catch (IOException e) {
				
			}
			return "empty";
		}
		
		// Hand a reference for the logged in user to thymeleaf
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			ModelAccount user = accountUserDetails.getAccount();
			model.addAttribute("user", user);
		} catch (Exception e) {}
		
		// Hand thymeleaf upload data
		model.addAttribute("upload", upload);
		model.addAttribute("uploadUrl", "https://s3.amazonaws.com/" + bucketName + "/" + id);
		
		return "upload";
	}
	
}
