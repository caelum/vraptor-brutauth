package br.com.caelum.brutauth.reflection.methodsearchers;

public class Argument {
	private final String variableName;
	private final Object argument;

	public Argument(String variableName, Object argument) {
		this.variableName = variableName;
		this.argument = argument;
	}

	public Object getArgument() {
		return argument;
	}

	public String getName() {
		return variableName;
	}
}
