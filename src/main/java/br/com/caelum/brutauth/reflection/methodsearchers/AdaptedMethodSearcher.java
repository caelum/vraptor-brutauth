package br.com.caelum.brutauth.reflection.methodsearchers;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.BrutauthMethod;
import br.com.caelum.brutauth.reflection.NamedParametersMethod;
import br.com.caelum.brutauth.reflection.Parameter;

public class AdaptedMethodSearcher implements MethodSearcher {

	private final DefaultMethodSearcher defaultMethodSearcher;
	private final ArgumentParameterMatcher matcher;

	/**
	 * @deprecated CDI eyes only
	 */
	public AdaptedMethodSearcher() {
		this(null, null);
	}
	
	@Inject
	public AdaptedMethodSearcher(DefaultMethodSearcher defaultMethodSearcher, ArgumentParameterMatcher matcher) {
		this.defaultMethodSearcher = defaultMethodSearcher;
		this.matcher = matcher;
	}
	
	@Override
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Argument...arguments) {
		try {
			NamedParametersMethod defaultMethod = defaultMethodSearcher.getMethod(ruleToSearch);
			Parameter[] classes = defaultMethod.getParameters();
			
			Argument[] matchedArguments = matcher.getValuesMatchingParameters(classes, arguments);
			return new BrutauthMethod(matchedArguments, defaultMethod.getMethod(), ruleToSearch);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
}
