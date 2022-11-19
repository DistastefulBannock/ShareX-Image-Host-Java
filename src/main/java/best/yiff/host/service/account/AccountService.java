/**
 * 
 */
package best.yiff.host.service.account;

import best.yiff.host.model.ModelAccount;

/**
 * @author DistastefulBannock
 *
 */
public interface AccountService {
	
	/**
	 * Registers a new account into the database
	 * @param username The username for the account
	 * @param email The email for the account
	 * @param password The password for the account
	 * @param invite The invite code to use when registering the account
	 * @return The fresh account object
	 * @throws AccountServiceException If something goes wrong while registering the account
	 */
	public abstract ModelAccount registerAccount(String username, String email, String password, String invite) throws AccountServiceException;
	
	/**
	 * Regenerate the upload key for an account
	 * @param modelAccount The account to generate the upload key for
	 * @throws AccountServiceException If something goes wrong while regenerating the upload key
	 */
	public abstract void regenerateUploadKey(ModelAccount modelAccount) throws AccountServiceException;
	
}
