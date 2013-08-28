package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.verifier.CustomBrutauthRulesVerifier;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(before=ExecuteMethodInterceptor.class, after=ParametersInstantiatorInterceptor.class)
public class CustomBrutauthRuleInterceptor implements Interceptor{

	private final CustomBrutauthRulesVerifier verifier;

	public CustomBrutauthRuleInterceptor(CustomBrutauthRulesVerifier verifier) {
		this.verifier = verifier;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		
		BrutauthClassOrMethod controllerType = new BrutauthClassOrMethod(method.getResource().getType());
		if(!verifier.rulesOfTypeAllows(controllerType)) return;
		
		BrutauthClassOrMethod methodtype = new BrutauthClassOrMethod(method.getMethod());
		if(!verifier.rulesOfTypeAllows(methodtype)) return;
		
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(CustomBrutauthRules.class) || method.getResource().getType().isAnnotationPresent(CustomBrutauthRules.class);
	}
}