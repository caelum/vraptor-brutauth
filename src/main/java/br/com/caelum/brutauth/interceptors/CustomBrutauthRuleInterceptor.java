package br.com.caelum.brutauth.interceptors;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.DefaultMethodInvoker;
import br.com.caelum.vraptor4.AroundCall;
import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.core.MethodInfo;
import br.com.caelum.vraptor4.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor4.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor4.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor4.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor4.ioc.Container;
import br.com.caelum.vraptor4.restfulie.controller.ControllerMethod;

@Intercepts(
	before=ExecuteMethodInterceptor.class,
	after=ParametersInstantiatorInterceptor.class
)
@AcceptsWithAnnotations(CustomBrutauthRules.class)
public class CustomBrutauthRuleInterceptor {

	@Inject private Container container;
	@Inject private MethodInfo methodInfo;
	@Inject private DefaultMethodInvoker invoker;
	@Inject private HandlerSearcher handlers;
	@Inject private ControllerMethod method;

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) throws InterceptionException {
		CustomBrutauthRules annotation = method.getMethod().getAnnotation(CustomBrutauthRules.class);
		Class<? extends CustomBrutauthRule>[] values = annotation.value();

		for (Class<? extends CustomBrutauthRule> value : values) {
			CustomBrutauthRule brutauthRule = container.instanceFor(value);
			boolean allowed = invoker.invoke(brutauthRule, methodInfo.getParameters());
			RuleHandler handler = handlers.getHandler(brutauthRule);
			if(!allowed){
				handler.handle();
				return;
			}
		}
		stack.next();
	}
}