/**
 * 
 */
package best.yiff.host.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.repo.RepoAccounts;

/**
 * @author DistastefulBannock
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private RepoAccounts repoAccounts;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ModelAccount account = repoAccounts.findByEmailIgnoreCase(username); // Load by email because fuck you
		if (account == null)
			throw new UsernameNotFoundException(username + " does not exist");
		return new AccountUserDetails(account);
	}
	
}
