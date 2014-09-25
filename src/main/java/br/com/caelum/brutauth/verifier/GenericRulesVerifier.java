package br.com.caelum.brutauth.verifier;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.vraptor.ioc.Container;

/**
 * Verifies if one of the rules of the annotated type returns false
 * 
 * @author Leonardo Wolter
 *
 */
public class GenericRulesVerifier {

	private Container container;
	private SingleRuleVerifier verifier;

	@Inject
	public GenericRulesVerifier(Container container, SingleRuleVerifier verifier) {
				this.container = container;
				this.verifier = verifier;
	}
	
	public boolean verify(Class<? extends BrutauthRule>[] rules, Argument[] arguments) {
		for (Class<? extends BrutauthRule> rule : rules) {
			BrutauthRule brutauthRule = container.instanceFor(rule);
			if(!verifier.verify(brutauthRule, arguments)) return false;
		}
		return true;
	}

}
