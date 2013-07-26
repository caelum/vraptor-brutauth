package br.com.caelum.brutauth.auth.rules;


public interface SimpleBrutauthRule extends BrutauthRule {
	boolean isAllowed(long accessLevel);
}
