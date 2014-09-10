package br.com.caelum.brutauth.interceptors;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.util.RulesMap;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;

@Intercepts
public class BrutauthMapInterceptor implements Interceptor {
	
	@Inject private Instance<BrutauthRule> rules;
	@Inject private Result result;
	@Inject private RulesMap mapaLegal;

	@Override
	public boolean accepts(ControllerMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object resourceInstance) throws InterceptionException {
		for (BrutauthRule rule : rules) {
			Class<? extends BrutauthRule> cls = rule.getClass();
			result.include(cls.getSimpleName(), cls);
			mapaLegal.put(cls, rule);
		}
		result.include("rules", mapaLegal);
		stack.next(method, resourceInstance);
	}

}
