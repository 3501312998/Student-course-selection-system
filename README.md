# 学生选课系统

基于 Spring Boot 3 + Vue 3 的前后端分离 Web 应用。

## 技术栈

| 层次 | 技术 |
|------|------|
| 后端 | Java 17, Spring Boot 3.2, MyBatis-Plus, Spring Security, JWT |
| 前端 | Vue 3 (Composition API), TypeScript, Vite, Element Plus, Pinia, Axios |
| 数据库 | MySQL 5.7+ |
| 构建 | Maven, pnpm |

## 项目结构

```
学生选课系统/
├── backend/                    # 后端项目
│   ├── pom.xml
│   ├── sql/init.sql            # 数据库初始化脚本
│   └── src/main/java/com/example/courseselect/
│       ├── CourseSelectApplication.java
│       ├── common/              # 统一响应、全局异常处理
│       ├── config/              # 安全、跨域、MyBatis-Plus配置
│       ├── controller/          # RESTful 控制器
│       ├── dto/                 # 数据传输对象
│       ├── entity/              # 实体类
│       ├── exception/           # 业务异常
│       ├── mapper/              # MyBatis-Plus Mapper
│       ├── security/            # JWT认证
│       └── service/             # 业务逻辑
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/                # API 请求模块
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── types/              # TypeScript 类型
│   │   ├── utils/              # 工具函数
│   │   └── views/              # 页面组件
│   ├── package.json
│   └── vite.config.ts
├── report/                     # 课程设计报告
└── 需求分析.md                  # 需求分析文档
    系统架构设计.md               # 架构设计文档
```

## 快速启动

### 前置环境

- JDK 17+
- MySQL 5.7+
- Maven 3.8+
- Node.js 18+
- pnpm（或 npm）

### 1. 数据库初始化

```sql
-- 执行初始化脚本
source backend/sql/init.sql
```

或在 MySQL 客户端中直接运行 `backend/sql/init.sql` 中的全部 SQL。

数据库密码配置为 `20040203`，如需修改请同时更新 `application.yml`。

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`

### 3. 启动前端

```bash
cd frontend
pnpm install
pnpm dev
```

前端运行在 `http://localhost:3000`

### 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 20040203 |
| 教师 | zhang_san | 20040203 |
| 教师 | li_si | 20040203 |
| 教师 | wang_wu | 20040203 |
| 学生 | stu_001 ~ stu_005 | 20040203 |

## API 接口

### 认证模块
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 登录 |
| POST | `/api/auth/register` | 学生注册 |
| GET | `/api/auth/me` | 获取当前用户信息 |
| PUT | `/api/auth/profile` | 更新个人信息 |
| PUT | `/api/auth/password` | 修改密码 |

### 课程模块
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/courses` | 课程列表（分页） |
| GET | `/api/courses/{id}` | 课程详情 |

### 选课模块
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/selection/select/{courseId}` | 选课 |
| POST | `/api/selection/drop/{courseId}` | 退课 |
| GET | `/api/selection/schedule` | 我的课表 |
| GET | `/api/selection/grades` | 成绩查询 |
| GET | `/api/selection/my-courses` | 我的课程 |

### 教师模块
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/teacher/courses` | 我的课程列表 |
| POST | `/api/teacher/courses` | 创建课程 |
| PUT | `/api/teacher/courses/{id}` | 编辑课程 |
| GET | `/api/teacher/courses/{courseId}/students` | 学生名单 |
| POST | `/api/teacher/score` | 录入成绩 |

### 管理模块
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/admin/users` | 用户列表 |
| PUT | `/api/admin/users/{id}/toggle-status` | 启用/禁用用户 |
| GET | `/api/admin/courses/{courseId}/students` | 课程学生列表 |
