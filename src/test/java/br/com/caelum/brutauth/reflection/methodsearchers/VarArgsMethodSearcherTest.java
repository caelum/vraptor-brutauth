package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class VarArgsMethodSearcherTest {

	@Test
	public void should_not_match_methods_without_arguments() {
		VarArgsMethodSearcher varArgsMethodSearcher = new VarArgsMethodSearcher(new DefaultMethodSearcher());
		assertNull(varArgsMethodSearcher.search(new FakeRuleWithoutArguments(), null));
	}

	public class FakeRuleWithoutArguments implements CustomBrutauthRule{
		public boolean isAllowed(){
			return true;
		}
	}
}
