package br.com.caelum.brutauth.verifier;

import static br.com.caelum.brutauth.util.TestUtils.singleArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
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
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.interceptors.MyController;
import br.com.caelum.brutauth.interceptors.MyCustomRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.MethodInvokers;
import br.com.caelum.vraptor.util.test.MockResult;

@RunWith(MockitoJUnitRunner.class)
public class SingleRuleVerifierTest {
	@Mock private MethodInvokers invokers;
	@Mock private HandlerSearcher handlers;

	private AccessNotAllowedHandler handler;
	private BrutauthRule singleRule = new MyCustomRule();
	private SingleRuleVerifier singleRuleVerifier;

	@Before
	public void setUp(){
		handler = spy(new AccessNotAllowedHandler(new MockResult()));
		singleRuleVerifier = new SingleRuleVerifier(invokers, handlers);

		when(handlers.getHandler(any(CustomBrutauthRule.class))).thenReturn(handler);
		when(invokers.invoke(any(BrutauthRule.class), any(Argument[].class))).then(new Answer<Boolean>() {
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
		assertFalse("should not allow", verifyRules(singleRule, MyController.UNNACCEPTABLE_STRING));
		verify(handler).handle();
	}

	@Test
	public void should_not_invoke_handler_if_allowed() throws Exception {
		assertTrue("should allow", verifyRules(singleRule, MyController.MY_STRING));

		verify(handler, never()).handle();
	}

	private boolean verifyRules(BrutauthRule rule, String argument) {
		return singleRuleVerifier.verify(rule, singleArgument(argument));
	}
}
