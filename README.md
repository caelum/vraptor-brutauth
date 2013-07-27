vraptor-brutauth
================


###Regras simples

Regras simples são aquelas que recebem apenas um nivel de acesso(long). 

####Como criar regras simples?


Basta criar uma classe que implementa `SimpleBrutauthRule` e anotá-la com o `@Component` do Vraptor.

ex:

```
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class CanAccess implements SimpleBrutauthRule {
	
	private UserSession userSession;

	public CanAccess(UserSession userSession) {
		this.userSession = userSession;
	}

	@Override
	public boolean isAllowed(long accessLevel) {
		return userSession.getUser().hasAccessLevel(accessLevel);
	}

}

```

####Como usar a regra que criei?


Você precisará anotar a action de seu controller com o `@SimpleBrutauthRules`, passando como argumento a classe de sua regra:

```
@Resource
public class BrutauthController {
	
	@SimpleBrutauthRules(CanAccess.class)
	public void somePage(){
		//logic
	}
}

```

Para passar o accessLevel necessário para sua regra, basta anotar a action com `@AccessLevel`, com o valor necessário.
O resultado será parecido com:

```

@Resource
public class BrutauthController {
	
	@SimpleBrutauthRules(CanAccess.class)
	@AccessLevel(2000)
	public void somePage(){
		//logic
	}
}

```

###Regras Customizada


O Brutauth oferece também o recurso de regras customizadas. A diferença delas para as regras simples é que você pode, no seu método `isAllowed`, receber como argumento qualquer coisa que a sua action receba.

Ou seja, se você tem uma action que recebe um objeto do tipo `Car`:

```
@Resource
public class BrutauthController {
	public void showCar(Car car){
		//logic
	}	
}
```

Você poderá receber o mesmo `Car` na sua regra. 

####Como criar regras customziadas?

Basta implementar `CustomBrutauthRule` e também precisa ser anotada com `@Component`

```
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class CanAccessCar implements CustomBrutauthRule {
	
	private UserSession userSession;

	public CanAccess(UserSession userSession) {
		this.userSession = userSession;
	}

	public boolean isAllowed(Car car) {
		return userSession.getUser().canAccess(car);
	}
}
```

Por padrão o nome do metodo deve ser `isAllowed` mas, se quiser usar outro nome, você pode anotar o metodo com `@BrutauthValidation`:

```
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class CanAccessCar implements CustomBrutauthRule {
	
	private UserSession userSession;

	public CanAccess(UserSession userSession) {
		this.userSession = userSession;
	}

	@BrutauthValidation
	public boolean otherName(Car car) {
		return userSession.getUser().canAccess(car);
	}
}
```

E não se esqueça de anotar sua action com `@CustomBrutauthRules(CanAccessCar.class)`:

```
@Resource
public class BrutauthController {

	@CustomBrutauthRules(CanAccessCar.class)
	public void showCar(Car car){
		//logic
	}	
}
```

###Como alterar a ação a ser feita após verificar uma regra?


Por padrão, o brutauth irá simplesmente devolver status `403` quando uma regra retornar false. Para alterar esse comportamento, 
você pode criar uma classe e implementar `RuleHandler`. Por exemplo, se você quiser que quando uma regra falhar, o usuario seja redirecionado para a pagina de login, você teria um `RuleHandler` parecido com esse:

```
package br.com.caelum.brutal.brutauth.auth.handlers;
@Component
public class LoggedHandler implements RuleHandler{
	private final Result result;

	public LoggedHandler(Result result) {
		this.result = result;
	}
	
	@Override
	public boolean handle(boolean isAllowed) {
		if(!isAllowed){
			result.redirectTo(AuthController.class).loginForm();
		}
        return isAllowed;
	}

}

```

Agora você só precisa anotar a sua regra com `@HandledBy(LoggedHandler.class)`:

```
@Component
@HandledBy(LoggedHandler.class)
public class LoggedAccessRule implements CustomBrutauthRule {
	
	private UserSession userSession;

	public CanAccess(UserSession userSession) {
		this.userSession = userSession;
	}

	public boolean isAllowed() {
		return userSession.isLogged();
	}
}
```

###Usando um RuleHandler diferente em apenas uma action

Você pode anotar a action com o mesmo `@HandledBy`, isso sobrescreverá o `RuleHandler` que foi definido na sua regra. 

Então, se você tem a action

```
@Resource
public class BrutauthController {
	@CustomBrutauthRules(LoggedAccessRule.class)
	@HandledBy(OtherHandler.class)
	public void showCar(Car car){
		//logic
	}	
}
```

O `RuleHandler` usado será o `OtherHandler`, mesmo se a sua regra `LoggedAccessRule` estiver anotada com um `@HandledBy`.

###Varias regras em uma só action

Você pode passar um array de regras para as annotations `CustomBrutauthRules` e `SimpleBrutauthRules`:

```
@Resource
public class BrutauthController {
	@CustomBrutauthRules({LoggedAccessRule.class, CanAccessCar.class})
	public void showCar(Car car){
		//logic
	}	
}
```

Deste modo, todas as regras serão validadas. O `RuleHandler` usado será o daquela que falhar, a não ser que a sua action esteja anotada com `@HandledBy`.