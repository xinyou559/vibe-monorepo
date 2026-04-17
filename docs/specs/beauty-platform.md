# 美业服务平台功能规格 (Beauty Platform)

## CAPABILITY
构建一个完整的美业服务闭环系统，包含面向C端的微信小程序（提供服务浏览、会员充值、购买、订单查询及分销裂变能力）以及面向B端的后台管理系统（提供服务项目管理、订单处理、会员管理及分销提成结算能力）。系统上线后，门店可实现在线化服务售卖与基于老带新的业绩增长。

## CONSTRAINTS
- **分销裂变规则**：新客通过分享链接注册并完成首次消费后，分享者（老客）才能获得提成收入。
- **资金与合规**：会员充值和购买服务涉及真实支付（MVP阶段先做模拟余额支付），提成收入发放到余额，需有严格的流水记录（资金不变性）。
- **数据归属**：订单必须关联到具体用户与具体服务，分销绑定关系一经确认（新客注册时绑定）不可随意篡改。

## IMPLEMENTATION CONTRACT
### 1. Actors (参与者)
- **C端用户**：浏览、充值、购买、分享、查看收益。
- **B端管理员**：管理服务项目、处理订单、查看流水。

### 2. Surfaces (交互端)
- `apps/miniapp`：首页(服务列表)、服务详情页、下单支付页、我的(订单/余额/分享海报)。
- `apps/admin-web`：服务管理、订单管理、用户与分销管理、资金流水。

### 3. States and Transitions (核心状态机)
- **订单状态**：`INIT`(待支付) → `PAID`(已支付/待服务) → `COMPLETED`(已完成) | `CANCELLED`(已取消)。
- **提成状态**：`PENDING`(订单支付后待结算) → `SETTLED`(订单完成后结算到余额)。

### 4. Interface & Data Implications (接口与数据模型)
#### 数据模型 (ER)
- `users`: id, openid, phone, balance, referrer_id (推荐人)
- `services`: id, name, description, price, commission_rate (提成比例), status
- `orders`: id, user_id, service_id, amount, status, created_at
- `transactions`: id, user_id, amount, type (RECHARGE/CONSUME/COMMISSION), related_id

#### 核心接口 (API)
- `POST /api/v1/auth/login` (用户注册/登录，含referrer_id入参)
- `GET /api/v1/services` (浏览服务)
- `POST /api/v1/orders` (购买服务下单)
- `POST /api/v1/recharge` (充值会员)
- `GET /api/v1/users/commission` (查看分销提成流水)

#### DTO & 错误码
- 统一返回体 `Result<T>`
- 错误码：`USER_NOT_FOUND` (用户不存在), `BALANCE_INSUFFICIENT` (余额不足), `ORDER_STATUS_ERROR` (订单状态错误)

## NON-GOALS
- 暂不实现技师排班与预约时间段管理（MVP阶段只卖服务凭证）。
- 暂不实现多门店隔离（按单门店架构设计）。
- 暂不实现实物商品的物流发货功能。

## OPEN QUESTIONS
1. 提成是按固定金额还是按服务价格的百分比？（假设：MVP按固定百分比设计）。
2. 会员充值是否有赠送金额？（假设：MVP阶段按1:1充值，无赠送梯度）。
3. 微信支付是直接调起真实支付还是MVP阶段先做模拟余额支付？（假设：优先实现余额支付，微信支付留作后续模块）。

## 验收标准 (Acceptance Criteria)
- [ ] Given 两个用户A和B，When A分享链接给B，B通过链接注册，Then B的 `referrer_id` 是 A。
- [ ] Given B充值了100元，When B购买了50元的服务并完成订单，Then A的余额增加相应的提成金额，生成对应的Transaction。
- [ ] Given B的订单在 `INIT` 状态，When 余额不足，Then 购买失败，提示 `BALANCE_INSUFFICIENT`。
- [ ] Given B端管理员，When 登录后台，Then 能看到上述的订单与分销流水记录。
