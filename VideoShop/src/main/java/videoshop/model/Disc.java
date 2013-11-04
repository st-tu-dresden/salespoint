package videoshop.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.salespointframework.core.catalog.Product;
import org.salespointframework.core.money.Money;
import org.salespointframework.core.quantity.Units;
import org.salespointframework.util.Iterables;

//(｡◕‿◕｡)
// Da der Shop DVD sowie BluRay verkaufen soll ist es sinnvoll eine gemeinsame Basisklasse zu erstellen.
// Diese erbt von Product um die Catalog-Klasse aus Salespoint nutzen zu können.
// Ein Primärschlüssel ist nicht notwendig, da dieser schon in Product definiert ist, alle anderen JPA-Anforderungen müssen aber erfüllt werden
@Entity
public class Disc extends Product {
	
	// (｡◕‿◕｡)
	// primitve Typen oder Strings müssen nicht extra für JPA annotiert werden
	private String genre;
	private String image;
	
	// (｡◕‿◕｡)
	// Jede Disc besitzt mehrere Kommentare, eine "1 zu n"-Beziehung -> @OneToMany für JPA
	// cascade gibt an, was mit den Kindelementen (Comment) passieren soll wenn das Parentelement (Disc) mit der Datenbank "interagiert"
	@OneToMany(cascade=CascadeType.ALL)
	private List<Comment> comments = new LinkedList<Comment>();

	// (｡◕‿◕｡)
	// Ein paremterloser public oder protected Konstruktor ist zwingend notwendig für JPA,
	// damit dieser nicht genutzt wird, markieren wir in mit @Deprecated
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
	
	// (｡◕‿◕｡)
	// Es ist immer sinnvoll sich zu überlegen wie speziell der Rückgabetyp sein sollte
	// Da sowies nur über die Kommentare iteriert wird, ist ein Iterable<T> das sinnvollste.
	// Weil wir keine Liste zurück geben, verhindern wir auch, dass jemand die comments-Liste einfach durch clear() leert.
	// Deswegen geben auch so viele Salespoint Klassen nur Iterable<T> zurück ;)
	public Iterable<Comment> getComments() {
		return Iterables.of(comments);
	}

	public String getImage() {
		return image;
	}
}
