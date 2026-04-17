# CLAUDE.md — 项目开发规则（单仓库：小程序 + 后台Web + Spring Boot）

## 目标
- 快速交付可上线功能：小步提交、每步可运行、可回滚。
- 严格最小化开发：只做需求范围内改动，禁止无关重构/优化。
- 后端严格遵循《阿里巴巴Java开发规范》。
- 完善注释：类、字段、方法、数据表均需添加注释并维护
## 仓库模块
- apps/miniapp：uni-app 小程序（TS/JS + Vue）
- apps/admin-web：后台管理（Vue + TS/JS）
- services/api-server：Spring Boot + PostgreSQL + MyBatis
- packages/shared-types：前端共享 DTO/枚举/错误码（TS）
- packages/api-client：前端请求封装（基于契约层）
- docs/specs: 功能规格
- docs/plan:执行计划/方案
- docs/changelist: 需求提交记录
## 命令（请按实际补齐/校准）
### 小程序（uni-app）
- 安装：cd apps/miniapp && npm install
- 开发编译：cd apps/miniapp && npm run dev:mp-weixin
- 生产构建：cd apps/miniapp npm run build:mp-weixin
- 高危依赖审计：cd apps/miniapp && npm run test:audit

### 后台Web（Vue）
- 安装：cd apps/admin-web && npm install
- 开发编译：cd apps/admin-web && npm run dev
- 生产构建：cd apps/admin-web && npm run build

### 后端（Spring Boot + MyBatis）
- 编译：cd services/api-server && mvn compile
- 打包：cd services/api-server && mvn clean package -DskipTests
- 本地运行：cd services/api-server &&  mvn spring-boot:run
- 本地启动：mvn -pl services/api-server spring-boot:run

## 开发流程（强制）
1. 先写 docs/specs/<feature>.md（功能规格），包含：接口/DTO/错误码/验收标准。
2. 再写实施计划（步骤+涉及文件+验证方式），计划必须可拆成小步提交。
3. 实现时严格按计划执行；每做完一步，必须能运行并通过最小验证。
4. 输出验证记录：跑了哪些命令、验证了哪些页面/接口。
5. 更新 docs/checklists/release.md（如影响发布/回归）。

## 变更边界（强制）
- 不新增无关依赖；如必须新增，说明原因、替代方案与影响。
- 不做“顺手重构”“顺手改格式”“顺手升级版本”。
- 不改动其他模块的公共接口，除非 spec 明确要求，并同步更新 shared-types/api-client。

## 后端编码规范（必须遵守阿里规范）
- 分层清晰：Controller 只做入参校验/调用Service/返回VO；业务逻辑在 Service。
- DTO/VO 分离；不要直接暴露 entity 到接口层。
- 统一返回体 Result<T>；统一错误码枚举；全局异常处理。
- 日志使用占位符：log.info("xxx id={}", id)；禁止字符串拼接。
- 入参校验使用 JSR-303：@Validated + @NotBlank/@NotNull 等。
- MyBatis：SQL 清晰可读；分页/排序/条件必须白名单化，避免注入。

## 前端规范（小程序/后台通用）
- 网络请求统一入口（api-client 或 services/request）；错误码统一处理。
- 状态以“业务实体/会话”为中心（如 attempt/order/user），不要把业务散落在页面里。
- 关键流程用状态机思维（例如支付：INIT→ORDER_CREATED→PAYING→PAID|CANCEL|FAIL）。

## 验收与回归（必须可勾选）
- 每个 feature spec 必须有验收清单（Given/When/Then 或 checklist）。
- 发布前必须跑最小回归：小程序主流程、后台核心页面、后端健康检查与关键接口。
- 发布前必须通过高危依赖审计：cd apps/miniapp && npm run test:audit
- **避免跨端联调 CORS 报错**：本地前端联调时，Admin 使用 Vite proxy（vite.config.ts），Miniapp (H5) 需要后端配置全局 CORS（WebMvcConfig.java）。
- **降低 MVP 本地环境差异**：优先使用 H2 内存数据库保证随时跑通（通过 spring.sql.init 自动刷入建表和测试数据），避免 PostgreSQL 本地账号配置带来的依赖报错。
- **Web/前端项目交付红线**：开发或修改完成后，绝不能仅凭“代码写完”就交付。必须执行 `npm run dev`，并使用 `OpenPreview`（或真实浏览器）查看渲染效果，验证是否存在打包或运行时报错。
