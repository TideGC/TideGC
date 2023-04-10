## Nginx 的“转发”配置模板

Nginx 的 proxy_pass 转发规则比较诡异…

之所以“诡异”是因为它要实现更强大、更丰富的功能。但是，对于我们普通程序员<small>（而非专业运维而言）</small>，我们一方面用不上这么强大的功能，另一方面还提升了复杂度，十分不友好…

不过，对于我们日常的“请求转发<small>（解决跨域问题）</small>”和“负载均衡”功能而言，我们可以将负责的规则总结成固定的套路。

- **套路一**：location 结尾有没有 `/` 不是问题，它不影响转发规则。为了方便，我们“故意”让 location 结尾==有没有== `/` 和 proxy_pass 保持一致。
   
  proxy_pass 有，location 就故意加上；proxy_pass 没有，location 结尾就不加。

- **套路二**：你的计划中==要不要==砍掉 URI 路径中的前缀<small>（location）</small>，取决于 proxy_pass 结尾有没有 `/` 。

  你要砍前缀（location），proxy_pass 结尾就故意加上 `/`；你要保留前缀<small>（location）</small>，proxy_pass 就一定不能以 `/` 结尾。

> [!cite]- 去前缀 /api 的版本示例
> **有 “/” 砍前缀**
> 
> ```
> location /api/ {
>     proxy_pass http://localhost:8080/;
> }
> ```
> 
> 请求走的是下面的 `2.a` 的规则。
> 
> 当你请求 `/api/login` 时，Nginx 会转给 `http://localhost:8080/login`。截去了 URI 中的 `/api` 。

> [!cite]- 不去前缀 /api 的版本示例
> **无 “/” 留前缀**
> 
> ```
> location /api {
>     proxy_pass http://localhost:8080;
> }
> ```
> 
> 请求走的是下面的 `2.b` 的规则。
> 
> 当你请求 `/api/login` 时，Nginx 会转给 `http://localhost:8080/api/login` 。URI 中的 `/api` 保留了下来。

