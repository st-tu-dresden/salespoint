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

	public SpecialIdentifier() {
		super();
	}
   
}
