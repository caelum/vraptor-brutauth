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
	
	public Argument[] getValuesMatchingParameters(Parameter[] params, Argument[] args) throws NoSuchMethodException {
		Argument[] argsToUse = new Argument[params.length];
		for (int i = 0; i < params.length; i++) {
			argsToUse[i] = searchForArgumentMatching(params[i], args);
		}
		return argsToUse;
	}

	private Argument searchForArgumentMatching(Parameter param, Argument[] args) {
		for (Argument arg : args) {
			if(arg != null && !matchedParameters.alreadyMatchedBy(arg)) 
				matchedParameters.tryToPut(param, arg);
		}
		MatchedArgument matchedArgument = matchedParameters.get(param);
		if(matchedArgument == null) return null;
		return matchedArgument.getArgument();
	}
}
