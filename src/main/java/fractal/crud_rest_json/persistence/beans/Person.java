package fractal.crud_rest_json.persistence.beans;

import java.util.Date;

import fractal.crud_rest_json.persistence.IdentifiableBean;

public class Person extends IdentifiableBean {

	public Person(String name) {
		super(name);
	}

	private String	fristName;
	private String	lastName;
	private Date	dateOfBirth;

	public String getFristName() {
		return fristName;
	}

	public void setFristName(String fristName) {
		this.fristName = fristName;
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
