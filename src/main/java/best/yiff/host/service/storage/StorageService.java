/**
 * 
 */
package best.yiff.host.service.storage;

import org.springframework.web.multipart.MultipartFile;

import best.yiff.host.model.ModelAccount;

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
	 * @param id The id of the file
	 * @return The url of where the file is reachable
	 * @throws StorageServiceException If there is an issue while retrieving the file
	 */
	public String load(String id) throws StorageServiceException;
	
}
