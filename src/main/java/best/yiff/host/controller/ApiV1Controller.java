package best.yiff.host.controller;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.repo.RepoAccounts;
import best.yiff.host.service.storage.StorageService;
import best.yiff.host.service.storage.StorageServiceException;

@Controller
@RequestMapping(value = "/api/v1")
public class ApiV1Controller {
	
	@Autowired
	private RepoAccounts repoAccounts;
	
	@Autowired
	private StorageService storageService;
	
	@PostMapping(value = "/upload")
	public ResponseEntity<V1ApiResponse> upload(@RequestParam(name = "uid", required = true) long uid, 
			@RequestParam(name = "key", required = true) String key,
			@RequestParam(name = "image", required = true) MultipartFile image){
		Optional<ModelAccount> account = repoAccounts.findById(uid);
		
		// If the uid doesn't exist or the key doesn't match the user's key then 
		// return teapot code with error text
		if (account.isEmpty() || !account.get().getUploadKey().equals(key))
			return ResponseEntity.status(418).body(new V1ApiResponse("", "No such uid/key exists", ""));
		
		// Upload file to s3
		String token;
		try {
			token = storageService.store(image, account.get());
		} catch (StorageServiceException e) {
			return ResponseEntity.status(418).body(new V1ApiResponse("", e.getMessage(), ""));
		}
		
		// Get random url from user's preferences, then return
		String domain;
		try {
			domain = account.get().getDomains().get(ThreadLocalRandom.current().nextInt(account.get().getDomains().size()));
		} catch (Exception e) {
			domain = "yiff.best";
		}
		return ResponseEntity.ok().body(new V1ApiResponse("https://" + domain + "/u/" + token, "File uploaded", 
				((domain.startsWith("http://") || domain.startsWith("https://")) ? "" : "https://") + 
				domain + (domain.endsWith("/") ? "" : "/") + "user/panel"));
	}
	
	/**
	 * @author DistastefulBannock
	 * Serialized in json and returned to the client
	 */
	private class V1ApiResponse {

		private final String uploadUrl;
		private final String errorMessage;
		private final String deletionURL;
		
		/**
		 * @param uploadUrl The url that the image could be reached with
		 * @param errorMessage The error message to show if the upload failed
		 * @param deletionURL The url used to delete the image
		 */
		public V1ApiResponse(String uploadUrl, String errorMessage, String deletionURL) {
			this.uploadUrl = uploadUrl;
			this.errorMessage = errorMessage;
			this.deletionURL = deletionURL;
		}
		
		/**
		 * @return the uploadUrl
		 */
		@JsonProperty(value = "UploadUrl")
		public String getUploadUrl() {
			return uploadUrl;
		}
		
		/**
		 * @return the errorMessage
		 */
		@JsonProperty(value = "ErrorMessage")
		public String getErrorMessage() {
			return errorMessage;
		}
		
		/**
		 * @return the deletionURL
		 */
		@JsonProperty(value = "DeletionUrl")
		public String getDeletionURL() {
			return deletionURL;
		}
		
	}
	
}
