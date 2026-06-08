# 学生选课系统

基于 Spring Boot 3 + Vue 3 的前后端分离选课管理系统，支持学生在线选课、教师管理课程与成绩、管理员系统监控。

---

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **后端** | Java / Spring Boot 3 | JDK 17 / 3.2.0 |
| | MyBatis-Plus | 3.5.5 |
| | Spring Security + JWT | jjwt 0.12.3 |
| | MySQL | 5.7+ |
| | Maven | — |
| **前端** | Vue 3 (Composition API) | 3.4.0 |
| | TypeScript | 5.3 |
| | Vite | 5.0 |
| | Pinia / Vue Router 4 | — |
| | Element Plus | 2.4 |
| | Axios | 1.6 |

---

## 功能概览

### 学生
- 浏览课程列表（分页、搜索）
- 在线选课 / 退课
- 查看我的课表
- 查询课程成绩

### 教师
- 管理授课课程（新增 / 编辑）
- 查看选课学生名单
- 录入 / 修改学生成绩（支持批量保存）
- 仪表盘统计数据

### 管理员
- 用户管理（启用 / 禁用账号）
- 课程管理（开放 / 关闭课程）
- 系统仪表盘（用户数、课程数、选课记录等数据概览）

---

## 快速启动

### 前置条件

- JDK 17+
- Maven 3.6+
- Node.js 18+ / pnpm
- MySQL 5.7+

### 1. 初始化数据库

```bash
# 登录 MySQL 并执行初始化脚本
mysql -u root -p < backend/sql/init.sql
```

脚本将自动创建数据库 `course_select`、建表，并插入预置账号。

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端启动在 `http://localhost:8080`。

### 3. 启动前端

```bash
cd frontend
pnpm install
pnpm run dev
```

前端启动在 `http://localhost:3000`，已配置代理转发 `/api` → `localhost:8080`。

---

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | `admin` | `20040203` |
| 教师 | `zhang_san` | `20040203` |
| 教师 | `li_si` | `20040203` |
| 教师 | `wang_wu` | `20040203` |
| 学生 | `stu_001` ~ `stu_005` | `20040203` |

---

## 项目结构

```
├── backend/                     # Spring Boot 后端
│   ├── pom.xml
│   ├── sql/init.sql             # 数据库初始化脚本
│   └── src/main/java/com/example/courseselect/
│       ├── common/              # 统一响应、状态码、全局异常处理
│       ├── config/              # 跨域/安全/MyBatis-Plus 配置
│       ├── controller/          # RESTful 控制器
│       ├── dto/                 # 请求/响应数据传输对象
│       ├── entity/              # 数据库实体
│       ├── mapper/              # MyBatis-Plus 数据访问层
│       ├── security/            # JWT 令牌/过滤器/认证
│       └── service/             # 业务服务接口及实现
│
├── frontend/                    # Vue 3 前端
│   ├── package.json
│   ├── vite.config.ts
│   └── src/
│       ├── api/                 # 后端 API 调用封装
│       ├── components/          # 公共组件
│       ├── router/              # 路由配置（含路由守卫）
│       ├── stores/              # Pinia 状态管理
│       ├── types/               # TypeScript 类型定义
│       ├── utils/               # Axios 实例、本地存储工具
│       ├── views/               # 页面组件
│       └── assets/              # 全局样式
│
└── README.md
```

---

## API 接口

### 认证 (`/api/auth`)
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/register` | 学生注册 |
| GET | `/api/auth/me` | 获取当前用户信息 |
| PUT | `/api/auth/profile` | 更新个人信息 |
| PUT | `/api/auth/password` | 修改密码 |

### 课程 (`/api/courses`)
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/courses` | 课程分页列表（公开） |
| GET | `/api/courses/{id}` | 课程详情（公开） |
| PUT | `/api/courses/{id}/toggle-status` | 切换课程状态 |

### 选课 (`/api/selection`)
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/selection/select/{courseId}` | 选课 |
| POST | `/api/selection/drop/{courseId}` | 退课 |
| GET | `/api/selection/schedule` | 我的课表 |
| GET | `/api/selection/grades` | 成绩查询 |
| GET | `/api/selection/my-courses` | 我的课程列表 |

### 教师 (`/api/teacher`)
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/teacher/courses` | 我的授课课程 |
| POST | `/api/teacher/courses` | 新建课程 |
| PUT | `/api/teacher/courses/{id}` | 更新课程 |
| GET | `/api/teacher/courses/{courseId}/students` | 选课学生名单 |
| POST | `/api/teacher/score` | 录入/修改成绩 |

### 管理员 (`/api/admin`)
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/admin/users` | 用户列表 |
| PUT | `/api/admin/users/{id}/toggle-status` | 启用/禁用用户 |
| GET | `/api/admin/courses/{courseId}/students` | 查看课程所有学生 |

### 仪表盘 (`/api/dashboard`)
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/dashboard/stats` | 首页统计数据（按角色返回） |

---

## 数据库设计

### t_user — 用户表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(255) | BCrypt 加密密码 |
| real_name | VARCHAR(50) | 真实姓名 |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机号 |
| role | VARCHAR(20) | ADMIN / TEACHER / STUDENT |
| student_no | VARCHAR(50) | 学号/工号 |
| status | TINYINT | 1启用 0禁用 |
| deleted | TINYINT | 逻辑删除标识 |

### t_course — 课程表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| course_name | VARCHAR(100) | 课程名称 |
| course_code | VARCHAR(50) | 课程编号 |
| teacher_id | BIGINT | 授课教师 ID |
| credit | TINYINT | 学分 |
| max_capacity | INT | 最大容量 |
| current_count | INT | 已选人数 |
| semester | VARCHAR(50) | 学期 |
| schedule | VARCHAR(100) | 上课时间 |
| classroom | VARCHAR(100) | 上课地点 |
| status | TINYINT | 1开放 0关闭 |

### t_course_selection — 选课记录表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| student_id | BIGINT | 学生 ID |
| course_id | BIGINT | 课程 ID |
| status | TINYINT | 1在学习 2已退课 3已结课 |
| score | DECIMAL(5,2) | 成绩 |
| select_time | DATETIME | 选课时间 |
| deleted | TINYINT | 逻辑删除标识 |
