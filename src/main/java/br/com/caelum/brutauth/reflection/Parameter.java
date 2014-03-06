package br.com.caelum.brutauth.reflection;

public class Parameter {

	private String name;
	private Class<?> type;

	public Parameter(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + name + ", type=" + type + "]";
	}

}
