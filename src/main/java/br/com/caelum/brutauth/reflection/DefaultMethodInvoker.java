package br.com.caelum.brutauth.reflection;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.methodsearchers.MethodSearchers;

public class DefaultMethodInvoker implements MethodInvoker {

		@Inject private MethodSearchers searcher;

		public boolean invoke(CustomBrutauthRule toInvoke, Argument[] args) {
			return searcher.search(toInvoke, args).invoke();
		}

}
