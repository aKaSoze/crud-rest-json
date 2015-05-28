package fractal.crud_rest_json.persistence.beans;

import java.util.Date;

import fractal.crud_rest_json.persistence.IdentifiableBean;

public class Person extends IdentifiableBean {

	public Person(String name) {
		super(name);
	}

	private String	firstName;
	private String	lastName;
	private Date	dateOfBirth;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String fristName) {
		this.firstName = fristName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}
