package br.com.caelum.brutauth.verifier;

import static br.com.caelum.brutauth.interceptors.MyController.brutauthMethod;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.enterprise.inject.Instance;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.auth.rules.GlobalRuleProducer;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;

@RunWith(MockitoJUnitRunner.class)
public class BrutauthRulesVerifiersTest {
	@Mock private Instance<BrutauthRulesVerifier> verifiersInstances;
	@Mock private GlobalRuleProducer defaultRuleProducer;
	@Mock private BrutauthRule defaultRule;
	@Mock private SingleRuleVerifier singleVerifier;
	@Mock private BrutauthRulesVerifier verifier;
	private BrutauthRulesVerifiers verifiers;
	
	@Before
	public void setUp(){
		when(verifier.canVerify(CustomBrutauthRules.class)).thenReturn(true);
		when(verifier.canVerify(SimpleBrutauthRules.class)).thenReturn(true);
		when(verifiersInstances.iterator()).thenReturn(Arrays.asList(verifier).iterator());
		when(defaultRuleProducer.getInstance()).thenReturn(defaultRule);
		verifiers = new BrutauthRulesVerifiers(verifiersInstances, defaultRuleProducer, singleVerifier);
	}
	
	@Test
	public void should_handle_custom_brutauth_rule() {
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("myCustomRuleMethod");
		verifiers.verify(brutauthMethod);
		
		verify(verifier, times(1)).rulesOfTypeAllows(brutauthMethod);
		verify(singleVerifier, never()).verify(defaultRule, null);
	}
	
	@Test
	public void should_handle_custom_brutauth_rule_that_returns_true_and_fail_at_default_rule() {
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("myCustomRuleMethod");
		
		when(verifier.rulesOfTypeAllows(brutauthMethod)).thenReturn(true);
		when(singleVerifier.verify(defaultRule, null)).thenReturn(false);
		
		assertFalse(verifiers.verify(brutauthMethod));
		verify(verifier, times(1)).rulesOfTypeAllows(brutauthMethod);
		verify(singleVerifier).verify(defaultRule, null);
	}

	@Test
	public void should_handle_custom_brutauth_rule_that_returns_true_and_succeed_at_default_rule() {
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("myCustomRuleMethod");
		
		when(verifier.rulesOfTypeAllows(brutauthMethod)).thenReturn(true);
		when(singleVerifier.verify(defaultRule, null)).thenReturn(true);
		
		assertTrue(verifiers.verify(brutauthMethod));
		verify(verifier, times(1)).rulesOfTypeAllows(brutauthMethod);
		verify(singleVerifier).verify(defaultRule, null);
	}
		
	@Test
	public void should_handle_simple_brutauth_rule() {
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("mySimpleRuleMethod");
		verifiers.verify(brutauthMethod);
		
		verify(verifier, times(1)).rulesOfTypeAllows(brutauthMethod);
		verify(singleVerifier, never()).verify(defaultRule, null);
	}
	
	@Test
	public void should_use_default_rule_if_method_has_no_annotations() {
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("myNonAnnotatedMethod");
		verifiers.verify(brutauthMethod);
		
		verify(verifier, never()).rulesOfTypeAllows(brutauthMethod);
		verify(singleVerifier, times(1)).verify(defaultRule, null);
	}

	@Test
	public void should_use_default_rule_if_no_annotations_can_be_handled() {
		BrutauthClassOrMethod brutauthMethod = brutauthMethod("myNonAnnotatedMethod");
		verifiers.verify(brutauthMethod);
		
		verify(verifier, never()).rulesOfTypeAllows(brutauthMethod);
		verify(singleVerifier, times(1)).verify(defaultRule, null);
	}
	

}

