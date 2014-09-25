package br.com.caelum.brutauth.verifier;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.DefaultRule;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;

/**
 * Verify every brutauth annotations for a BrutauthClassOrMethod.
 * This is called two times for each request: One for the controller class and other for the controller method.
 * If no annotations of BrutauthClassOrMethod can be verified, the rule annotated with @DefaultRule will be used.
 * @author Leonardo Wolter
 */
public class BrutauthRulesVerifiers {

	private final Instance<BrutauthRulesVerifier> verifiers;
	private final BrutauthRule defaultRule;
	private SingleRuleVerifier singleVerifier;

	@Inject
	public BrutauthRulesVerifiers(Instance<BrutauthRulesVerifier> verifiers, @DefaultRule BrutauthRule defaultRule,
			SingleRuleVerifier singleVerifier) {
		this.verifiers = verifiers;
		this.defaultRule = defaultRule;
		this.singleVerifier = singleVerifier;
	}

	/**
	 * @deprecated CDI eyes only
	 */
	protected BrutauthRulesVerifiers() {
		this(null, null, null);
	}
	
	public boolean verify(BrutauthClassOrMethod type) {
		List<Annotation> annotations = type.getAnnotations();
		boolean wasVerified = false;
		boolean isAllowed = true;
		for (BrutauthRulesVerifier verifier: verifiers) {
			for (Annotation annotation : annotations) {
				if(verifier.canVerify(annotation.annotationType())){
					wasVerified = true;
					isAllowed = verifier.rulesOfTypeAllows(type) && isAllowed;
				}
			}
		}
		if(!wasVerified) return singleVerifier.verify(defaultRule, null);
		return isAllowed;
	}
	
}
