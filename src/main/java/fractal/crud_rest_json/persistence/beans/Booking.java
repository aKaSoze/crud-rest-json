package fractal.crud_rest_json.persistence.beans;

import java.util.Date;
import java.util.Objects;

import fractal.crud_rest_json.persistence.IdentifiableBean;

public class Booking extends IdentifiableBean {

	private User	user;

	private Date	startTime;
	private Date	endTime;

	public Booking(String name, User user, Date startTime, Date endTime) {
		super(name);
		this.startTime = Objects.requireNonNull(startTime);
		this.endTime = Objects.requireNonNull(endTime);
		setUser(user);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = Objects.requireNonNull(user);
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = Objects.requireNonNull(startTime);
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = Objects.requireNonNull(endTime);
	}

}
