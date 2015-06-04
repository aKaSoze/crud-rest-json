package fractal.crud_rest_json.persistence.beans;

import java.util.Objects;

import fractal.crud_rest_json.persistence.IdentifiableBean;

public class Court extends IdentifiableBean {

	private Long	minNumberOfPlayers;
	private Long	maxNumberOfPlayers;

	public Court(String name) {
		super(name);
	}

	public Long getMinNumberOfPlayers() {
		return minNumberOfPlayers;
	}

	public void setMinNumberOfPlayers(Long minNumberOfPlayers) {
		this.minNumberOfPlayers = Objects.requireNonNull(minNumberOfPlayers);
	}

	public Long getMaxNumberOfPlayers() {
		return maxNumberOfPlayers;
	}

	public void setMaxNumberOfPlayers(Long maxNumberOfPlayers) {
		this.maxNumberOfPlayers = Objects.requireNonNull(maxNumberOfPlayers);
	}

}
