# 美业平台实施计划

## 阶段一：数据库与基础设施 (Backend MVP)
- **步骤 1.1：定义共享类型与实体 (shared-types & entity)**
  - 涉及文件：`packages/shared-types/src/index.ts`, `services/api-server/src/main/resources/schema.sql`, `User.java`, `ServiceItem.java`, `Order.java`, `Transaction.java`
  - 验证方式：执行 `mvn test`，Spring Boot 启动时能正常初始化数据库表结构。

- **步骤 1.2：基础服务接口实现 (Service & Controller)**
  - 涉及文件：实现登录/注册、服务列表、下单、支付(余额)、提成结算的核心API。
  - 验证方式：通过写 Controller 层的 MockMvc 单元测试验证核心链路（注册 -> 充值 -> 下单 -> 提成结算）。

## 阶段二：后台管理端 (Admin Web)
- **步骤 2.1：基础页面骨架与路由**
  - 涉及文件：`apps/admin-web/src/router/index.ts`, `views/ServiceManage.vue`, `views/OrderManage.vue`
  - 验证方式：`npm run dev` 后可访问服务管理与订单管理空页面。

- **步骤 2.2：对接后端API并实现数据管理**
  - 涉及文件：`packages/api-client/src/admin.ts`, 及上述Vue页面
  - 验证方式：能在后台新建一个“美容服务”，并在前端页面看到列表；能查看到订单状态与分销提成流水。

## 阶段三：C端小程序 (Miniapp)
- **步骤 3.1：用户体系与服务浏览**
  - 涉及文件：`apps/miniapp/src/pages/index/index.vue`, `pages/user/login.vue`
  - 验证方式：小程序编译运行，能够展示后台配置的服务列表，能够带参数（referrer）注册并正确绑定推荐人。

- **步骤 3.2：充值、购买与分销提成闭环**
  - 涉及文件：`apps/miniapp/src/pages/order/index.vue`, `pages/user/wallet.vue`
  - 验证方式：完整体验C端链路：充值 -> 下单余额支付 -> 分享者获得提成，检查数据库和页面显示。
