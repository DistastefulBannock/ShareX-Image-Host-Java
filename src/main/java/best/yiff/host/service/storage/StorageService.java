/**
 * 
 */
package best.yiff.host.service.storage;

import org.springframework.web.multipart.MultipartFile;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.model.ModelUpload;

/**
 * @author DistastefulBannock
 *
 */
public interface StorageService {
	
	/**
	 * Stores the user uploaded file to whatever storage system is used, will also keep track of the data
	 * @param uploaded The file being uploaded
	 * @param uploader The account object for the person who is uploading the content
	 * @return The id used to keep track of the data
	 * @throws StorageServiceException If there is an issue while storing the file
	 */
	public String store(MultipartFile uploaded, ModelAccount uploader) throws StorageServiceException;
	
	/**
	 * Loads the file from whatever storage system is used
	 * @param key The key of the file
	 * @return The upload object, or null if it doesn't exist
	 * @throws StorageServiceException If there is an issue while retrieving the file
	 */
	public ModelUpload load(String key) throws StorageServiceException;
	
}
