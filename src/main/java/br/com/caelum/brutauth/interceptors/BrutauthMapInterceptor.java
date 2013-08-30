package br.com.caelum.brutauth.interceptors;

import java.util.List;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.util.RulesMap;
import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Result;
import br.com.caelum.vraptor4.controller.ControllerMethod;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.interceptor.Interceptor;

@Intercepts
public class BrutauthMapInterceptor implements Interceptor {
	
	private List<BrutauthRule> rules;
	private Result result;
	private RulesMap mapaLegal;

	public BrutauthMapInterceptor(List<BrutauthRule> rules, Result result, RulesMap mapaLegal) {
		this.rules = rules;
		this.result = result;
		this.mapaLegal = mapaLegal;
	}
	
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
