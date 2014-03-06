package br.com.caelum.brutauth.reflection.methodsearchers;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.MatchLevel;
import br.com.caelum.brutauth.reflection.Parameter;

public class MatchedParameters {
	private final Map<Parameter, MatchedArgument> matches = new HashMap<>();

	public void tryToPut(Parameter param, Argument arg) {
		MatchLevel matchLevel = arg.matches(param);
		
		if(MatchLevel.ZERO.equals(matchLevel)) return;
		
		MatchedArgument matchedArgument = matches.get(param);
		boolean hasAFullMatch = matchedArgument != null && matchedArgument.isFull();
		if(hasAFullMatch) return;

		matches.put(param, new MatchedArgument(arg, matchLevel));
	}
	
	public MatchedArgument get(Parameter parameter) {
		return matches.get(parameter);
	}

	public boolean alreadyMatchedBy(Argument arg) {
		return matches.containsValue(new MatchedArgument(arg, null));
	}
	
}
