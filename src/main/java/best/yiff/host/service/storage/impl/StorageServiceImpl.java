/**
 * 
 */
package best.yiff.host.service.storage.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import best.yiff.host.model.ModelAccount;
import best.yiff.host.model.ModelUpload;
import best.yiff.host.model.ModelUpload.UploadType;
import best.yiff.host.repo.RepoUploads;
import best.yiff.host.service.storage.StorageService;
import best.yiff.host.service.storage.StorageServiceException;

/**
 * @author DistastefulBannock
 *
 */
@Service
public class StorageServiceImpl implements StorageService {
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private RepoUploads repoUploads;
	
	@Value(value = "${s3.bucketName}")
	private String bucketName;
	
	// Allowed mime types
	private static final List<String> IMAGE_MIMES = Arrays.asList("image/png", "image/jpg", "image/jpeg", "image/gif");
	private static final List<String> VIDEO_MIMES = Arrays.asList("video/quicktime", "video/mp4");
	private static final List<String> TEXT_MIMES = Arrays.asList("text/plain");
	private static final List<String> ALLOWED_MIMES = new ArrayList<>();
	static {
		ALLOWED_MIMES.addAll(IMAGE_MIMES);
		ALLOWED_MIMES.addAll(VIDEO_MIMES);
		ALLOWED_MIMES.addAll(TEXT_MIMES);
	}
	
	@Override
	public String store(MultipartFile uploaded, ModelAccount uploader) throws StorageServiceException {
		
		// Make sure the uploader's account isn't banned or disabled
		if (uploader.isBanned() || uploader.isDisabled() || uploader.getUploadKey() == null || uploader.getUploadKey().isBlank()) {
			throw new StorageServiceException("You are banned or your account is disabled");
		}
		
		// Check file mime type to see if it matches the allowed options
		Tika tika = new Tika();
		UploadType uploadType;
		String mime;
		try {
			mime = tika.detect(uploaded.getBytes()).toLowerCase();
			if (mime == null || !ALLOWED_MIMES.contains(mime))
				throw new StorageServiceException("Non supported mime type " + mime);
			
			// Map mime to upload type
			if (IMAGE_MIMES.contains(mime)) {
				uploadType = UploadType.IMAGE;
			}
			else if (VIDEO_MIMES.contains(mime)) {
				uploadType = UploadType.VIDEO;
			}
			else if (TEXT_MIMES.contains(mime)) {
				uploadType = UploadType.TEXT;
			}
			else
				throw new StorageServiceException("Couldn't match mime to a valid upload type");
		} catch (IOException e) {
			throw new StorageServiceException("Error while checking mime");
		}
		
		// Generate a new upload key
		String uploadKey = best.yiff.host.utils.StringUtils.randomEnglishString(4);
		int maxTries = 16, currentTries = 0;
		while (repoUploads.findByKey(uploadKey) != null) {
			if (++currentTries > maxTries)
				throw new StorageServiceException(maxTries + " duplicate image keys generated, this shouldn't happen!");
			uploadKey = best.yiff.host.utils.StringUtils.randomEnglishString(3 + currentTries);
		}
		
		// Upload to s3
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(mime);
		try {
			amazonS3.putObject(bucketName, uploadKey, uploaded.getInputStream(), metadata);
		} catch (SdkClientException | IOException e) {
			throw new StorageServiceException("An error occurred while uploading to aws s3");
		}
		
		// Save upload info in db
		ModelUpload upload = new ModelUpload(uploader.getId(), uploadKey, uploadType);
		repoUploads.saveAndFlush(upload);
		
		// Return upload key
		return uploadKey;
	}

	@Override
	public ModelUpload load(String key) throws StorageServiceException {
		return repoUploads.findByKey(key);
	}

}
