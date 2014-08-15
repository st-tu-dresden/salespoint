package spielwiese.annotation;

import java.io.Serializable;
import javax.persistence.*;
import spielwiese.annotation.Identifier;

/**
 * Entity implementation class for Entity: SpecialIdentifier
 *
 */
@Embeddable
public class SpecialIdentifier extends Identifier implements Serializable {
	//private int foobar;
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("javadoc")
	public SpecialIdentifier() {
		super();
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) { 
		return super.equals(other);
	}
	
	
   
}
