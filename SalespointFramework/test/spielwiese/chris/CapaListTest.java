package spielwiese.chris;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CapaListTest {
	
	
	@Id
	private String id;
	
	@ElementCollection
	private Set<TestCapas> capalist;
	
	
	public CapaListTest(){
		
	}
	
	public CapaListTest(String id){
		capalist= new HashSet<TestCapas>();
		this.id=id;
	}
	
	public void addCapa(TestCapas tc){
		capalist.add(tc);
	}
	
	public String toString(){
		return id;
		
	}
	
}
