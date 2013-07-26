package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.DefaultMethodInvoker;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(before=ExecuteMethodInterceptor.class, after=ParametersInstantiatorInterceptor.class)
public class CustomBrutauthRuleInterceptor implements Interceptor{

	private final Container container;
	private final MethodInfo methodInfo;
	private final DefaultMethodInvoker invoker;
	private final HandlerSearcher handlers;

	public CustomBrutauthRuleInterceptor(Container container, MethodInfo methodInfo,
			DefaultMethodInvoker invoker, HandlerSearcher handlers) {
		this.container = container;
		this.methodInfo = methodInfo;
		this.invoker = invoker;
		this.handlers = handlers;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		CustomBrutauthRules annotation = method.getMethod().getAnnotation(CustomBrutauthRules.class);
		Class<? extends CustomBrutauthRule>[] values = annotation.value();
		
		for (Class<? extends CustomBrutauthRule> value : values) {
			CustomBrutauthRule brutauthRule = container.instanceFor(value);
			boolean allowed = invoker.invoke(brutauthRule, methodInfo.getParameters());
			RuleHandler handler = handlers.getHandler(brutauthRule);
			if(!handler.handle(allowed)){
				return;
			}
		}
		
		
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(CustomBrutauthRules.class);
	}
}