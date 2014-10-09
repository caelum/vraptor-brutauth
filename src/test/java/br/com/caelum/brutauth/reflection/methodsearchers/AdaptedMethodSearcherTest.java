package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import br.com.caelum.vraptor.http.Parameter;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.BrutauthMethod;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AdaptedMethodSearcherTest {

    @Mock private ParameterNameProvider parameterNameProvider;
    @Mock private ArgumentParameterMatcher argumentParameterMatcher;
	private DefaultMethodSearcher methodSearcher ;
	private AdaptedMethodSearcher adaptedMethodSearcher;
	private Argument dog;
	private Argument cat;

	@Before
	public void setUp(){
        methodSearcher = new DefaultMethodSearcher(parameterNameProvider);
		adaptedMethodSearcher = new AdaptedMethodSearcher(methodSearcher, argumentParameterMatcher);
		dog = argument("dog", new Dog());
		cat = argument("cat", new Cat());

	}

	private Argument argument(String name, Object value) {
        return new Argument(name, value);
    }

	@Test
	public void should_match_default_method_with_correct_parameters() throws NoSuchMethodException {
        Parameter[] anyParameters = any(Parameter[].class);
        Argument[] anyArguments = any(Argument[].class);
        when(argumentParameterMatcher.getValuesMatchingParameters(anyParameters, anyArguments)).thenReturn(new Argument[]{cat, dog});
        BrutauthMethod method = adaptedMethodSearcher.search(new AnimalsRule(), cat, dog);
		
		assertMatchedMethod(method);
	}


	@Test
	public void should_match_default_method_with_one_correct_parameter() throws NoSuchMethodException {
        Parameter[] anyParameters = any(Parameter[].class);
        Argument[] anyArguments = any(Argument[].class);
        when(argumentParameterMatcher.getValuesMatchingParameters(anyParameters, anyArguments)).thenReturn(new Argument[]{cat});
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), cat, dog);

        assertMatchedMethod(method);
    }
	
	@Test
	public void should_match_methods_without_parameters() throws NoSuchMethodException {
        Parameter[] anyParameters = any(Parameter[].class);
        Argument[] anyArguments = any(Argument[].class);
        when(argumentParameterMatcher.getValuesMatchingParameters(anyParameters, anyArguments)).thenReturn(new Argument[]{});
		BrutauthMethod method = adaptedMethodSearcher.search(new NoneRule(), cat, cat);
		
		assertMatchedMethod(method);
	}
	
	@Test
	public void should_ignore_nulls() throws NoSuchMethodException {
        Parameter[] anyParameters = any(Parameter[].class);
        Argument[] anyArguments = any(Argument[].class);
        when(argumentParameterMatcher.getValuesMatchingParameters(anyParameters, anyArguments)).thenReturn(new Argument[]{cat});
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), cat, null);
		
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
	


}
