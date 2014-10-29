package br.com.caelum.brutauth.reflection;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.methodsearchers.MethodSearchers;

@Dependent
public class CustomBrutauthRuleMethodInvoker implements MethodInvoker {

	@Inject private MethodSearchers searcher;
	
	public boolean invoke(BrutauthRule toInvoke, Argument[] args) {
		return searcher.search((CustomBrutauthRule) toInvoke, args).invoke();
	}

	@Override
	public boolean canInvoke(Class<? extends BrutauthRule> brutauthRule) {
		return CustomBrutauthRule.class.isAssignableFrom(brutauthRule);
	}

}
