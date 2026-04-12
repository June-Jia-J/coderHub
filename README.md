# 程序员交流博客（MVP）

面向程序员的交流学习博客，支持发布博客与 **AI 小巧思**（Ollama 本地模型辅助写作）。

## 前端页面风格特点（像素风 + 赛博朋克 + 骇客）

- **像素风**
  - **字体**：等宽或像素风格字体（如 VT323、Share Tech Mono），标题可选用像素/复古感字体。
  - **轮廓**：直角或小圆角（2px～6px），块状分明，避免大圆角。
  - **布局**：网格感、卡片块状分明，边界清晰。

- **赛博朋克**
  - **背景**：深色（黑/深灰/深蓝），层次分明。
  - **主色**：霓虹色点缀（青/氰色 #0d9488 为主，可辅以霓虹发光）。
  - **光效**：关键元素外发光（box-shadow 霓虹光晕）、高对比。

- **骇客/终端感**
  - **终端感**：等宽字体、命令行式排版，代码块风格。
  - **配色**：深色底 + 青绿/氰色高亮，或高对比单色。
  - **元素**：边框像终端窗口、焦点时霓虹描边。

以上特点在 `frontend/src/assets/styles/variables.css` 与各页面组件中统一体现；主色保持青绿 #0d9488，无紫色。

## 🛠️ 技术栈

- **Frontend**: Vue 3 + Vite + Element Plus + Pinia + Axios + Marked
- **Backend**: Spring Boot 3 + MyBatis + MySQL + Flyway + JWT
- **Database**: MySQL 8.0
- **AI**: Ollama（Docker 镜像 `ollama/ollama`，与后端同网，无需宿主机安装）
- **部署**: Docker + Docker Compose

## 🚀 快速启动 (Docker)

1. 确保 Docker Desktop 已运行
2. 在项目根目录执行：`docker compose up --build`
3. 等待容器启动完成（首次构建可能较久）
4. 访问前端：http://localhost:3530
5. **首次使用 AI 功能前**，执行一次拉取模型（约 1GB，仅首次）：
   ```bash
   docker compose exec ollama ollama pull deepseek-r1:1.5b
   ```

## 🧪 测试账号

- 用户名：`admin`
- 密码：`123456`

## 🗃️ 功能介绍

### 核心功能

1. **用户注册与登录**
   - JWT Token 认证
   - 登录状态持久化（localStorage）
   - 路由守卫（未登录访问写博客跳转登录）

2. **博客列表与详情**
   - 博客列表（分页、按时间倒序）
   - 卡片展示标题、摘要、作者、时间
   - 点击进入详情，Markdown 渲染正文

3. **发布博客**
   - 登录后可见「写博客」入口
   - 标题 + 正文（支持 Markdown）
   - 发布成功后跳转列表或详情

4. **AI 小巧思**
   - **与 AI 对话**：在写博客页与 AI 多轮对话，获取灵感
   - **AI 帮我写**：根据当前标题（及提纲）生成正文并插入编辑器
   - 依赖 Ollama 容器内模型 `deepseek-r1:1.5b`（需先执行上述 `ollama pull`）

5. **博客编辑与删除**
   - 仅作者本人可编辑/删除（需登录）
   - 详情页对作者显示「编辑」「删除」按钮

6. **用户个人主页**
   - 通过 `/users/:username` 查看某用户发布的全部博客（分页）
   - 列表页与详情页作者名可点击跳转

7. **评论功能**
   - 详情页展示评论列表
   - 登录后可发表评论（长度限制 1000）

### 技术特点

- **零依赖部署**：只需 Docker 即可运行，无需安装 Node、Java、Ollama 等环境
- **数据持久化**：MySQL 与 Ollama 模型数据均挂载 Volume，重启不丢失
- **现代化 UI**：深色主题 + 青绿主色，响应式布局，支持 PC 与移动端
- **SPA 友好**：Nginx 配置 `try_files`，刷新任意路由不 404

## 📁 项目结构

