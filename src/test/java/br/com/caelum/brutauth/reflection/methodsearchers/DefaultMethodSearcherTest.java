package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthValidation;
import br.com.caelum.brutauth.reflection.NamedParametersMethod;

public class DefaultMethodSearcherTest {

	private DefaultMethodSearcher methodSearcher;

	@Before
	public void setUp(){
		methodSearcher = new DefaultMethodSearcher();
	}
	
	
	@Test
	public void should_return_is_allowed_method() throws NoSuchMethodException {
		NamedParametersMethod method = methodSearcher.getMethod(new MyCustomRuleWithIsAllowedMethod());
		assertEquals("isAllowed", method.getMethod().getName());
	}
	
	@Test
	public void should_return_annotated_method() throws NoSuchMethodException {
		NamedParametersMethod method = methodSearcher.getMethod(new MyCustomRuleWithAnnotatedMethod());
		assertTrue("should contains @" + BrutauthValidation.class.getSimpleName() + " annnotation", method.getMethod().isAnnotationPresent(BrutauthValidation.class));
	}
	
	@Test(expected = NoSuchMethodException.class)
	public void should_not_return_is_allowed_method_if_it_doesnt_returns_boolean() throws NoSuchMethodException {
		methodSearcher.getMethod(new MyCustomRuleWithInvalidIsAllowedMethod());
	}
	
	@Test(expected = NoSuchMethodException.class)
	public void should_not_return_annotated_method_if_it_doesnt_returns_boolean() throws NoSuchMethodException {
		methodSearcher.getMethod(new MyCustomRuleWithInvalidAnnotatedMethod());
	}
	
	private class MyCustomRuleWithIsAllowedMethod implements CustomBrutauthRule{
		public boolean isAllowed() {
			return false;
		}
	}
	
	private class MyCustomRuleWithInvalidIsAllowedMethod implements CustomBrutauthRule{
		public void isAllowed() {
		}
	}
	
	private class MyCustomRuleWithAnnotatedMethod implements CustomBrutauthRule{
		@BrutauthValidation
		public boolean annotherName() {
			return false;
		}
	}
	
	private class MyCustomRuleWithInvalidAnnotatedMethod implements CustomBrutauthRule{
		@BrutauthValidation
		public void annotherName() {
		}
	}

}
