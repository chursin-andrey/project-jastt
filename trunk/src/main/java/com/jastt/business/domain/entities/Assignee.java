package com.jastt.business.domain.entities;

import java.util.Comparator;


public class Assignee extends PersistentEntity<Integer>{
	
	private static final long serialVersionUID = -6761832289105928380L;
	private String name;
	private String email;
	
	public Assignee(){	
	
	}
	
	public Assignee(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assignee other = (Assignee) obj;
		if (email == null) {
			if (other.getEmail() != null)
				return false;
		} else if (!email.equals(other.getEmail()))
			return false;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return name + " ["+ email +"]";
	}		

	public static Comparator<Assignee> getAssigneeNameComparator() {
		return new AssigneeNameComparator();
	}

	private static final class AssigneeNameComparator implements Comparator<Assignee> {
		@Override
		public int compare(Assignee a1, Assignee a2) {
			if (a1 == a2) {
				return 0;
			}
			if (a1 == null || a1.getName() == null) {
				return -1;
			}
			if (a2 == null || a2.getName() == null) {
				return 1;
			}
			return a1.getName().compareTo(a2.getName());
		}
	}
}

