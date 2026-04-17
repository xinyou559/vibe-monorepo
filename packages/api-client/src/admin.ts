import { Result, ServiceItem, Order, Transaction, User } from 'shared-types';

const BASE_URL = '/api/v1';

export class AdminApiClient {
  private async request<T>(endpoint: string, options?: RequestInit): Promise<T> {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options?.headers,
      },
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    const result: Result<T> = await response.json();
    if (result.code !== 200) {
      throw new Error(result.message || 'API request failed');
    }
    
    return result.data;
  }

  // 服务管理
  async getServices(): Promise<ServiceItem[]> {
    return this.request<ServiceItem[]>('/services');
  }

  async createService(service: Partial<ServiceItem>): Promise<ServiceItem> {
    return this.request<ServiceItem>('/admin/services', {
      method: 'POST',
      body: JSON.stringify(service),
    });
  }

  async updateService(id: number, service: Partial<ServiceItem>): Promise<ServiceItem> {
    return this.request<ServiceItem>(`/admin/services/${id}`, {
      method: 'PUT',
      body: JSON.stringify(service),
    });
  }

  // 订单管理
  async getOrders(): Promise<Order[]> {
    return this.request<Order[]>('/admin/orders');
  }

  async completeOrder(id: number): Promise<void> {
    return this.request<void>(`/admin/orders/${id}/complete`, {
      method: 'POST',
    });
  }

  // 用户与资金
  async getUsers(): Promise<User[]> {
    return this.request<User[]>('/admin/users');
  }

  async getTransactions(): Promise<Transaction[]> {
    return this.request<Transaction[]>('/admin/transactions');
  }
}

export const adminApi = new AdminApiClient();