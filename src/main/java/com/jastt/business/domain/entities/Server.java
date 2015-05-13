package com.jastt.business.domain.entities;


public class Server extends PersistentEntity<Integer>{
		
	private static final long serialVersionUID = 2097826735449036033L;
	private String url;
	
	public Server() {
		
	}
		
	public Server(String url) {
		super();
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Server other = (Server) obj;
		if (url == null) {
			if (other.getUrl() != null)
				return false;
		} else if (!url.equals(other.getUrl()))
			return false;
		return true;
	}		
	
	@Override
	public String toString() {
		return url;
	}
}