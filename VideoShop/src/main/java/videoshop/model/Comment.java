package videoshop.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

@Embeddable
public class Comment {

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
