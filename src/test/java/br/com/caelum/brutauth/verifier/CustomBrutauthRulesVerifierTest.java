package br.com.caelum.brutauth.verifier;

import static br.com.caelum.brutauth.util.TestUtils.brutauthMethod;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
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
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.MethodInvoker;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.Parameter;
import br.com.caelum.vraptor.http.ValuedParameter;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.util.test.MockResult;

@RunWith(MockitoJUnitRunner.class)
public class CustomBrutauthRulesVerifierTest {

	@Mock
	private Container container;
	@Mock
	private HandlerSearcher handlers;
	@Mock
	private MethodInfo methodInfo;
	@Mock
	private MethodInvoker invoker;

	private CustomBrutauthRulesVerifier verifier;
	private MyCustomRule customRule;
	private AnotherCustomRule anotherCustomRule;
	private AccessNotAllowedHandler handler;
	private BrutauthClassOrMethod singleRuleControllerMethod;
	private BrutauthClassOrMethod manyRulesControllerMethod;
	private Parameter defaultParameter;

	@Before
	public void setUp() throws Exception {
		singleRuleControllerMethod = brutauthMethod(MyController.class, "myCustomRuleMethod");
		manyRulesControllerMethod = brutauthMethod(MyController.class, "myManyCustomRulesMethod");
		
		
		MethodArguments methodArguments = new MethodArguments(methodInfo);
		verifier = new CustomBrutauthRulesVerifier(container, methodArguments , invoker, handlers);
		
		customRule = spy(new MyCustomRule());
		anotherCustomRule = spy(new AnotherCustomRule());
		handler = spy(new AccessNotAllowedHandler(new MockResult()));


		defaultParameter = mock(Parameter.class);
		when(defaultParameter.getName()).thenReturn("test");
		
		when(container.instanceFor(MyCustomRule.class)).thenReturn(customRule);
		when(container.instanceFor(AnotherCustomRule.class)).thenReturn(anotherCustomRule);
		when(handlers.getHandler(any(CustomBrutauthRule.class))).thenReturn(handler);
		when(invoker.invoke(any(CustomBrutauthRule.class), any(Argument[].class))).then(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object rule = invocation.getArguments()[0];
				Object[] args = toValues((Argument[]) invocation.getArguments()[1]);
				
				
				
				return (Boolean) new Mirror().on(rule).invoke().method("isAllowed").withArgs(args);
			}

			private Object[] toValues(Argument[] arguments) {
				Object[] values = new Object[arguments.length];
				for (int i = 0; i < arguments.length; i++) {
					Argument argument = arguments[i];
					values[i] = argument.getValue();
				}
				return values;
			}
		});
	}

	@Test
	public void should_invoke_handler_if_not_allowed() throws Exception {
		when(methodInfo.getValuedParameters()).thenReturn(parameters(MyController.UNNACCEPTABLE_STRING));

		assertFalse("should not allow", verifier.rulesOfTypeAllows(singleRuleControllerMethod));

		verify(handler).handle();
	}

	@Test
	public void should_not_invoke_handler_if_allowed() throws Exception {
		when(methodInfo.getValuedParameters()).thenReturn(parameters(MyController.MY_STRING));

		assertTrue("should allow", verifier.rulesOfTypeAllows(singleRuleControllerMethod));

		verify(handler, never()).handle();
	}
	
	@Test
	public void should_not_invoke_second_rule_if_first_fails() throws Exception {
		when(methodInfo.getValuedParameters()).thenReturn(parameters( MyController.UNNACCEPTABLE_STRING ));

		assertFalse("should not allow", verifier.rulesOfTypeAllows(manyRulesControllerMethod));

		verify(anotherCustomRule, never()).isAllowed(anyString());
	}
	
	@Test
	public void should_invoke_second_rule_if_first_succeeds() throws Exception {
		when(methodInfo.getValuedParameters()).thenReturn(parameters(MyController.MY_STRING));

		assertFalse("should allow", verifier.rulesOfTypeAllows(manyRulesControllerMethod));

		verify(customRule).isAllowed(anyString());
		verify(anotherCustomRule).isAllowed(anyString());
	}

	@Test
	public void should_add_controllers_class_rules() throws Exception {
		BrutauthClassOrMethod controllerWithRules = new BrutauthClassOrMethod(ControllerWithRules.class);
		
		when(methodInfo.getValuedParameters()).thenReturn(parameters(MyController.MY_STRING));
		
		assertTrue("should accept ControllerWithRules", verifier.rulesOfTypeAllows(controllerWithRules));
		
		verify(customRule).isAllowed(anyString());
	}

	private ValuedParameter[] parameters(String string) {
		return new ValuedParameter[] {new ValuedParameter(defaultParameter, string)};
	}
	
	

	
}
