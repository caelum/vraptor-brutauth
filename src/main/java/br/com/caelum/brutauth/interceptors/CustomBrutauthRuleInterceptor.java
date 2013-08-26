package br.com.caelum.brutauth.interceptors;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.MethodInvoker;
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
	private final MethodInvoker invoker;
	private final HandlerSearcher handlers;

	public CustomBrutauthRuleInterceptor(Container container, MethodInfo methodInfo,
			MethodInvoker invoker, HandlerSearcher handlers) {
		this.container = container;
		this.methodInfo = methodInfo;
		this.invoker = invoker;
		this.handlers = handlers;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		

		ArrayList<Class<? extends CustomBrutauthRule>> rules = new ArrayList<>();
		
		Class<?> controllerType = method.getResource().getType();
		if (containsAnnotation(controllerType)) {
			CustomBrutauthRules annotation = controllerType.getAnnotation(CustomBrutauthRules.class);
			rules.addAll(Arrays.asList(annotation.value()));
		}
		
		if (method.containsAnnotation(CustomBrutauthRules.class)) {
			CustomBrutauthRules annotation = method.getMethod().getAnnotation(CustomBrutauthRules.class);
			rules.addAll(Arrays.asList(annotation.value()));
		}
		
		for (Class<? extends CustomBrutauthRule> value : rules) {
			CustomBrutauthRule brutauthRule = container.instanceFor(value);
			boolean allowed = invoker.invoke(brutauthRule, methodInfo.getParameters());
			RuleHandler handler = handlers.getHandler(brutauthRule);
			if(!allowed){
				handler.handle();
				return;
			}
			
		}
		stack.next(method, resourceInstance);
	}

	private boolean containsAnnotation(Class<?> type) {
		return type.isAnnotationPresent(CustomBrutauthRules.class);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(CustomBrutauthRules.class) || method.getResource().getType().isAnnotationPresent(CustomBrutauthRules.class);
	}
}