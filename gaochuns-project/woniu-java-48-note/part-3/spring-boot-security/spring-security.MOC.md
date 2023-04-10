---
alias:
- moc
- spirng-security
---

> [!multi-column]
> > [!attention] 上：非前后端分离
> > - 问题一：[[202212011430.问题一|需要实现登录功能]] 
> >   - 解决方案：[[202302052110.helloworld|为项目引入 spring-security]]
> > - 问题二：[[202212011431.问题二|需要使用定制的用户名和密码]] 
> >   - 解决方案：[[202302052111.helloworld-plus|自定义 UserDetailsService]]
> > - 问题三：[[202212011432.问题三|定制的用户名和密码应该来自数据库中]] 
> >   - 解决方案：在 UserDetailsService 中访问数据库
> > - 问题四：需要定制登录页面 
> >   - 解决方案：[[202208151738|自定义登录页面]]
> > - 问题五：需要实现鉴权功能 
> >   - 解决方案：自定义 URI 的访问权限（[[202208151833|关于鉴权配置]]）
> > - 问题六：有没有偷懒的“空间”
> >   - 解决方案：使用注解简化配置（[[202208151926|关于 spring-security 的鉴权注解]]）
> 
> > [!summary] 下：前后端分离
> > - [[202212011426.问题一|问题一]] && [[202212220945|解决问题一：认证返回自定义 JOSN 串]]
> > - [[202212011427.问题二|问题二]] && [[202301061909.failure|解决问题二：鉴权失败后返回 JSON String]]
> > - [[08.JWT|Spring Security 整合 JWT]]

> [!tip] 核心过滤器和地层原理
> - [[06.原理-概括版|Spring Security 底层原理（概括版）]]
> - [[202208151736|过滤器链中的 UsernamePasswordAuthenticationFilter]]

---

- [[02.UserDetailsService|关于 UserDetailsService 的更多信息]]
- [[03.配置|Spring Security 的基本配置]]

