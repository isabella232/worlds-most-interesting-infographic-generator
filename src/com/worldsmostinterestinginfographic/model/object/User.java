package com.worldsmostinterestinginfographic.model.object;

import java.io.Serializable;

/**
 * This class represents a user of our application. It will contain any
 * important information that we would like to display in our infographic. All
 * of this data will be provided by the service provider, in our case, Facebook.
 */
public final class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String firstName;
	private String lastName;
	private String name;
	private String link;
	private String gender;

	public User(long id, String firstName, String lastName, String name, String link,
			String gender) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.name = name;
		this.link = link;
		this.gender = gender;
	}
	
	public User(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof User)) {
			return false;
		}
		
		return id == ((User)obj).getId();
	}
	
	@Override
	public int hashCode() {
		System.out.println((int)id);
		return (int)id;
	}

	public long getId() {
		return id;
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
