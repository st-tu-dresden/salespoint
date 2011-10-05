package dvdshop.model;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;
import org.salespointframework.core.shop.Shop;

@Embeddable
public class Comment {

	private String text;
	private int rating;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date date;
	
	
	@Deprecated
	protected Comment() { }
	
	public Comment(String text, int rating) {
		this.text = text;
		this.rating = rating;
		this.date = Shop.INSTANCE.getTime().getDateTime().toDate();
	}
	
	public String getText() {
		return text;
	}

	public DateTime getDate() {
		return new DateTime(date);
	}

	public int getRating() {
		return rating;
	}
}
