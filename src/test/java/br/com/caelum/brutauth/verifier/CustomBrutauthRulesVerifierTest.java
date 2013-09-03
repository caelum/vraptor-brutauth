package br.com.caelum.brutauth.verifier;

import static br.com.caelum.brutauth.util.TestUtils.brutauthMethod;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.vidageek.mirror.dsl.Mirror;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import br.com.caelum.brutauth.auth.handlers.AccessNotAllowedHandler;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.interceptors.AnotherCustomRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.brutauth.interceptors.ControllerWithRules;
import br.com.caelum.brutauth.interceptors.MyController;
import br.com.caelum.brutauth.interceptors.MyCustomRule;
import br.com.caelum.brutauth.parameters.RequestArgumentsParser;
import br.com.caelum.brutauth.reflection.Arguments;
import br.com.caelum.brutauth.reflection.MethodInvoker;
import br.com.caelum.brutauth.reflection.methodsearchers.Argument;
import br.com.caelum.vraptor4.ioc.Container;
import br.com.caelum.vraptor4.util.test.MockResult;

@RunWith(MockitoJUnitRunner.class)
public class CustomBrutauthRulesVerifierTest {

	@Mock
	private Container container;
	@Mock
	private HandlerSearcher handlers;
	@Mock
	private RequestArgumentsParser parser;
	@Mock
	private MethodInvoker invoker;

	private CustomBrutauthRulesVerifier verifier;
	private MyCustomRule customRule;
	private AnotherCustomRule anotherCustomRule;
	private AccessNotAllowedHandler handler;
	private BrutauthClassOrMethod singleRuleControllerMethod;
	private BrutauthClassOrMethod manyRulesControllerMethod;

	@Before
	public void setUp() throws Exception {
		singleRuleControllerMethod = brutauthMethod(MyController.class, "myCustomRuleMethod");
		manyRulesControllerMethod = brutauthMethod(MyController.class, "myManyCustomRulesMethod");
		verifier = new CustomBrutauthRulesVerifier(container, invoker, handlers, parser);
		customRule = spy(new MyCustomRule());
		anotherCustomRule = spy(new AnotherCustomRule());
		handler = spy(new AccessNotAllowedHandler(new MockResult()));

		when(container.instanceFor(MyCustomRule.class)).thenReturn(customRule);
		when(container.instanceFor(AnotherCustomRule.class)).thenReturn(anotherCustomRule);
		when(handlers.getHandler(any(CustomBrutauthRule.class))).thenReturn(handler);
		when(invoker.invoke(any(CustomBrutauthRule.class), any(Argument[].class))).then(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object rule = invocation.getArguments()[0];
				Object[] args = new Arguments((Argument[]) invocation.getArguments()[1]).toValuesOnly();
				return (Boolean) new Mirror().on(rule).invoke().method("isAllowed").withArgs(args);
			}
		});
	}

	@Test
	public void should_invoke_handler_if_not_allowed() throws Exception {
		when(parser.parseArguments()).thenReturn(new Argument[] { new Argument("variable", MyController.UNNACCEPTABLE_STRING) });

		assertFalse("should not allow", verifier.rulesOfTypeAllows(singleRuleControllerMethod));

		verify(handler).handle();
	}

	@Test
	public void should_not_invoke_handler_if_allowed() throws Exception {
		when(parser.parseArguments()).thenReturn(new Argument[] { new Argument("variable", MyController.MY_STRING) });

		assertTrue("should allow", verifier.rulesOfTypeAllows(singleRuleControllerMethod));

		verify(handler, never()).handle();
	}
	
	@Test
	public void should_not_invoke_second_rule_if_first_fails() throws Exception {
		when(parser.parseArguments()).thenReturn(new Argument[] { new Argument("variable", MyController.UNNACCEPTABLE_STRING) });

		assertFalse("should not allow", verifier.rulesOfTypeAllows(manyRulesControllerMethod));

		verify(anotherCustomRule, never()).isAllowed(anyString());
	}
	
	@Test
	public void should_invoke_second_rule_if_first_succeeds() throws Exception {
		when(parser.parseArguments()).thenReturn(new Argument[] { new Argument("variable", MyController.MY_STRING) });

		assertFalse("should allow", verifier.rulesOfTypeAllows(manyRulesControllerMethod));

		verify(customRule).isAllowed(anyString());
		verify(anotherCustomRule).isAllowed(anyString());
	}
	
	@Test
	public void should_add_controllers_class_rules() throws Exception {
		BrutauthClassOrMethod controllerWithRules = new BrutauthClassOrMethod(ControllerWithRules.class);
		
		when(parser.parseArguments()).thenReturn(new Argument[] { new Argument("variable", MyController.MY_STRING) });
		
		assertTrue("should accept ControllerWithRules", verifier.rulesOfTypeAllows(controllerWithRules));
		
		verify(customRule).isAllowed(anyString());
	}
}
