package br.com.caelum.brutauth.reflection;

import javax.enterprise.context.Dependent;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;

@Dependent
public class SimpleBrutauthRuleMethodInvoker implements MethodInvoker {

	public boolean invoke(BrutauthRule toInvoke, Argument[] args) {
		Argument argument = args[0];
		Long accessLevel = (Long) argument.getValue();
		return ((SimpleBrutauthRule) toInvoke).isAllowed(accessLevel);
	}

	@Override
	public boolean canInvoke(Class<? extends BrutauthRule> brutauthRule) {
		return SimpleBrutauthRule.class.isAssignableFrom(brutauthRule);
	}

}
