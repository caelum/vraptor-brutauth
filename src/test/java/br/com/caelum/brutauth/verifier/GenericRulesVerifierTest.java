package br.com.caelum.brutauth.verifier;

import static br.com.caelum.brutauth.util.TestUtils.brutauthMethod;
import static br.com.caelum.brutauth.util.TestUtils.customRulesFor;
import static br.com.caelum.brutauth.util.TestUtils.singleArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.interceptors.AnotherCustomRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.brutauth.interceptors.ControllerWithRules;
import br.com.caelum.brutauth.interceptors.MyController;
import br.com.caelum.brutauth.interceptors.MyCustomRule;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.ioc.Container;

@RunWith(MockitoJUnitRunner.class)
public class GenericRulesVerifierTest {
	
	@Mock private Container container;
	@Mock private MethodInfo methodInfo;
	@Mock private SingleRuleVerifier singleRuleVerifier;
	
	private MyCustomRule customRule;
	private AnotherCustomRule anotherCustomRule;
	private Class<? extends BrutauthRule>[] manyRulesControllerMethod;
	private GenericRulesVerifier genericRulesVerifier;

	@Before
	public void setUp(){
		customRule = spy(new MyCustomRule());
		anotherCustomRule = spy(new AnotherCustomRule());

		manyRulesControllerMethod = customRulesFor(brutauthMethod(MyController.class, "myManyCustomRulesMethod"));

		when(container.instanceFor(MyCustomRule.class)).thenReturn(customRule);
		when(container.instanceFor(AnotherCustomRule.class)).thenReturn(anotherCustomRule);
		when(singleRuleVerifier.verify(customRule, singleArgument(MyController.UNNACCEPTABLE_STRING))).thenReturn(false);
		when(singleRuleVerifier.verify(customRule, singleArgument(MyController.MY_STRING))).thenReturn(true);

		genericRulesVerifier = new GenericRulesVerifier(container, singleRuleVerifier);

	}
	
	@Test
	public void should_not_invoke_second_rule_if_first_fails() throws Exception {
		assertFalse("should not allow", verifyRules(manyRulesControllerMethod, MyController.UNNACCEPTABLE_STRING));

		verify(singleRuleVerifier, never()).verify(anotherCustomRule, singleArgument(MyController.UNNACCEPTABLE_STRING));
	}
	
	@Test
	public void should_invoke_second_rule_if_first_succeeds() throws Exception {
		assertFalse("should allow", verifyRules(manyRulesControllerMethod, MyController.MY_STRING));
		
		verify(singleRuleVerifier).verify(customRule, singleArgument(MyController.MY_STRING));
		verify(singleRuleVerifier).verify(anotherCustomRule, singleArgument(MyController.MY_STRING));
	}

	@Test
	public void should_add_controllers_class_rules() throws Exception {
		BrutauthClassOrMethod controllerWithRules = new BrutauthClassOrMethod(ControllerWithRules.class);
		
		assertTrue("should accept ControllerWithRules", verifyRules(customRulesFor(controllerWithRules), MyController.MY_STRING));
		
		verify(singleRuleVerifier).verify(customRule, singleArgument(MyController.MY_STRING));
	}
	
	private boolean verifyRules(Class<? extends BrutauthRule>[] rule, String argument) {
		return genericRulesVerifier.verify(rule, singleArgument(argument));
	}

}
