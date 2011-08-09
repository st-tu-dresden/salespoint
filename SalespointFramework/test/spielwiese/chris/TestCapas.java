package spielwiese.chris;

import java.io.Serializable;

import javax.persistence.Embeddable;


@Embeddable
public class TestCapas implements Serializable{

	
	private static final long serialVersionUID = 31154175801615520L;
	private String name;
	
			
	public TestCapas(){
	
	}
	
	public TestCapas(String name){
		this.name=name;
	}
	
	public String toString(){
		return name;
	}
	
}
