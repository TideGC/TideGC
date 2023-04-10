---
alias: 
- MinIO 在 CentOS Stream 上的安装
tags: 
- minio 
- linux
---

## MinIO 在 CentOS Stream 上的安装

CentOS Stream 自带的源中并没有 MinIO ，而且，MinIO 官方也没有提供专门的源来安装它。

MinIO 官方提供的是 MinIO 的安装包<small>（rpm 文件）</small>

对于初学者，你可以[[202208050920|卸载]]掉 minio 再重新安装来重新学习、熟悉安装过程的步骤。

### 第 1 步：下载 MinIO

从[官网](https://dl.min.io/server/minio/release/linux-amd64)下载 rpm 安装包。

例如：

```bash
cd /tmp && wget https://dl.min.io/server/minio/release/linux-amd64/minio-20220802235916.0.0.x86_64.rpm
```

### 第 2 步：安装

```bash
dnf install -y minio-20220802235916.0.0.x86_64.rpm
```

### 第 3 步：修改 minio 启动配置文件

minio 的启动配置文件在 /etc/systemd/system 下，叫 minio.service ，即，/etc/systemd/system/minio.service 。

```text
/etc/systemd/system 
└── minio.service 
```

执行以下命令，间接修改 minio.service 启动配置文件：

```sh
## 11 行附近
sed -i -e "s|User=minio-user|User=root|" /etc/systemd/system/minio.service 
## 12 行附近
sed -i -e "s|Group=minio-user|Group=root|" /etc/systemd/system/minio.service 
```


### 第 4 步：创建 minio 的数据目录

```bash
mkdir -p /opt/minio/data
```

### 第 5 步：创建并编辑 minio 配置文件

```bash
touch /etc/default/minio
```

执行如下命令，间接编辑 minio 配置文件：

```bash
cat > /etc/default/minio << EOF
MINIO_VOLUMES="/opt/minio/data"
MINIO_OPTS="--address :9001 --console-address :9000"
MINIO_ACCESS_KEY=minioadmin
MINIO_SECRET_KEY=minioadmin
EOF
```

### 第 6 步：运行，使用 systemctl 启停 minio

```bash
systemctl daemon-reload # 重新加载配置文件信息
systemctl start minio   # 启动
systemctl status minio  # 查看运行状态，验证启动
systemctl enable minio  # 设置为开机启动
# systemctl stop minio  # 停止
```