```
32. coderHub-3530/
├── README.md                    # 项目说明文档
├── docker-compose.yml           # Docker 编排（db / ollama / backend / frontend）
├── frontend/                    # 前端代码
│   ├── Dockerfile
│   ├── nginx.conf               # SPA 路由回退配置
│   ├── src/
│   │   ├── api/                 # 接口封装（request、auth、blog、ai、comment）
│   │   ├── components/          # 公共组件（AppHeader、MarkdownView、AiChat、CommentList）
│   │   ├── views/               # 页面（Login、Register、BlogList、BlogDetail、BlogEdit、UserProfile）
│   │   ├── router/              # 路由与守卫
│   │   ├── stores/              # Pinia 用户状态
│   │   ├── utils/               # 校验等工具
│   │   └── assets/styles/       # 全局样式与设计变量
│   └── package.json
├── backend/                     # 后端代码
│   ├── Dockerfile
│   ├── settings.xml            # Maven 阿里云镜像
│   ├── src/main/
│   │   ├── java/com/example/blog/
│   │   │   ├── config/          # CORS、JWT 拦截、Ollama 配置
│   │   │   ├── controller/     # 认证、博客、AI、健康检查
│   │   │   ├── service/        # 业务逻辑
│   │   │   ├── mapper/         # MyBatis
│   │   │   ├── entity/         # User、Blog、Comment
│   │   │   ├── dto/            # 请求/响应 DTO
│   │   │   └── exception/      # 全局异常处理
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/   # Flyway 建表与 Seed
│   └── pom.xml
└── doc/                         # 需求说明、开发设计、任务清单等
```

## 🔧 开发环境

### 前端开发

```bash
cd frontend
npm install
npm run dev
```

前端默认代理 `/api` 到 `http://localhost:8207`，需先启动后端。

### 后端开发

```bash
cd backend
# 需本地 Java 17+、Maven、MySQL 8（或 Docker 仅起 db）
# 配置 application.yml 中 DB_URL 为 jdbc:mysql://localhost:3306/blog...
mvn -s settings.xml spring-boot:run
```

### 仅启动数据库与 Ollama（供本地前后端联调）

```bash
docker compose up -d db ollama
# 拉取模型：docker compose exec ollama ollama pull deepseek-r1:1.5b
```

## 📝 服务与端口

| 服务     | 地址                          |
|----------|-------------------------------|
| 前端     | http://localhost:3530         |
| 后端 API | http://localhost:8207         |
| 数据库   | localhost:3306（root / root） |
| Ollama   | localhost:11434（容器内后端通过服务名 `http://ollama:11434` 访问） |

## 🐳 Docker 配置

- **前端端口**: 3530
- **后端端口**: 8207
- **数据库**: MySQL 8.0，Volume `mysql_data`
- **Ollama**: 11434，Volume `ollama_data`（模型持久化）

### 清理 Docker 缓存并重新构建

#### 方法一：清理构建缓存并重新构建（推荐）

```bash
docker compose down
docker compose build --no-cache
docker compose up -d
```

#### 方法二：完全清理（包括镜像）

```bash
docker compose down
docker rmi 32coderhub-3530-backend 32coderhub-3530-frontend 32coderhub-3530-ollama
docker builder prune -f
docker compose up --build
```

#### 方法三：深度清理（清理所有 Docker 资源）

```bash
docker compose down
docker system prune -a --volumes -f
docker compose up --build
```

**注意**：方法三会删除所有未使用的镜像与卷，请谨慎使用。若需保留数据库与 Ollama 模型，可先仅删除项目容器与镜像，再执行 `docker compose up --build`。

## 📄 许可证

MIT

---

## 修订历史

| 版本 | 作者 | 日期 | 说明 |
|------|------|------|------|
| v1 | jiajing(jiajing@163.com) | 2026-02-28 | 初版：登录注册、博客浏览/发布、AI 小巧思、Docker 一键启动。 |
| v2 | jiajing(jiajing@163.com) | 2026-03-17 | 新增：博客编辑/删除、用户个人主页、评论功能；README 补充新增功能说明与结构更新。 |

**项目作者**: jiajing(jiajing@163.com)  
**最后更新**: 2026-03-17
