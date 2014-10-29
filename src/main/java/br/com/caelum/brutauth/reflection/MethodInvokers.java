package br.com.caelum.brutauth.reflection;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;

/**
 * Knows how to invoke every kind of BrutauthRules
 * @author Leonardo Wolter
 *
 */
@Dependent
public class MethodInvokers {

	private Instance<MethodInvoker> invokers;

	@Inject
	public MethodInvokers(Instance<MethodInvoker> invokers) {
		this.invokers = invokers;
	}
	
	public boolean invoke(BrutauthRule brutauthRule, Argument[] arguments) {
		for (MethodInvoker methodInvoker : invokers) {
			if(methodInvoker.canInvoke(brutauthRule.getClass()))
				return methodInvoker.invoke(brutauthRule, arguments);
		}
		return false;
	}

}
