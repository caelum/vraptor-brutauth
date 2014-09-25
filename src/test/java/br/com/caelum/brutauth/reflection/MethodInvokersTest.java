package br.com.caelum.brutauth.reflection;

import static br.com.caelum.brutauth.util.TestUtils.singleArgument;
import static br.com.caelum.brutauth.verifier.SimpleBrutauthRulesVerifier.ACCESS_LEVEL_ARG_NAME;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Iterator;

import javax.enterprise.inject.Instance;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.interceptors.MyCustomRule;
import br.com.caelum.brutauth.verifier.SimpleBrutauthRulesVerifier;

@RunWith(MockitoJUnitRunner.class)
public class MethodInvokersTest {

	@Mock private Instance<MethodInvoker> cdiInvokers;
	@Mock private MethodInvoker customInvoker;
	@Mock private MethodInvoker simpleInvoker;
	private MethodInvokers methodInvokers;
	
	@Before
	public void setUp(){
		Iterator<MethodInvoker> trueInvokers = Arrays.asList(customInvoker, simpleInvoker).iterator();
		when(cdiInvokers.iterator()).thenReturn(trueInvokers);
		methodInvokers = new MethodInvokers(cdiInvokers);
	}
	
	@Test
	public void should_stop_on_first_invocation() {
		MyCustomRule brutauthRule = new MyCustomRule();
		Argument[] arguments = singleArgument("");
		
		when(customInvoker.canInvoke(any(Class.class))).thenReturn(true);
		when(simpleInvoker.canInvoke(any(Class.class))).thenReturn(false);

		methodInvokers.invoke(brutauthRule, arguments);
		
		verify(customInvoker).canInvoke(brutauthRule.getClass());
		verify(customInvoker).invoke(brutauthRule, arguments);
		verify(simpleInvoker, never()).canInvoke(brutauthRule.getClass());
		verify(simpleInvoker, never()).invoke(brutauthRule, arguments);
	}

	@Test
	public void should_not_stop_on_first_invocation() {
		MyCustomRule brutauthRule = new MyCustomRule();
		Argument[] arguments = singleArgument("");
		
		when(customInvoker.canInvoke(any(Class.class))).thenReturn(false);
		when(simpleInvoker.canInvoke(any(Class.class))).thenReturn(true);
		
		methodInvokers.invoke(brutauthRule, arguments);
		
		verify(customInvoker).canInvoke(brutauthRule.getClass());
		verify(customInvoker, never()).invoke(brutauthRule, arguments);
		verify(simpleInvoker).canInvoke(brutauthRule.getClass());
		verify(simpleInvoker).invoke(brutauthRule, arguments);
	}

}
