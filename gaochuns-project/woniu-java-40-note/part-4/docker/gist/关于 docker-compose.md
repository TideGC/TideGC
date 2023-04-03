## 关于 docker-compose

Docker Compose 可以基于 Compose 文件帮我们快速的部署分布式应用，而无需手动一个个创建和运行容器！Compose 文件是一个文本文件，通过指令定义集群中的每个容器如何运行。

Compose 有 2 个重要的概念：

- **项目**<small>（Project）</small>：由一组关联的应用容器组成的一个完整业务单元，在 docker-compose.yml 文件中定义。

- **服务**<small>（Service）</small>：一个应用的容器，实际上可以包括若干运行相同镜像的容器实例。

关于 docker-compose 的安装参见笔记[[centos.dockercompose|《docker-compose 在 CentOS Stream 上的安装与卸载》]]。


