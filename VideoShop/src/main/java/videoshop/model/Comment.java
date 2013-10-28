package videoshop.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

// (｡◕‿◕｡)
// Eigene Entity Klasse um Kommentare für Discs zu speichern
// Alle JPA Anforderungen erfüllt :)
// Mit der Table-Annotation kann man u.a. den Name der Tabelle angeben, ansonsten wird der Klassennamen genommen

@Entity
@Table(name="COMMENTS")
public class Comment {

	// (｡◕‿◕｡)
	// Falls man die Id nicht selber setzen will, kann die mit @GeneratedValue vom JPA-Provider generiert und gesetzt werden
	@Id
	@GeneratedValue
	private long id;
	
	private String text;
	private int rating;
	
	// (｡◕‿◕｡)
	// 1. Es gibt eine extra Annotation für Dates
	// 2. JPA kann nicht mit Joda DateTimes umgehen, deswegen in einem normalen Date halten, aber immer als DateTime rausgeben (siehe getter)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date date;
	
	@Deprecated
	protected Comment() { }
	
	public Comment(String text, int rating, DateTime dateTime) {
		this.text = text;
		this.rating = rating;
		this.date = dateTime.toDate();
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}

	// (｡◕‿◕｡)
	// java.util.Date ist Schrott, deswegwen in ein DateTime wrappen und das rausgeben
	public DateTime getDate() {
		return new DateTime(date);
	}

	public int getRating() {
		return rating;
	}
}
