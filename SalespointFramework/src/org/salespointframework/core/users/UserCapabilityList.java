package org.salespointframework.core.users;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;


@Entity
public class UserCapabilityList{

	
	@EmbeddedId
	private UserIdentifier id;

	@ElementCollection
	private List<UserCapability> capaList;
	
	@Deprecated
	public UserCapabilityList(){
		
	}
	
	public UserCapabilityList(UserIdentifier id){
		this.id=id;
		capaList= new ArrayList<UserCapability>();
	}
	
	public void addCapa(UserCapability uc){
		capaList.add(uc);
	}
	
	public String toString(){
		String r= id.toString()+": "+ capaList.toString();
		return r;
		
	}

	public boolean contains(UserCapability uc) {
		if(capaList.contains(uc)) return true;
		return false;
	}

	public void remove(UserCapability uc) {
		capaList.remove(uc);		
	}

}
