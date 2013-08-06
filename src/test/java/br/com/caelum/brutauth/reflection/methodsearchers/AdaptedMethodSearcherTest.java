package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public class AdaptedMethodSearcherTest {

	private DefaultMethodSearcher methodSearcher = new DefaultMethodSearcher();
	private AdaptedMethodSearcher adaptedMethodSearcher;
	
	@Before
	public void setUp(){
		adaptedMethodSearcher = new AdaptedMethodSearcher(methodSearcher);
	}
	
	
	@Test
	public void should_match_default_method_with_correct_parameters() {
		BrutauthMethod method = adaptedMethodSearcher.search(new AnimalsRule(), new Cat(), new Dog());
		
		assertMatchedMethod(method);
	}

	@Test
	public void should_match_default_method_with_one_correct_parameter() {
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), new Cat(), new Dog());
		
		assertMatchedMethod(method);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void should_not_allow_null_parameters() {
		adaptedMethodSearcher.search(new AnimalsRule(), new Cat());
	}
	
	@Test
	public void should_match_methods_without_parameters() {
		BrutauthMethod method = adaptedMethodSearcher.search(new NoneRule(), new Cat(), new Cat());
		
		assertMatchedMethod(method);
	}


	private void assertMatchedMethod(BrutauthMethod method) {
		assertNotNull(method);
		assertEquals("isAllowed", method.getMethod().getName());
	}
	
	
	
	private class AnimalsRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(Cat cat, Dog dog){
			return false;
		}
	}
	
	private class CatRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(Cat cat){
			return false;
		}
	}
	
	private class NoneRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(){
			return false;
		}
	}
	
	private class Cat{
	}
	
	private class Dog{
	}

}
