package spielwiese.chris;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CapaListTest {
	
	
	@Id
	private String id;
	
	@ElementCollection
	private List<TestCapas> capalist;
	
	
	public CapaListTest(){
		
	}
	
	public CapaListTest(String id){
		capalist= new ArrayList<TestCapas>();
		this.id=id;
	}
	
	public void addCapa(TestCapas tc){
		capalist.add(tc);
	}
	
	public String toString(){
		return id;
		
	}
	
}
