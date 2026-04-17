export interface User {
  id: number;
  openid: string;
  phone?: string;
  balance: number;
  referrerId?: number;
  createdAt: string;
}

export interface ServiceItem {
  id: number;
  name: string;
  description?: string;
  price: number;
  commissionRate: number;
  status: 'ACTIVE' | 'INACTIVE';
  createdAt: string;
}

export interface Order {
  id: number;
  userId: number;
  serviceId: number;
  amount: number;
  status: 'INIT' | 'PAID' | 'COMPLETED' | 'CANCELLED';
  createdAt: string;
}

export interface Transaction {
  id: number;
  userId: number;
  amount: number;
  type: 'RECHARGE' | 'CONSUME' | 'COMMISSION';
  relatedId?: number;
  createdAt: string;
}

export interface Result<T = any> {
  code: number;
  message: string;
  data: T;
}

export enum ErrorCode {
  SUCCESS = 200,
  BAD_REQUEST = 400,
  UNAUTHORIZED = 401,
  FORBIDDEN = 403,
  NOT_FOUND = 404,
  INTERNAL_SERVER_ERROR = 500,
  
  // Business logic errors
  USER_NOT_FOUND = 1001,
  BALANCE_INSUFFICIENT = 1002,
  ORDER_STATUS_ERROR = 1003,
  SERVICE_NOT_FOUND = 1004,
  SERVICE_UNAVAILABLE = 1005
}
