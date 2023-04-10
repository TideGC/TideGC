---
alias: 
- nodejs 在 CentOS Stream 上的卸载
tags: 
- linux 
- nodejs
---

## nodejs 在 CentOS Stream 上的卸载

To completely remove Node.js installed from the rpm.nodesource.com package methods above:

```bash
dnf remove -y nodejs
rm -r /etc/yum.repos.d/nodesource*.repo
dnf clean all
```

