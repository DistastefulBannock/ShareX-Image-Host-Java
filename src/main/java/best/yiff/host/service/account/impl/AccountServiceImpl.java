/**
 * 
 */
package best.yiff.host.service.account.impl;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.model.ModelInvite;
import best.yiff.host.repo.RepoAccounts;
import best.yiff.host.repo.RepoInvites;
import best.yiff.host.service.account.AccountService;
import best.yiff.host.service.account.AccountServiceException;

/**
 * @author DistastefulBannock
 *
 */
@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private RepoAccounts repoAccounts;
	
	@Autowired
	private RepoInvites repoInvites;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value(value = "${api.uploadKeySize}")
	private int uploadKeySize;
	
	@Override
	public ModelAccount registerAccount(String username, String email, String password, String invite)
			throws AccountServiceException {
		
		// First we do a number of checks for the input
		if (username.isBlank() || email.isBlank() || password.isBlank() || invite.isBlank())
			throw new AccountServiceException("Please fill out all of the fields");
		if (StringUtils.containsWhitespace(username))
			throw new AccountServiceException("Invalid username, contains a space");
		if (StringUtils.containsWhitespace(email))
			throw new AccountServiceException("Invalid email, contains a space");
		if (username.length() > 18)
			throw new AccountServiceException("Username is greater than 18 characters");
		if (email.length() > 200)
			throw new AccountServiceException("Email is greater than 200 characters");
		
		// Then thing to do is check that the invite is valid
		ModelInvite modelInvite = repoInvites.findByInvite(invite);
		if (modelInvite == null)
			throw new AccountServiceException("Invite is invalid");
		
		// Next we check if the invite is already redeemed
		if (modelInvite.isRedeemed()) {
			Optional<ModelAccount> redeemer = repoAccounts.findById(modelInvite.getRedeemer());
			throw new AccountServiceException("Invite has already been redeemed" + (redeemer.isPresent() ? " by " + redeemer.get().getUsername() : ""));
		}
		
		// Now we check to make sure the email isn't registered
		ModelAccount modelAccount = repoAccounts.findByEmailIgnoreCase(email);
		if (modelAccount != null)
			throw new AccountServiceException("Email is already registered");
		
		// Finally we check to make sure the username isn't already taken
		modelAccount = repoAccounts.findByUsernameIgnoreCase(username);
		if (modelAccount != null)
			throw new AccountServiceException("Username taken");
		
		// And now that all the checks have been passed we're able to register the account and redeem the invite
		ModelAccount newAccount = new ModelAccount(username, email, passwordEncoder.encode(password), false);
		newAccount.setInvitedBy(modelInvite.getOwner());
		repoAccounts.saveAndFlush(newAccount);
		modelInvite.setRedeemed(true);
		modelInvite.setRedeemer(newAccount.getId());
		repoInvites.saveAndFlush(modelInvite);
		
		return newAccount;
	}
	
	@Override
	public void regenerateUploadKey(ModelAccount modelAccount) throws AccountServiceException {
		
		// Check if the account is banned or disabled, upload keys cannot be created for banned or disabled accounts
		if (modelAccount.isBanned())
			throw new AccountServiceException("Cannot regenerate an upload key for a banned account");
		if (modelAccount.isDisabled())
			throw new AccountServiceException("Cannot regenerate an upload key for a disabled account");
		
		// Create a new key, check to make sure it doesn't already exist
		String newUploadKey = best.yiff.host.utils.StringUtils.randomEnglishString(uploadKeySize);
		int maxTries = 10, currentTries = 0;
		while (repoAccounts.findByUploadKey(newUploadKey) != null) {
			if (++currentTries > maxTries)
				throw new AccountServiceException("Duplicate upload key generated, this shouldn't happen!");
			newUploadKey = best.yiff.host.utils.StringUtils.randomEnglishString(uploadKeySize);
		}
		
		// Save new upload key to the specific account
		modelAccount.setUploadKey(newUploadKey);
		repoAccounts.saveAndFlush(modelAccount);
		
	}
	
}
