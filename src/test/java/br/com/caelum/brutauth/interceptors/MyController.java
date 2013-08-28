package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.util.TestUtils;
import br.com.caelum.vraptor.resource.ResourceMethod;

public class MyController {
	public static final String MY_STRING = "Brutauth rulez";
	public static final String UNNACCEPTABLE_STRING = "Brutauth suckz";

	@SimpleBrutauthRules(MySimpleBiggerThanZeroRule.class)
	public void mySimpleRuleMethod() {}

	@SimpleBrutauthRules(MySimpleBiggerThanZeroRule.class)
	@AccessLevel(1234l)
	public void mySimpleRuleMethodWithAccessLevel() {}

	@SimpleBrutauthRules({ MySimpleBiggerThanZeroRule.class, AnotherSimpleRule.class })
	public void myManySimpleRulesMethod() {}

	@SimpleBrutauthRules({ MySimpleBiggerThanZeroRule.class, AnotherSimpleRule.class })
	@AccessLevel(1234l)
	public void myManySimpleRulesMethodWithAccessLevel() {}

	@CustomBrutauthRules(MyCustomRule.class)
	public void myCustomRuleMethod(String myString) {}

	@CustomBrutauthRules({MyCustomRule.class, AnotherCustomRule.class})
	public void myManyCustomRulesMethod(String myString) {}

	public static ResourceMethod method(String method) {
		return TestUtils.method(MyController.class, method);
	}
	
	public static BrutauthClassOrMethod brutauthMethod(String method) {
		return TestUtils.brutauthMethod(MyController.class, method);
	}
}
