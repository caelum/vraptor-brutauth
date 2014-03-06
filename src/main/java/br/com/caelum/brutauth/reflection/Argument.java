package br.com.caelum.brutauth.reflection;


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

	public boolean matches(Parameter param) {
		return value.getClass().isAssignableFrom(param.getType());
	}

	@Override
	public String toString() {
		return "Argument [name=" + name + ", value=" + value + "]";
	}

}
