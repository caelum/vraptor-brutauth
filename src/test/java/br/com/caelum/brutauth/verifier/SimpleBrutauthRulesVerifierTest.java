package br.com.caelum.brutauth.verifier;

import static org.junit.Assert.assertFalse;
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
import br.com.caelum.brutauth.interceptors.AnotherSimpleRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.brutauth.interceptors.ControllerWithRules;
import br.com.caelum.brutauth.interceptors.MyController;
import br.com.caelum.brutauth.interceptors.MySimpleBiggerThanZeroRule;
import br.com.caelum.vraptor4.ioc.Container;
import br.com.caelum.vraptor4.util.test.MockResult;

@RunWith(MockitoJUnitRunner.class)
public class SimpleBrutauthRulesVerifierTest {
	@Mock
	private Container container;
	@Mock
	private HandlerSearcher handlers;
	private SimpleBrutauthRulesVerifier verifier;
	private MySimpleBiggerThanZeroRule simpleRule;
	private AccessNotAllowedHandler handler;
	private AnotherSimpleRule anotherSimpleRule;

	@Before
	public void setUp() throws Exception {
		verifier = new SimpleBrutauthRulesVerifier(handlers, container);
		simpleRule = spy(new MySimpleBiggerThanZeroRule());
		anotherSimpleRule = spy(new AnotherSimpleRule());
		handler = spy(new AccessNotAllowedHandler(new MockResult()));

		when(container.instanceFor(MySimpleBiggerThanZeroRule.class)).thenReturn(simpleRule);
		when(container.instanceFor(AnotherSimpleRule.class)).thenReturn(anotherSimpleRule);
		when(handlers.getHandler(any(SimpleBrutauthRule.class))).thenReturn(handler);
	}

	@Test
	public void should_invoke_handler_if_not_allowed() throws Exception {
		BrutauthClassOrMethod controllerMethod = MyController.brutauthMethod("mySimpleRuleMethod");

		assertFalse("should not allow", verifier.rulesOfTypeAllows(controllerMethod));

		verify(handler).handle();
	}
	
	@Test
	public void should_not_invoke_handler_if_allowed() throws Exception {
		BrutauthClassOrMethod controllerMethod = MyController.brutauthMethod("mySimpleRuleMethodWithAccessLevel");

		assertTrue("should allow", verifier.rulesOfTypeAllows(controllerMethod));

		verify(handler, never()).handle();
	}
	
	@Test
	public void should_not_invoke_second_rule_if_first_fails() throws Exception {
		BrutauthClassOrMethod controllerMethod = MyController.brutauthMethod("myManySimpleRulesMethod");

		assertFalse("should not allow", verifier.rulesOfTypeAllows(controllerMethod));

		verify(anotherSimpleRule, never()).isAllowed(anyLong());
	}
	
	@Test
	public void should_invoke_second_rule_if_first_succeeds() throws Exception {
		BrutauthClassOrMethod controllerMethod = MyController.brutauthMethod("myManySimpleRulesMethodWithAccessLevel");

		assertTrue("should allow", verifier.rulesOfTypeAllows(controllerMethod));

		verify(anotherSimpleRule).isAllowed(anyLong());
	}

	@Test
	public void should_add_controllers_class_rules_with_correct_access_level() throws Exception {
		BrutauthClassOrMethod controllerMethod = new BrutauthClassOrMethod(ControllerWithRules.class);

		assertTrue("should allow", verifier.rulesOfTypeAllows(controllerMethod));
		
		verify(simpleRule).isAllowed(ControllerWithRules.ACCESS_LEVEL);
	}

}
