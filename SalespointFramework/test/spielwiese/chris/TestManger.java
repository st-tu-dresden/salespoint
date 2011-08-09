package spielwiese.chris;

import java.util.HashMap;

import javax.persistence.ElementCollection;


public class TestManger {
	
		
	
	private HashMap<String, CapaListTest> m= new HashMap<String, CapaListTest>();
	
	public TestManger(){
		
	}
	
	public void addCapa(String name, TestCapas tc){
		CapaListTest cl= new CapaListTest();
		cl.addCapa(tc);
		m.put(name, cl);
	}

}
