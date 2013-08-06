package br.com.caelum.brutauth.interceptors;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.auth.handlers.AccessNotAllowedHandler;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.test.MockResult;

@RunWith(MockitoJUnitRunner.class)
public class SimpleBrutauthRuleInterceptorTest {

	@Mock
	private Container container;
	@Mock
	private HandlerSearcher handlers;
	@Mock
	private InterceptorStack stack;

	private MyController controller;
	private SimpleBrutauthRuleInterceptor interceptor;
	private MySimpleBiggerThanZeroRule simpleRule;
	private AccessNotAllowedHandler handler;
	private AnotherSimpleRule anotherSimpleRule;

	@Before
	public void setUp() throws Exception {
		controller = new MyController();
		interceptor = new SimpleBrutauthRuleInterceptor(container, handlers);
		simpleRule = new MySimpleBiggerThanZeroRule();
		anotherSimpleRule = spy(new AnotherSimpleRule());
		handler = spy(new AccessNotAllowedHandler(new MockResult()));

		when(container.instanceFor(MySimpleBiggerThanZeroRule.class)).thenReturn(simpleRule);
		when(container.instanceFor(AnotherSimpleRule.class)).thenReturn(anotherSimpleRule);
		when(handlers.getHandler(any(SimpleBrutauthRule.class))).thenReturn(handler);
	}
	@Test
	public void should_stop_stack_if_rule_says_so() throws Exception {
		ResourceMethod controllerMethod = MyController.method("mySimpleRuleMethod");

		assertTrue("should accept mySimpleRuleMethod", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(stack, never()).next(controllerMethod, controller);
	}
	@Test
	public void should_continue_stack_if_rule_allows_access() throws Exception {
		ResourceMethod controllerMethod = MyController.method("mySimpleRuleMethodWithAccessLevel");

		assertTrue("should accept mySimpleRuleMethodWithAccessLevel", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(stack).next(controllerMethod, controller);
	}
	@Test
	public void should_invoke_handler_if_not_allowed() throws Exception {
		ResourceMethod controllerMethod = MyController.method("mySimpleRuleMethod");

		assertTrue("should accept mySimpleRuleMethod", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(handler).handle();
	}
	@Test
	public void should_not_invoke_handler_if_allowed() throws Exception {
		ResourceMethod controllerMethod = MyController.method("mySimpleRuleMethodWithAccessLevel");

		assertTrue("should accept mySimpleRuleMethodWithAccessLevel", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(handler, never()).handle();
	}
	@Test
	public void should_not_invoke_second_rule_if_first_fails() throws Exception {
		ResourceMethod controllerMethod = MyController.method("myManySimpleRulesMethod");
		assertTrue("should accept myManySimpleRulesMethod", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(anotherSimpleRule, never()).isAllowed(anyLong());
	}
	@Test
	public void should_invoke_second_rule_if_first_succeeds() throws Exception {
		ResourceMethod controllerMethod = MyController.method("myManySimpleRulesMethodWithAccessLevel");
		assertTrue("should accept myManySimpleRulesMethodWithAccessLevel", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(anotherSimpleRule).isAllowed(anyLong());
	}
}
