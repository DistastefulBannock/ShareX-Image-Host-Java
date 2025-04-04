/**
 * 
 */
package best.yiff.host.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.repo.RepoAccounts;
import best.yiff.host.repo.RepoDomains;
import best.yiff.host.repo.RepoUploads;
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
	
	@Autowired
	private RepoUploads repoUploads;
	
	@Autowired
	private RepoDomains repoDomains;
	
	@Autowired
	private RepoAccounts repoAccounts;
	
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
		
		// Hand a reference for the logged in user along with how many uploads they have to thymeleaf
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			ModelAccount user = accountUserDetails.getAccount();
			model.addAttribute("user", user);
			model.addAttribute("uploadCount", repoUploads.countByUploader(user.getId()));
		} catch (Exception e) {}
		
		// Give thymeleaf list of all available domains
		model.addAttribute("domains", repoDomains.findAll());
		
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
	
	@GetMapping(value = "/generateConfig")
	public ResponseEntity<?> generateConfig(){
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			ModelAccount user = accountUserDetails.getAccount();
			
			// Make sure the user has an upload token
			if (user.getUploadKey() == null || user.getUploadKey().isBlank()) {
				return ResponseEntity.badRequest().body("No upload key exists for logged in account, please go back to the panel and refresh it");
			}
			
			// Create config, map to json, then return as stream
			ObjectMapper objectMapper = new ObjectMapper();
			byte[] config = objectMapper.writeValueAsBytes(new SharexConfigFile(user));
			ByteArrayInputStream bais = new ByteArrayInputStream(config);
			ResponseEntity<InputStreamResource> response = ResponseEntity.ok().contentLength(config.length)
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.header("Content-Disposition", "attachment; filename=\"yiff.sxcu\"")
					.body(new InputStreamResource(bais));
			bais.close();
			return response;
		} catch (Exception e) {
			return ResponseEntity.ok("Error");
		}
	}
	
	@PostMapping(value = "/domainConfig")
	public ResponseEntity<String> domainConfig(@RequestParam(name = "domains") String domainJson) {
		
		// Get user account
		ModelAccount user;
		try {
			AccountUserDetails accountUserDetails = ((AccountUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			user = accountUserDetails.getAccount();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("No logon");
		}
		
		// Check to make sure it isn't banned or disabled
		if (user.isBanned()) {
			return ResponseEntity.badRequest().body("Your account is banned");
		}
		if (user.isDisabled()) {
			return ResponseEntity.badRequest().body("Your account is disabled");
		}
		
		// Gets domains from json data
		ArrayList<String> domains = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(domainJson);
		
		// Set limit so users don't lag server by spamming massive domain update requests
		if (jsonArray.length() > 50) {
			return ResponseEntity.badRequest().body("Too many domains");
		}
		
		// Get domains from json array
		jsonArray.iterator().forEachRemaining(d -> {
			if (!(d instanceof String))
				return;
			domains.add((String) d);
		});
		
		// Save account domain config
		user.setDomains(domains);
		repoAccounts.saveAndFlush(user);
		
		// Return ok
		return ResponseEntity.ok("OK");
	}
	
	/**
	 * @author DistastefulBannock
	 * Config file following guidelines from https://getsharex.com/docs/custom-uploader
	 */
	private class SharexConfigFile{
		private String version = "14.0.0";
		private String name = "Yiff.best";
		private String destinationType = "ImageUploader, TextUploader, FileUploader";
		private String requestMethod = "POST";
		private String requestURL = "https://yiff.best/api/v1/upload";
		private Map<String, String> parameters = new HashMap<>();
		private String body = "MultipartFormData";
		private Map<String, String> arguments = new HashMap<>();
		private String fileFormName = "image";
		private String url = "{json:UploadUrl}";
		private String thumbnailURL = "{json:UploadUrl}";
		private String deletionURL = "{json:DeletionUrl}";
		private String errorMessage = "{json:ErrorMessage}";
		
		public SharexConfigFile() {}
		
		/**
		 * @param account The account to generate the config for
		 */
		public SharexConfigFile(ModelAccount account) {
			arguments.put("uid", account.getId() + "");
			arguments.put("key", account.getUploadKey());
		}
		
		/**
		 * @return the version
		 */
		@JsonProperty(value = "Version")
		public String getVersion() {
			return version;
		}

		/**
		 * @param version the version to set
		 */
		public void setVersion(String version) {
			this.version = version;
		}

		/**
		 * @return the name
		 */
		@JsonProperty(value = "Name")
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the destinationType
		 */
		@JsonProperty(value = "DestinationType")
		public String getDestinationType() {
			return destinationType;
		}

		/**
		 * @param destinationType the destinationType to set
		 */
		public void setDestinationType(String destinationType) {
			this.destinationType = destinationType;
		}

		/**
		 * @return the requestMethod
		 */
		@JsonProperty(value = "RequestMethod")
		public String getRequestMethod() {
			return requestMethod;
		}

		/**
		 * @param requestMethod the requestMethod to set
		 */
		public void setRequestMethod(String requestMethod) {
			this.requestMethod = requestMethod;
		}

		/**
		 * @return the requestURL
		 */
		@JsonProperty(value = "RequestURL")
		public String getRequestURL() {
			return requestURL;
		}

		/**
		 * @param requestURL the requestURL to set
		 */
		public void setRequestURL(String requestURL) {
			this.requestURL = requestURL;
		}

		/**
		 * @return the parameters
		 */
		@JsonProperty(value = "Parameters")
		public Map<String, String> getParameters() {
			return parameters;
		}

		/**
		 * @param parameters the parameters to set
		 */
		public void setParameters(Map<String, String> parameters) {
			this.parameters = parameters;
		}

		/**
		 * @return the body
		 */
		@JsonProperty(value = "Body")
		public String getBody() {
			return body;
		}

		/**
		 * @param body the body to set
		 */
		public void setBody(String body) {
			this.body = body;
		}

		/**
		 * @return the arguments
		 */
		@JsonProperty(value = "Arguments")
		public Map<String, String> getArguments() {
			return arguments;
		}

		/**
		 * @param arguments the arguments to set
		 */
		public void setArguments(Map<String, String> arguments) {
			this.arguments = arguments;
		}

		/**
		 * @return the fileFormName
		 */
		@JsonProperty(value = "FileFormName")
		public String getFileFormName() {
			return fileFormName;
		}

		/**
		 * @param fileFormName the fileFormName to set
		 */
		public void setFileFormName(String fileFormName) {
			this.fileFormName = fileFormName;
		}

		/**
		 * @return the url
		 */
		@JsonProperty(value = "URL")
		public String getUrl() {
			return url;
		}

		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * @return the thumbnailURL
		 */
		@JsonProperty(value = "ThumbnailURL")
		public String getThumbnailURL() {
			return thumbnailURL;
		}

		/**
		 * @param thumbnailURL the thumbnailURL to set
		 */
		public void setThumbnailURL(String thumbnailURL) {
			this.thumbnailURL = thumbnailURL;
		}

		/**
		 * @return the deletionURL
		 */
		@JsonProperty(value = "DeletionURL")
		public String getDeletionURL() {
			return deletionURL;
		}

		/**
		 * @param deletionURL the deletionURL to set
		 */
		public void setDeletionURL(String deletionURL) {
			this.deletionURL = deletionURL;
		}

		/**
		 * @return the errorMessage
		 */
		@JsonProperty(value = "ErrorMessage")
		public String getErrorMessage() {
			return errorMessage;
		}

		/**
		 * @param errorMessage the errorMessage to set
		 */
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

	}
	
}
