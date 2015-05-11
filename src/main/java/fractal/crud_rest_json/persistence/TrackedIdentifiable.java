package fractal.crud_rest_json.persistence;

import java.util.Date;
import java.util.Optional;

public class TrackedIdentifiable implements Identifiable<String> {

	private Optional<String>	id			= Optional.empty();

	private String				name;

	private final Date			created		= new Date();
	private Optional<Date>		lastUpdated	= Optional.empty();

	public TrackedIdentifiable(String name) {
		this.name = name;
	}

	@Override
	public Optional<String> getId() {
		return id;
	}

	@Override
	public void setId(String key) {
		id = Optional.of(key);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public Optional<Date> getLastUpdated() {
		return lastUpdated;
	}

}
