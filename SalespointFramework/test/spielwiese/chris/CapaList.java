package spielwiese.chris;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import jaxax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import test.user.MyEmployeeManager;

@Entity
public class CapaList implements Serializable{
	
	
	private static final long serialVersionUID = 3499085046761873815L;



	@Id
	private String id;
	
	
	
	@ManyToMany
    @JoinTable(
    name="CUST_PHONE",
    joinColumns=
        @JoinColumn(name="CUST_ID", referencedColumnName="ID"),
    inverseJoinColumns=
        @JoinColumn(name="PHONE_ID", referencedColumnName="ID")
    )
	private ArrayList<TestCapas> capalist= new ArrayList<TestCapas>();
	
//    public ArrayList<TestCapas> getPhones() {
//		return  capalist1;
//	}
	
	
	public CapaList(){
		
	}
	
	public CapaList(String id){
		this.id=id;
	}
	
	public void addCapa(TestCapas tc){
		capalist.add(tc);
	}
	
	public String toString(){
		return id;
		
	}
	
}
