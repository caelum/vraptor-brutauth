package br.com.caelum.brutauth.verifier;

import static br.com.caelum.brutauth.interceptors.MyController.brutauthMethod;
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
import br.com.caelum.brutauth.reflection.Argument;

@RunWith(MockitoJUnitRunner.class)
public class CustomBrutauthRulesVerifierTest {

	@Mock private GenericRulesVerifier genericVerifier;
	@Mock private MethodArguments arguments;
	private CustomBrutauthRulesVerifier customBrutauthRulesVerifier;
	
	
	@Before
	public void setUp() throws Exception {
		customBrutauthRulesVerifier = new CustomBrutauthRulesVerifier(arguments, genericVerifier);
	}
	
	@Test
	public void should_handle_custom_brutauth_rules(){
		assertTrue(customBrutauthRulesVerifier.canVerify(CustomBrutauthRules.class));
		assertFalse(customBrutauthRulesVerifier.canVerify(SimpleBrutauthRules.class));
	}
	
	@Test
	public void should_call_generic_verifier_with_annotation_value(){
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("myCustomRuleMethod");
		when(genericVerifier.verify(any(Class[].class), any(Argument[].class))).thenReturn(true);
		boolean isAllowed = customBrutauthRulesVerifier.rulesOfTypeAllows(brutauthMethod);
		verify(genericVerifier, times(1)).verify(any(Class[].class), any(Argument[].class));
		assertTrue(isAllowed);
	}
	
}
