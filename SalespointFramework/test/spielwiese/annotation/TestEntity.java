package spielwiese.annotation;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TestEntity
 *
 */
@Entity
public class TestEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -418508624407265636L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Embedded
	@AttributeOverride(name="id_", column=@Column(name="NORM_ID"))
	private Identifier ident;
	
	@Embedded
	@AttributeOverride(name="id_", column=@Column(name="SP_ID"))

	private SpecialIdentifier sp_ident;
	
	@Deprecated
	protected TestEntity() {}
	
	public TestEntity(Identifier ident) {
		super();
		System.out.println("Generated " + ident.toString());
		this.ident = ident;
	}

	public TestEntity(SpecialIdentifier ident) {
		super();
		System.out.println("Generated " + ident.toString());
		this.sp_ident = ident;
	}
	
	public Identifier getIdentifier() {
		return ident;
	}
	
	public SpecialIdentifier getSpecialIdentifier() {
		return sp_ident;
	}
	
	public long getId() {
		return id;
	}
}
