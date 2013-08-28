package br.com.caelum.brutauth.interceptors;

import static br.com.caelum.brutauth.util.TestUtils.method;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.verifier.SimpleBrutauthRulesVerifier;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.resource.ResourceMethod;

@RunWith(MockitoJUnitRunner.class)
public class SimpleBrutauthRuleInterceptorTest {

	@Mock
	private SimpleBrutauthRulesVerifier verifier;
	@Mock
	private InterceptorStack stack;

	private MyController controller;
	private SimpleBrutauthRuleInterceptor interceptor;
	private MySimpleBiggerThanZeroRule simpleRule;
	private AnotherSimpleRule anotherSimpleRule;

	@Before
	public void setUp() throws Exception {
		controller = new MyController();
		interceptor = new SimpleBrutauthRuleInterceptor(verifier);
		simpleRule = spy(new MySimpleBiggerThanZeroRule());
		anotherSimpleRule = spy(new AnotherSimpleRule());
	}
	
	@Test
	public void should_stop_stack_if_rule_says_so() throws Exception {
		ResourceMethod controllerMethod = MyController.method("mySimpleRuleMethod");

		when(verifier.rulesOfTypeAllows(Mockito.any(BrutauthClassOrMethod.class))).thenReturn(false);
		
		assertTrue("should accept mySimpleRuleMethod", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(stack, never()).next(controllerMethod, controller);
	}
	
	@Test
	public void should_continue_stack_if_rule_allows_access() throws Exception {
		ResourceMethod controllerMethod = MyController.method("mySimpleRuleMethodWithAccessLevel");

		when(verifier.rulesOfTypeAllows(Mockito.any(BrutauthClassOrMethod.class))).thenReturn(true);
		
		assertTrue("should accept mySimpleRuleMethodWithAccessLevel", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);

		verify(stack).next(controllerMethod, controller);
	}
	
	@Test
	public void should_add_controllers_class_rules_and_method_rules() throws Exception {
		ResourceMethod controllerMethod = method(ControllerWithRules.class, "methodWithRules");

		when(verifier.rulesOfTypeAllows(Mockito.any(BrutauthClassOrMethod.class))).thenReturn(true);
		
		assertTrue("should accept ControllerWithRules", interceptor.accepts(controllerMethod));
		interceptor.intercept(stack, controllerMethod, controller);
		
		verify(verifier, times(2)).rulesOfTypeAllows(Mockito.any(BrutauthClassOrMethod.class));
	}
}
