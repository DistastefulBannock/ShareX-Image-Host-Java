/**
 * 
 */
package best.yiff.host.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import best.yiff.host.model.ModelUpload;

/**
 * @author DistastefulBannock
 *
 */
public interface RepoUploads extends JpaRepository<ModelUpload, Long> {
	
	/**
	 * Finds an upload via the unique key
	 * @param key The key of the upload
	 * @return The upload object if one with the key exists, otherwise null
	 */
	ModelUpload findByKey(String key);
	
	/**
	 * Counts how many uploads a user has
	 * @param uploader The uid of the uploader
	 * @return How many uploads they have
	 */
	int countByUploader(long uploader);
	
}
