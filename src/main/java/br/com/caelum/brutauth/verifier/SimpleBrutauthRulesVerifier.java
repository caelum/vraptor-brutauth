package br.com.caelum.brutauth.verifier;

import java.lang.annotation.Annotation;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.brutauth.reflection.Argument;

/**
 * Just get values of SimpleBrutauthRules annotation, this exists because we don't have polimorfism for annotations
 * @author Leonardo Wolter
 */
public class SimpleBrutauthRulesVerifier implements BrutauthRulesVerifier {
	public static final String ACCESS_LEVEL_ARG_NAME = "accessLevel";
	private final GenericRulesVerifier genericVerifier;
	
	/**
	 * @deprecated CDI eyes only
	 */
	protected SimpleBrutauthRulesVerifier() {
		this(null);
	}
	
	@Inject
	public SimpleBrutauthRulesVerifier(GenericRulesVerifier genericVerifier) {
		this.genericVerifier = genericVerifier;
	}

	public boolean rulesOfTypeAllows(BrutauthClassOrMethod type) {
		SimpleBrutauthRules annotation = type.getAnnotation(SimpleBrutauthRules.class);
		Class<? extends BrutauthRule>[] rules = annotation.value();
		long permissionData = 0l;
		if (type.containsAnnotation(AccessLevel.class)) {
			permissionData = type.getAnnotation(AccessLevel.class).value();
		}
		return genericVerifier.verify(rules, new Argument[]{new Argument(ACCESS_LEVEL_ARG_NAME, permissionData)});
	}


	@Override
	public boolean canVerify(Class<? extends Annotation> annotation) {
		return SimpleBrutauthRules.class.isAssignableFrom(annotation);
	}	
}
