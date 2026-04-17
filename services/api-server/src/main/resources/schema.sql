-- 数据库表结构 (beauty_platform)

-- 用户表：存储小程序用户信息及余额、推荐人关系
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    openid VARCHAR(100) UNIQUE NOT NULL, -- 微信 openid，作为唯一标识
    phone VARCHAR(20),                   -- 手机号（可选）
    balance DECIMAL(10, 2) DEFAULT 0.00, -- 账户余额
    referrer_id INT REFERENCES users(id),-- 推荐人ID（关联自己，为空表示无推荐人）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 注册时间
);

-- 服务项目表：存储美业可购买的服务
CREATE TABLE IF NOT EXISTS service_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,          -- 服务名称
    description TEXT,                    -- 服务描述/详情
    price DECIMAL(10, 2) NOT NULL,       -- 售价
    commission_rate DECIMAL(5, 2) DEFAULT 0.00, -- 提成比例（例如 0.10 表示 10%）
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 状态：ACTIVE/INACTIVE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 订单表：存储用户购买服务的记录
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),       -- 购买用户
    service_id INT NOT NULL REFERENCES service_items(id), -- 购买的服务
    amount DECIMAL(10, 2) NOT NULL,                  -- 实际支付金额
    status VARCHAR(20) DEFAULT 'INIT',               -- 状态：INIT(待支付), PAID(已支付), COMPLETED(已完成), CANCELLED(已取消)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 资金流水表：记录所有余额变动及提成明细
CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),       -- 资金变动所属用户
    amount DECIMAL(10, 2) NOT NULL,                  -- 变动金额（正数为增加，负数为扣减）
    type VARCHAR(20) NOT NULL,                       -- 变动类型：RECHARGE(充值), CONSUME(消费), COMMISSION(提成)
    related_id INT,                                  -- 关联业务ID（如对应的 order_id）
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
