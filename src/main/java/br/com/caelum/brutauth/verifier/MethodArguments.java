package br.com.caelum.brutauth.verifier;

import javax.inject.Inject;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.ValuedParameter;

public class MethodArguments {
	
	private MethodInfo info;

	@Inject
	public MethodArguments(MethodInfo info) {
		this.info = info;
	}

	public Argument[] getValuedArguments() {
		ValuedParameter[] valuedParameters = info.getValuedParameters();
		Argument[] valuedArguments = new Argument[valuedParameters.length];
		for (int i = 0; i < valuedParameters.length; i++) {
			ValuedParameter valuedParameter = valuedParameters[i];
			
			valuedArguments[i] = new Argument(valuedParameter.getName(), valuedParameter.getValue());
		}
		return valuedArguments;
		
	}
	
	
	
}
