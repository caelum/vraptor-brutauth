package br.com.caelum.brutauth.verifier;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.brutauth.reflection.MethodInvoker;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.ioc.Container;

public class CustomBrutauthRulesVerifier implements BrutauthRulesVerifier {

	private final Container container;
	private final MethodInfo methodInfo;
	private final MethodInvoker invoker;
	private final HandlerSearcher handlers;

	/**
	 * @deprecated CDI eyes only
	 */
	public CustomBrutauthRulesVerifier() {
		this(null, null, null, null);
	}
	
	@Inject
	public CustomBrutauthRulesVerifier(Container container, MethodInfo methodInfo,
			MethodInvoker invoker, HandlerSearcher handlers) {
				this.container = container;
				this.methodInfo = methodInfo;
				this.invoker = invoker;
				this.handlers = handlers;
	}
	
	@Override
	public boolean rulesOfTypeAllows(BrutauthClassOrMethod type) {
		boolean rulesAllows = true;
		if (type.containsAnnotation(CustomBrutauthRules.class)) {
			CustomBrutauthRules annotation = type.getAnnotation(CustomBrutauthRules.class);
			Class<? extends CustomBrutauthRule>[] rules = annotation.value();
			rulesAllows = rulesAllows(rules);
		}
		return rulesAllows;
		
	}

	private boolean rulesAllows(Class<? extends CustomBrutauthRule>[] rules) {
		for (Class<? extends CustomBrutauthRule> rule : rules) {
			CustomBrutauthRule brutauthRule = container.instanceFor(rule);
			boolean allowed = invoker.invoke(brutauthRule, methodInfo.getParametersValues());
			RuleHandler handler = handlers.getHandler(brutauthRule);
			if(!allowed){
				handler.handle();
				return false;
			}
		}
		return true;
	}

}
