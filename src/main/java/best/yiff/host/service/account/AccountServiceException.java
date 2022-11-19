/**
 * 
 */
package best.yiff.host.service.account;

/**
 * @author DistastefulBannock
 * An exception that's thrown when something goes wrong while using the account service
 */
public class AccountServiceException extends Exception {
	
	public AccountServiceException(String reason) {
		super(reason);
	}
	
	private static final long serialVersionUID = 7719110827600980188L;
	
}
