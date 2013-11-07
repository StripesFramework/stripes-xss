# Stripes XSS Interceptor

Stripes XSS Interceptor escapes all the parameters that Stripes binds during its Validation & Binding phase using a wrapped request object (a convenient implementation of the `HttpServletRequest` interface). The code follows the XSS (Cross Site Scripting) security guidance posted at [Open Web Application Security Project (OWASP)](http://www.owasp.org).

**NOTE:** Parameters gotten manually through `request.getParameter()` are **not** sanitized.

This project is an update of the excellent [XSS filter](http://www.stripesframework.org/display/stripes/XSS+filter) from Jeff Ferber and contain the following changes:

* All external dependencies have been removed except for Servlet API and Stripes Framework.
* The class `sun.text.Normalizer` has been replaced with the newer `java.text.Normalizer` shipped with Java SE 6. Please read [Why Developers Should Not Write Programs That Call 'sun' Packages](http://java.sun.com/products/jdk/faq/faq-sun-packages.html).

## Configuration

### Maven Configuration

Add Stripes XSS Interceptor dependency to your project:

```xml
<dependency>
    <groupId>com.samaxes.stripes</groupId>
    <artifactId>stripesafe</artifactId>
    <version>VERSION</version>
</dependency>
```

### Stripes filter configuration

Add Stripes XSS Interceptor to Stripes filter `Extension.Packages` configuration in `web.xml`:

```xml
<init-param>
    <param-name>Extension.Packages</param-name>
    <param-value>com.samaxes.stripes.xss</param-value>
</init-param>
```

## License

This distribution is licensed under the terms of the Apache License, Version 2.0 (see LICENSE.txt).
