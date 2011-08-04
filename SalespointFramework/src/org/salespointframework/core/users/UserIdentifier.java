package org.salespointframework.core.users;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.util.SalespointIdentifier;

/**
 * Entity implementation class for Entity: UserIdentifier
 *
 * @author hannesweisbach
 */
@Embeddable

public final class UserIdentifier extends SalespointIdentifier implements Serializable {

	
	private static final long serialVersionUID = 1L;


	public UserIdentifier(){
		super();
	}
	
	public UserIdentifier(String userId) {
		super(userId);
	}
   
}
