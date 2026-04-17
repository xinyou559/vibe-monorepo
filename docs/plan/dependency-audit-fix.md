# 修复 apps/miniapp NPM 漏洞

## 步骤与验证项

- [x] **步骤 1**：检查当前漏洞情况
  - 验证项：运行 `npm audit` 记录初始状态（有 61 个安全漏洞，含 36 个高危）。
- [x] **步骤 2**：使用自动修复功能，对不产生 breaking changes 的部分执行修复。
  - 验证项：运行 `npm audit fix`。
- [x] **步骤 3**：通过 package.json 强制锁定 (overrides) 那些未直接引用的深层高危依赖（如 `esbuild`, `vue-i18n`, `cookie`, `qs` 等）。
  - 验证项：配置 `overrides` 后，运行 `npm install`。
- [x] **步骤 4**：新增一条 `test:audit` 命令，确保未来只会因为高危漏洞（`--audit-level=high`）拦截，而允许框架必须的中危/低危漏洞存在。
  - 验证项：运行 `npm run test:audit`，状态码应为 0。
- [x] **步骤 5**：确保修复没有破坏小程序的编译构建。
  - 验证项：运行 `npm run build:h5`，构建成功。
