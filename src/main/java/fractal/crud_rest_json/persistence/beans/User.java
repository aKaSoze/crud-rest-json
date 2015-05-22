package fractal.crud_rest_json.persistence.beans;

import java.util.Objects;

public class User extends Person {

	private String	password;

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

}
