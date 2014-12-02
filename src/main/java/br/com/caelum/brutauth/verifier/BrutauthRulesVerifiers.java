package br.com.caelum.brutauth.verifier;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.IgnoreGlobalRule;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.auth.rules.GlobalRuleProducer;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;

/**
 * Verify every brutauth annotations for a BrutauthClassOrMethod.
 * This is called two times for each request: One for the controller class and other for the controller method.
 * If present, the rule annotated with @GlobalRule will be used at both controller class and methods.
 * @author Leonardo Wolter
 */
public class BrutauthRulesVerifiers {

	private final Instance<BrutauthRulesVerifier> verifiers;
	private final SingleRuleVerifier singleVerifier;
	private GlobalRuleProducer defaultRuleProvider;

	@Inject
	public BrutauthRulesVerifiers(Instance<BrutauthRulesVerifier> verifiers, GlobalRuleProducer globalRuleProvider,
			SingleRuleVerifier singleVerifier) {
		this.verifiers = verifiers;
		this.defaultRuleProvider = globalRuleProvider;
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
		for (BrutauthRulesVerifier verifier: verifiers) {
			for (Annotation annotation : annotations) {
				if(verifier.canVerify(annotation.annotationType())){
					if(!verifier.rulesOfTypeAllows(type)) return false;
				}
			}
		}
		BrutauthRule globalRule = defaultRuleProvider.getInstance();
		boolean userDefinedGlobalRule = globalRule != null;
		boolean shouldIgnoreGlobalRule = type.containsAnnotation(IgnoreGlobalRule.class);
		if(userDefinedGlobalRule && !shouldIgnoreGlobalRule)
			return singleVerifier.verify(globalRule, null);
		return true;
	}
	
}
