/**
 * 
 */
package best.yiff.host.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import best.yiff.host.model.ModelAccount;

/**
 * @author DistastefulBannock
 *
 */
public interface RepoAccounts extends JpaRepository<ModelAccount, Long> {
	
	/**
	 * Returns a list of all banned accounts
	 * @param banned Whether or not the account is banned
	 * @return A list of all the banned accounts
	 */
	List<ModelAccount> findByBanned(boolean banned);
	
	/**
	 * Returns an account with a specific username
	 * @param name The name of the account
	 * @return An account with the specific username, otherwise null
	 */
	ModelAccount findByUsernameIgnoreCase(String name);
	
	/**
	 * Returns an account with a specific email
	 * @param email The email of the account
	 * @return An account with the specific email, otherwise null
	 */
	ModelAccount findByEmailIgnoreCase(String email);
	
	/**
	 * Returns an account with a specific email and password
	 * @param email The email of the account
	 * @param password The password of the account
	 * @return An account with the specific email and password, otherwise null
	 */
	ModelAccount findByEmailAndPassword(String email, String password);
	
	/**
	 * Returns an account with a specific upload key
	 * @param uploadKey The upload key of the account
	 * @return An account with the specific upload key, otherwise null
	 */
	ModelAccount findByUploadKey(String uploadKey);
	
}
