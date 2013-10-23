package videoshop.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

@Entity
@Table(name="COMMENTS")
public class Comment {

	@Id
	@GeneratedValue
	private long id;
	
	private String text;
	private int rating;
	
	//@Temporal(value = TemporalType.TIMESTAMP)
	//private Date date;
	
	@Deprecated
	protected Comment() { }
	
	public Comment(String text, int rating) {
		this.text = text;
		this.rating = rating;
	}
	
	public String getText() {
		return text;
	}
	
	@Override
	public String toString() {
		return text;
	}

	//public DateTime getDate() {
	//	return new DateTime(date);
	//}

	public int getRating() {
		return rating;
	}
}
