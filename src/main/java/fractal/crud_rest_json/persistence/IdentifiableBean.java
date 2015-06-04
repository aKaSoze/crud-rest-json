package fractal.crud_rest_json.persistence;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class IdentifiableBean implements Identifiable<UUID> {

	private Optional<UUID>	id			= Optional.empty();

	private String			name;

	private Date			created		= new Date();
	private Optional<Date>	lastUpdated	= Optional.empty();

	public IdentifiableBean(String name) {
		setName(name);
	}

	@Override
	public Optional<UUID> getId() {
		return id;
	}

	@Override
	public void setId(UUID key) {
		id = Optional.of(key);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = Objects.requireNonNull(name);
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(Date created) {
		this.created = Objects.requireNonNull(created);
	}

	@Override
	public Optional<Date> getLastUpdated() {
		return lastUpdated;
	}

	@Override
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = Optional.of(lastUpdated);
	}

}
