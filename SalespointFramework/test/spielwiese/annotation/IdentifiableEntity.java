package spielwiese.annotation;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: IdentifiableEntitiy
 *
 */
@Entity
public class IdentifiableEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7853412919785226295L;
	@EmbeddedId
	private Identifier id;

	public IdentifiableEntity() {
		super();
		//id = new Identifier();
	}
	
	public Identifier getIdentifier() {
		return id;
	}
	
	public String toString() {
		return id.toString();
	}
   
}
