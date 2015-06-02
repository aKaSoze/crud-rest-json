package fractal.crud_rest_json.persistence.beans;

import java.util.Objects;
import java.util.Set;

import fractal.crud_rest_json.persistence.IdentifiableBean;

public class Arena extends IdentifiableBean {

	private Set<Court>	courts;

	public Arena(String name) {
		super(name);
	}

	public Set<Court> getCourts() {
		return courts;
	}

	public void setCourts(Set<Court> courts) {
		this.courts = Objects.requireNonNull(courts);
	}

}
