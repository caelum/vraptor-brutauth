package br.com.caelum.brutauth.verifier;

import static br.com.caelum.brutauth.interceptors.ControllerWithRules.ACCESS_LEVEL;
import static br.com.caelum.brutauth.interceptors.MyController.brutauthMethod;
import static br.com.caelum.brutauth.util.TestUtils.simpleRulesFor;
import static br.com.caelum.brutauth.util.TestUtils.singleArgument;
import static br.com.caelum.brutauth.verifier.SimpleBrutauthRulesVerifier.ACCESS_LEVEL_ARG_NAME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.brutauth.interceptors.ControllerWithRules;
import br.com.caelum.brutauth.reflection.Argument;

@RunWith(MockitoJUnitRunner.class)
public class SimpleBrutauthRulesVerifierTest {
	@Mock private GenericRulesVerifier genericVerifier;
	@Mock private MethodArguments arguments;
	private SimpleBrutauthRulesVerifier simpleBrutauthRulesVerifier;
	
	
	@Before
	public void setUp() throws Exception {
		simpleBrutauthRulesVerifier = new SimpleBrutauthRulesVerifier(genericVerifier);
	}
	
	@Test
	public void should_handle_simple_brutauth_rules(){
		assertTrue(simpleBrutauthRulesVerifier.canVerify(SimpleBrutauthRules.class));
		assertFalse(simpleBrutauthRulesVerifier.canVerify(CustomBrutauthRules.class));
	}
	
	@Test
	public void should_call_generic_verifier_with_annotation_value(){
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("mySimpleRuleMethod");
		when(genericVerifier.verify(any(Class[].class), any(Argument[].class))).thenReturn(true);
		boolean isAllowed = simpleBrutauthRulesVerifier.rulesOfTypeAllows(brutauthMethod);
		verify(genericVerifier, times(1)).verify(any(Class[].class), any(Argument[].class));
		assertTrue(isAllowed);
	}
	
	@Test
	public void should_add_controllers_class_rules_with_correct_access_level() throws Exception {
		BrutauthClassOrMethod controllerMethod = new BrutauthClassOrMethod(ControllerWithRules.class);
		when(genericVerifier.verify(any(Class[].class), any(Argument[].class))).thenReturn(true);
		assertTrue("should allow", simpleBrutauthRulesVerifier.rulesOfTypeAllows(controllerMethod));
		verify(genericVerifier, times(1)).verify(simpleRulesFor(controllerMethod), singleArgument(ACCESS_LEVEL_ARG_NAME, ACCESS_LEVEL));
	}

}
