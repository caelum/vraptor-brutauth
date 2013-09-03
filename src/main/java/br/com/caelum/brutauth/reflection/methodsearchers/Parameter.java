package br.com.caelum.brutauth.reflection.methodsearchers;

import java.util.ArrayList;

public class Parameter {
	
	private final String name;
	private final Class<?> type;

	public Parameter(String name, Class<?> type) {
		this.name = name;
		this.type = type;
	}
	
	public static Parameter[] build(String[] names, Class<?>[] types){
		if(names.length != types.length) throw new IllegalArgumentException("The number of parameter names should be the same number of types");
		ArrayList<Parameter> parameters = new ArrayList<>();
		
		for (int i = 0; i < types.length; i++) {
			parameters.add(new Parameter(names[i], types[i]));
		}
		return parameters.toArray(new Parameter[parameters.size()]);
	}

	public Class<?> getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "[name = "+name+" | type = "+type+"]";
	}

	public String getName() {
		return name;
	}
}
