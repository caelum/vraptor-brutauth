package br.com.caelum.brutauth.reflection;


import javax.enterprise.inject.Vetoed;

import br.com.caelum.vraptor.http.Parameter;

@Vetoed
public class Argument {

	private String name;
	private Object value;

	public Argument(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public MatchLevel matches(Parameter param) {
		boolean typesAreEquals = value.getClass().isAssignableFrom(param.getType());
		boolean namesAreEquals = name.equals(param.getName());
		
		if(typesAreEquals) {
			if(namesAreEquals) return MatchLevel.FULL;
			return MatchLevel.HALF;
		}
		
		return MatchLevel.ZERO;
	}

	@Override
	public String toString() {
		return "Argument [name=" + name + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Argument other = (Argument) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
