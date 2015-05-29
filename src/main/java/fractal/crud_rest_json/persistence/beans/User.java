package fractal.crud_rest_json.persistence.beans;

import java.util.Collections;
import java.util.Objects;
import java.util.SortedSet;

public class User extends Person {

	private String				password;

	private SortedSet<Booking>	bookHistory;

	public User(String name, String password) {
		super(name);
		setPassword(password);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = Objects.requireNonNull(password);
	}

	public SortedSet<Booking> getBookHistory() {
		return Collections.unmodifiableSortedSet(bookHistory);
	}

}
