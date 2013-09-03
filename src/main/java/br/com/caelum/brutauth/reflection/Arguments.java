package br.com.caelum.brutauth.reflection;

import br.com.caelum.brutauth.reflection.methodsearchers.Argument;

public class Arguments {
	
	private final Argument[] arguments;

	public Arguments(Argument[] arguments) {
		this.arguments = arguments;
	}
	
	public Object[] toValuesOnly() {
		if(arguments == null) return new Object[]{};
		Object[] values = new Object[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			values[i] = arguments[i].getArgument();
		}
		return values;
	}

}
