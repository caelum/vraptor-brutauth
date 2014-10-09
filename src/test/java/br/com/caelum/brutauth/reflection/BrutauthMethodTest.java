package br.com.caelum.brutauth.reflection;


import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class BrutauthMethodTest {

	
	@Test
	public void should_find_method_with_two_arguments() throws NoSuchMethodException, SecurityException {
		Method realMethod = FakeRuleWithTwoArguments.class.getMethod("isAllowed", RightFakeObject.class, OtherRightFakeObject.class);
		Argument[] arguments = fakeArgs(new RightFakeObject(), new OtherRightFakeObject());
		BrutauthMethod brutauthMethod = new BrutauthMethod(arguments, realMethod, new FakeRuleWithTwoArguments());
		assertTrue(brutauthMethod.invoke());
	}
	@Test
	public void should_not_throw_exception_if_method_is_defined() throws NoSuchMethodException, SecurityException {
		Method realMethod = FakeRule.class.getMethod("isAllowed", RightFakeObject.class);
		Argument[] arguments = fakeArgs(new RightFakeObject());
		BrutauthMethod brutauthMethod = new BrutauthMethod(arguments, realMethod, new FakeRule());
		assertTrue(brutauthMethod.invoke());
	}
	
	@Test
	public void should_not_throw_exception_if_method_is_definedsdas() throws NoSuchMethodException, SecurityException {
		Method realMethod = FakeRule.class.getMethod("isAllowed", RightFakeObject.class);
		Argument[] arguments = fakeArgs(null);
		BrutauthMethod brutauthMethod = new BrutauthMethod(arguments, realMethod, new FakeRule());
		assertTrue(brutauthMethod.invoke());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void should_throw_exception_if_method_is_not_defined() throws NoSuchMethodException, SecurityException {
		Method realMethod = FakeRule.class.getMethod("isAllowed", RightFakeObject.class);
		Argument[] arguments = fakeArgs(new FalseFakeObject());
		new BrutauthMethod(arguments, realMethod, new FakeRule());
	}
	
	@Test
	public void should_invoke_method_with_varargs() throws NoSuchMethodException, SecurityException {
		Method realMethod = FakeRuleWithVarargs.class.getMethod("isAllowed", Object[].class);
		Object[] varargs = new Object[]{new Object[]{new FalseFakeObject()}};
		Argument[] arguments = fakeArgs(varargs);
		BrutauthMethod brutauthMethod = new BrutauthMethod(arguments, realMethod, new FakeRuleWithVarargs());
		assertTrue(brutauthMethod.invoke());
	}
	
	@Test
	public void should_find_method_without_arguments() throws NoSuchMethodException, SecurityException {
		Method realMethod = FakeRuleWithoutArguments.class.getMethod("isAllowed");
		BrutauthMethod brutauthMethod = new BrutauthMethod(null, realMethod, new FakeRuleWithoutArguments());
		assertTrue(brutauthMethod.invoke());
	}

	private Argument[] fakeArgs(Object... rules) {
		if(rules == null){
			return new Argument[]{null};
		}
		Argument[] arguments = new Argument[rules.length];
		for (int i = 0; i < rules.length; i++) {
			arguments[i] = new Argument("name"+i, rules[i]);
		}
		return arguments;
	}

	public class FakeRule implements CustomBrutauthRule{
		public boolean isAllowed(RightFakeObject fake){
			return true;
		}
	}
	
	public class FakeRuleWithVarargs implements CustomBrutauthRule{
		public boolean isAllowed(Object...args){
			return true;
		}
	}
	
	public class FakeRuleWithTwoArguments implements CustomBrutauthRule{
		public boolean isAllowed(RightFakeObject fake, OtherRightFakeObject fake2){
			return true;
		}
	}
	
	public class FakeRuleWithoutArguments implements CustomBrutauthRule{
		public boolean isAllowed(RightFakeObject fake){
			return true;
		}
		
		public boolean isAllowed(){
			return true;
		}
	}
	
	public class RightFakeObject{}

	public class OtherRightFakeObject{}

	public class FalseFakeObject{}


}
