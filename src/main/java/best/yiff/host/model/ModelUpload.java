/**
 * 
 */
package best.yiff.host.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author DistastefulBannock
 *
 */
@Entity
@Table(name = "UPLOADS")
public class ModelUpload {
	
	public ModelUpload() {}
	
	/**
	 * @param uploader The id of the person who uploaded the image
	 * @param key The key used to find the image
	 * @param uploadType The type of upload this is
	 */
	public ModelUpload(long uploader, String key, UploadType uploadType) {
		this.uploader = uploader;
		this.key = key;
		this.type = uploadType;
	}
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	private long id;
	
	@Column(name = "uploader")
	private long uploader;
	
	@Column(name = "uploadKey", unique = true)
	private String key;
	
	@Column(name = "type")
	private UploadType type;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the uploader
	 */
	public long getUploader() {
		return uploader;
	}
	
	/**
	 * @param uploader the uploader to set
	 */
	public void setUploader(long uploader) {
		this.uploader = uploader;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * @return the type
	 */
	public UploadType getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(UploadType type) {
		this.type = type;
	}
	
	/**
	 * @author DistastefulBannock
	 *
	 */
	public static enum UploadType{
		IMAGE,
		VIDEO,
		TEXT
	}
	
}
