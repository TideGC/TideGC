---
alias: Spring Security 中 Session 的创建机制
---


| 机制	| 描述 |
| :- | :- |
|always|	一旦登陆成功就创建一个 session 。同一个账户反复登录（不退出）时，会有多个 session 。|
|ifRequired	|登陆成功就创建，同一个账户反复登录使用同一个 session 。（默认）|
|never	|SpringSecurity 将不会创建Session，但是如果应用中其他地方创建了Session，那么Spring Security将会使用它。|
|stateless	| SpringSecurity将绝对不会创建Session|



