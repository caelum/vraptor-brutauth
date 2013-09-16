package br.com.caelum.brutauth.verifier;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.vraptor.ioc.Container;

public class SimpleBrutauthRulesVerifier implements BrutauthRulesVerifier {
	private final HandlerSearcher handlers;
	private final Container container;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public SimpleBrutauthRulesVerifier() {
		this(null, null);
	}
	
	@Inject
	public SimpleBrutauthRulesVerifier(HandlerSearcher handlers, Container container) {
		this.handlers = handlers;
		this.container = container;
	}

	public boolean rulesOfTypeAllows(BrutauthClassOrMethod type) {
		boolean rulesAllows = true;
		if (type.containsAnnotation(SimpleBrutauthRules.class)) {
			SimpleBrutauthRules annotation = type.getAnnotation(SimpleBrutauthRules.class);
			Class<? extends SimpleBrutauthRule>[] rules = annotation.value();
			long permissionData = 0l;
			if (type.containsAnnotation(AccessLevel.class)) {
				permissionData = type.getAnnotation(AccessLevel.class).value();
			}
			rulesAllows = rulesAllows(rules, permissionData);
		}
		return rulesAllows;
	}

	private boolean rulesAllows(Class<? extends SimpleBrutauthRule>[] rules,
			long permissionData) {
		for (Class<? extends SimpleBrutauthRule> permission : rules) {
			SimpleBrutauthRule rule = container.instanceFor(permission);
			RuleHandler handler = handlers.getHandler(rule);
			if(!rule.isAllowed(permissionData)){
				handler.handle();
				return false;
			}
		}
		return true;
	}	
}
