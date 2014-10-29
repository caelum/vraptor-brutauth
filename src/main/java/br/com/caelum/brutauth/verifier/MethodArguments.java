package br.com.caelum.brutauth.verifier;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.ValuedParameter;

@Dependent
public class MethodArguments {
	
	private MethodInfo info;

	@Inject
	public MethodArguments(MethodInfo info) {
		this.info = info;
	}

	public Argument[] getValuedArguments() {
		ValuedParameter[] valuedParameters = info.getValuedParameters();
		List<Argument> valuedArguments = new ArrayList<Argument>();
		for (int i = 0; i < valuedParameters.length; i++) {
			ValuedParameter valuedParameter = valuedParameters[i];
			boolean validParameter = valuedParameter != null && valuedParameter.getValue() != null;
			if(validParameter) {
				Argument argument = new Argument(valuedParameter.getName(), valuedParameter.getValue());
				valuedArguments.add(argument);
			}
		}
		return valuedArguments.toArray(new Argument[] {});
		
	}
	
	
	
}
