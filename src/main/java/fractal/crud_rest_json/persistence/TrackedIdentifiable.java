package fractal.crud_rest_json.persistence;

import java.util.Date;
import java.util.Optional;

public class TrackedIdentifiable implements Identifiable<Long> {

	private Observable<Optional<Long>>	id			= new Observable<>(Optional.empty());

	private Observable<String>			name;

	private final Date					created		= new Date();
	private Optional<Date>				lastUpdated	= Optional.empty();

	public TrackedIdentifiable(String name) {
		this.name = new Observable<>(name);
		Observable.registerAfterAction(() -> lastUpdated = Optional.of(new Date()), id, this.name);
	}

	@Override
	public Optional<Long> getId() {
		return id.get();
	}

	@Override
	public void setId(Long key) {
		id.set(Optional.of(key));
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public Date getCreated() {
		return created;
	}

	public Optional<Date> getLastUpdated() {
		return lastUpdated;
	}

}
