package br.com.caelum.brutauth.auth.rules;


public class BrutauthDefaultRule implements CustomBrutauthRule{
	public boolean isAllowed(){
		return true;
	}
}
