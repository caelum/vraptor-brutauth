package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.interceptors.CustomBrutauthRuleInterceptorTest.TrueCustomRule;


@CustomBrutauthRules({MyCustomRule.class})
@SimpleBrutauthRules({MySimpleBiggerThanZeroRule.class})
class ControllerWithRules{
	
	public void methodWithoutRules(){
	}
	
	@CustomBrutauthRules(TrueCustomRule.class)
	public void methodWithRules(){
	}
}
