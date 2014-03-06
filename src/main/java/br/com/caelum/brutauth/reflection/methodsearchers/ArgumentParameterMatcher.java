package br.com.caelum.brutauth.reflection.methodsearchers;

import javax.inject.Inject;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.Parameter;

public class ArgumentParameterMatcher {
	
	private MatchedParameters matchedParameters;

	@Inject
	public ArgumentParameterMatcher(MatchedParameters matchedParameters) {
		this.matchedParameters = matchedParameters;
	}
	
	public Object[] getValuesMatchingParameters(Parameter[] params, Argument[] args) throws NoSuchMethodException {
		Object[] argsToUse = new Object[params.length];
		for (int i = 0; i < params.length; i++) {
			Object parameter = searchForArgumentMatching(params[i], args);
			argsToUse[i] = parameter;
		}
		return argsToUse;
	}

	private Object searchForArgumentMatching(Parameter param, Argument[] args) {
		for (Argument arg : args) {
			if(arg != null && !matchedParameters.alreadyMatchedBy(arg)) 
				matchedParameters.tryToPut(param, arg);
		}
		MatchedArgument matchedArgument = matchedParameters.get(param);
		if(matchedArgument == null)
			throw new IllegalArgumentException("I didn't find any argument matching the parameter "+ param+ ". Arguments: "+args);
		return matchedArgument.getArgument().getValue();
	}
}
