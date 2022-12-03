package best.yiff.host.controller;

import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.repo.RepoAccounts;
import best.yiff.host.service.storage.StorageService;

@Controller
@RequestMapping(value = "/api/v1")
public class Test1Controller {
	
	@Autowired
	private RepoAccounts repoAccounts;
	
	@Autowired
	private StorageService storageService;
	
	@PostMapping(value = "/test")
	public ResponseEntity<V1ApiResponse> test() {
		return ResponseEntity.ok(new V1ApiResponse("test", "test", "test"));
	}
	
	@PostMapping(value = "/upload")
	public ResponseEntity<V1ApiResponse> upload(@RequestParam(name = "uid", required = true) long uid, 
			@RequestParam(name = "key", required = true) String key,
			@RequestParam(name = "image", required = true) MultipartFile image){
		Optional<ModelAccount> account = repoAccounts.findById(uid);
		
		// If the uid doesn't exist or the key doesn't match the user's key then 
		// return teapot code with error text
		if (account.isEmpty() || !account.get().getUploadKey().equals(key))
			return ResponseEntity.status(418).body(new V1ApiResponse("", "No such uid/key exists", ""));
		
		// Take file and upload to s3
		Tika tika = new Tika();
		try {
			String mime = tika.detect(image.getBytes());
			System.out.println(mime);
		} catch (Exception e) {
			
		}
		return ResponseEntity.ok().build();
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
		
		public String getUploadUrl() {
			return uploadUrl;
		}
		
		public String getErrorMessage() {
			return errorMessage;
		}
		
		public String getDeletionURL() {
			return deletionURL;
		}

	}
	
}
