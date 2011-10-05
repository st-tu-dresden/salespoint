package dvdshop.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProductType;
import org.salespointframework.util.Iterables;

@Entity
public class Disc extends PersistentProductType {
	private String genre;
	
	@ElementCollection
	private List<Comment> comments = new LinkedList<Comment>();
	
	@Deprecated
	public Disc() {
		
	}
	
	public Disc(String name, Money price, String genre) {
		super(name, price);
		this.genre = genre;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}
	
	public Iterable<Comment> getComments() {
		return Iterables.from(comments);
	}
}
