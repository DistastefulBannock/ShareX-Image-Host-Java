/**
 * 
 */
package best.yiff.host.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import best.yiff.host.model.ModelAccount;

/**
 * @author DistastefulBannock
 *
 */
public class AccountUserDetails implements UserDetails {
	
	private final ModelAccount account;

	/**
	 * @param account The account
	 */
	public AccountUserDetails(ModelAccount account) {
		this.account = account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (String role : account.getRoles()) {
			authorities.add(new SimpleGrantedAuthority((role.startsWith("ROLE_") ? "" : "ROLE_") + role));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		return account.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !account.isBanned();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !account.isDisabled();
	}
	
	/**
	 * @return the account
	 */
	public ModelAccount getAccount() {
		return account;
	}
	
}
