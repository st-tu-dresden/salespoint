package dvdshop.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.util.Iterables;

@Entity
public class Disc extends PersistentProduct {
	private String genre;
	private String image;
	
	@ElementCollection
	private List<Comment> comments = new LinkedList<Comment>();
	
	@Deprecated
	protected Disc() {}
	
	public Disc(String name, String image, Money price, String genre) {
		super(name, price, Units.METRIC);
		this.image = image;
		this.genre = genre;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}
	
	public Iterable<Comment> getComments() {
		return Iterables.of(comments);
	}

	public String getImage() {
		return image;
	}
}
