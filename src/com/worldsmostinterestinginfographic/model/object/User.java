package com.worldsmostinterestinginfographic.model.object;

import java.io.Serializable;

/**
 * This class represents a user of our application. It will contain any
 * important information that we would like to display in our infographic. All
 * of this data will be provided by the service provider, in our case, Facebook.
 */
public final class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private String name;
	private String link;
	private String gender;

	public User(String firstName, String lastName, String name, String link,
			String gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = name;
		this.link = link;
		this.gender = gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}

	public String getGender() {
		return gender;
	}
}
