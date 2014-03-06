package br.com.caelum.brutauth.reflection.methodsearchers;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.NamedParametersMethod;
import br.com.caelum.brutauth.reflection.BrutauthMethod;
import br.com.caelum.brutauth.reflection.Parameter;

public class AdaptedMethodSearcher implements MethodSearcher {

	private DefaultMethodSearcher defaultMethodSearcher;

	/**
	 * @deprecated CDI eyes only
	 */
	public AdaptedMethodSearcher() {
		this(null);
	}
	
	@Inject
	public AdaptedMethodSearcher(DefaultMethodSearcher defaultMethodSearcher) {
		this.defaultMethodSearcher = defaultMethodSearcher;
	}
	
	@Override
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Argument...arguments) {
		try {
			NamedParametersMethod defaultMethod = defaultMethodSearcher.getMethod(ruleToSearch);
			Parameter[] classes = defaultMethod.getParameters();
			Object[] argumentValues = getValuesMatchingParameters(classes, arguments);
			return new BrutauthMethod(argumentValues, defaultMethod.getMethod(), ruleToSearch);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private Object[] getValuesMatchingParameters(Parameter[] params, Argument[] args) throws NoSuchMethodException {
		Object[] argsToUse = new Object[params.length];
		for (int i = 0; i < params.length; i++) {
			Object parameter = searchForArgumentMatching(params[i], args);
			argsToUse[i] = parameter;
		}
		return argsToUse;
	}

	private Object searchForArgumentMatching(Parameter param, Argument[] args) {
		for (Argument arg : args) {
			if (arg.matches(param)) return arg.getValue();
		}
		throw new IllegalArgumentException("I didn't find any argument matching the parameter "+ param+ ". Arguments: "+args);
	}
}
