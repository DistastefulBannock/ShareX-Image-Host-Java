/**
 * 
 */
package best.yiff.host.service.storage;

/**
 * @author DistastefulBannock
 * An exception that's thrown when something goes wrong while using the account service
 */
public class StorageServiceException extends Exception {
	
	public StorageServiceException(String reason) {
		super(reason);
	}
	
	private static final long serialVersionUID = 9013923586656844970L;
	
}
