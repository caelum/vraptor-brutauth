package br.com.caelum.brutauth.verifier;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;

/**
 * Just get values of SimpleBrutauthRules annotation, this exists because we don't have polimorfism for annotations
 * @author Leonardo Wolter
 */

public class CustomBrutauthRulesVerifier implements BrutauthRulesVerifier {

	private final MethodArguments arguments;
	private final GenericRulesVerifier genericVerifier;

	/**
	 * @deprecated CDI eyes only
	 */
	public CustomBrutauthRulesVerifier() {
		this(null, null);
	}
	
	@Inject
	public CustomBrutauthRulesVerifier(MethodArguments arguments, GenericRulesVerifier genericVerifier) {
		this.arguments = arguments;
		this.genericVerifier = genericVerifier;
	}
	
	@Override
	public boolean canVerify(Class<? extends Annotation> annotation) {
		return CustomBrutauthRules.class.isAssignableFrom(annotation);
	}

	@Override
	public boolean rulesOfTypeAllows(BrutauthClassOrMethod type) {
		CustomBrutauthRules annotation = type.getAnnotation(CustomBrutauthRules.class);
		Class<? extends CustomBrutauthRule>[] rules = annotation.value();
		return genericVerifier.verify(rules, arguments.getValuedArguments());
	}


}
