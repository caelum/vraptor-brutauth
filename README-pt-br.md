VRaptor Brutauth
================

VRaptor Brutauth fornece um jeito fácil e prático de verificar permissões de acesso(autorização) em determinada action e/ou controller quando usando VRaptor


##Como Instalar?

Basta adicionar as seguintes linhas ao seu pom.xml:

```xml
<dependency>
	<groupId>br.com.caelum.vraptor</groupId>
	<artifactId>vraptor-brutauth</artifactId>
	<version>4.0.1.Final</version> <!--ou última versão-->
</dependency>
```

##Como Usar?

O VRaptor Brutauth suporta dois tipos de regras de autenticação: simples e customizadas.

###Regras simples

Regras simples são aquelas que autorizam o acesso a um recurso baseadas apenas em um nível de acesso (um long).

####Como criar regras simples?


Basta criar uma classe que implementa `SimpleBrutauthRule`.

ex:

```java
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CanAccess implements SimpleBrutauthRule {

	private UserSession userSession;

	/**
	 * @deprecated CDI eyes only
	 */
	protected CanAccess() {
		this(null);
	}

	@Inject
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


Você precisará anotar a action de seu controller com o `@SimpleBrutauthRules` passando como argumento a classe de sua regra:

```java
@Controller
public class BrutauthController {

	@SimpleBrutauthRules(CanAccess.class)
	public void somePage(){
		//logic
	}
}

```

Para passar o accessLevel necessário para sua regra, basta anotar a action com `@AccessLevel` com o valor necessário.
O resultado será parecido com:

```java
@Controller
public class BrutauthController {

	@SimpleBrutauthRules(CanAccess.class)
	@AccessLevel(2000)
	public void somePage(){
		//logic
	}
}

```

###Regras customizadas


O Brutauth oferece também o recurso de regras customizadas. A diferença delas para as regras simples é que você pode, no seu método `isAllowed`, receber como argumento qualquer coisa que a sua action receba.

Ou seja, se você tem uma action que recebe um objeto do tipo `Car`:

```java
@Controller
public class BrutauthController {
	public void showCar(Car car){
		//logic
	}
}
```

Você poderá receber o mesmo `Car` na sua regra.

####Como criar regras customizadas?

Basta implementar `CustomBrutauthRule`.

```java
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CanAccessCar implements CustomBrutauthRule {

	private UserSession userSession;

	/**
	 * @deprecated CDI eyes only
	 */
	protected CanAccessCar() {
		this(null);
	}

	@Inject
	public CanAccessCar(UserSession userSession) {
		this.userSession = userSession;
	}

	public boolean isAllowed(Car car) {
		return userSession.getUser().canAccess(car);
	}
}
```

Por padrão, o nome do metodo deve ser `isAllowed` mas, se quiser usar outro nome, você pode anotar o metodo com `@BrutauthValidation`:

```java
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class CanAccessCar implements CustomBrutauthRule {

	private UserSession userSession;

	/**
	 * @deprecated CDI eyes only
	 */
	protected CanAccessCar() {
		this(null);
	}

	@Inject
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

```java
@Controller
public class BrutauthController {

	@CustomBrutauthRules(CanAccessCar.class)
	public void showCar(Car car){
		//logic
	}
}
```

###E se eu quiser limitar o acesso de um controller inteiro?

Para isso, você não precisa anotar cada método com a regra necessária. Basta anotar o controller:

```java
@Controller
@SimpleBrutauthRules(CanAccess.class)
public class BrutauthController {
	public void somePage(){
		//logic
	}

	public void otherPage(){
		//logic
	}
}
```
Assim, ao tentar acessar qualquer método desse controller, a regra `CanAccess.class` terá de ser satisfeita

E isso também funciona com o `@CustomBrutauthRules`:

```java
@Controller
@CustomBrutauthRules(CanAccessCar.class)
public class CarController {
	public void showCar(Car car){
		//logic
	}

	public void editCar(Car car){
		//logic
	}
}
```

###E se eu precisar limitar o sistema inteiro com uma regra?
Você pode criar uma `DefaultRule` nesse caso.
Tudo que você precisa fazer é anotar sua classe com `@DefaultRule`.

Exemplo:

```java
@RequestScoped @DefaultRule
public class ShoudBeLoggedRule implements CustomBrutauthRule {

	@Inject	private UserSession userSession;

	public boolean isAllowed() {
		return userSession.isLogged();
	}
}
```

A `DefaultRule` vai ser usada em todos os métodos e controllers.

###Como alterar a ação a ser feita após verificar uma regra?


Por padrão, o vraptor-brutauth simplesmente devolverá um status `403` quando uma regra retornar false. Para alterar esse comportamento,
você pode criar uma classe e implementar `RuleHandler`.

Por exemplo, se você quiser que quando determinada regra falhar o usuario seja redirecionado para a página de login, você teria um `RuleHandler` parecido com esse:

```java
@RequestScoped
public class LoggedHandler implements RuleHandler{
	private final Result result;

	/**
	 * @deprecated CDI eyes only
	 */
	protected LoggedHandler() {
		this(null);
	}

	@Inject
	public LoggedHandler(Result result) {
		this.result = result;
	}

	@Override
	public void handle() {
		result.redirectTo(AuthController.class).loginForm();
	}
}

```

Agora você só precisa anotar a sua regra com `@HandledBy(LoggedHandler.class)`:

```java
@RequestScoped
@HandledBy(LoggedHandler.class)
public class LoggedAccessRule implements CustomBrutauthRule {

	private UserSession userSession;

	/**
	 * @deprecated CDI eyes only
	 */
	protected LoggedAccessRule() {
		this(null);
	}

	@Inject
	public LoggedAccessRule(UserSession userSession) {
		this.userSession = userSession;
	}

	public boolean isAllowed() {
		return userSession.isLogged();
	}
}
```

E o seu `RuleHandler` será invocado quando essa regra falhar.

###Usando um RuleHandler diferente em apenas uma action

Você pode anotar a action com o mesmo `@HandledBy`. Isso sobrescreverá o `RuleHandler` que foi definido na sua regra.

Então, se você tem a action

```java
@Controller
public class BrutauthController {
	@CustomBrutauthRules(LoggedAccessRule.class)
	@HandledBy(OtherHandler.class)
	public void showCar(Car car){
		//logic
	}
}
```

O `RuleHandler` usado será o `OtherHandler`, mesmo se a sua regra `LoggedAccessRule` estiver anotada com um `@HandledBy`.

###Várias regras em uma só action

Você pode passar um array de regras para as annotations `CustomBrutauthRules` e `SimpleBrutauthRules`:

```java
@Controller
public class BrutauthController {
	@CustomBrutauthRules({LoggedAccessRule.class, CanAccessCar.class})
	public void showCar(Car car){
		//logic
	}
}
```

Deste modo, todas as regras serão validadas da esquerda para a direita, até uma delas falhar ou todas permitirem o acesso. O `RuleHandler` usado será o daquela que falhar, a não ser que a sua action esteja anotada com `@HandledBy`.

###Usando as regras na view

Para verificar se uma regra é satisfeita na view, use o objeto `rules`. Por exemplo:

```jsp
<c:if test="${rules[CanAccessCar].isAllowed(car)}">
	<a href="brutauth/showCar">Show car</a>
</c:if>
```
