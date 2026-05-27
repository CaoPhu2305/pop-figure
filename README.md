# PopFigure

Dự án web bán mô hình với kiến trúc tách lớp:
- Frontend: Vue 3 + TypeScript + Vite + Vuetify
- Backend: Spring Boot + Spring Security + JPA + SQL Server

## Cấu trúc thư mục

- `frontend/wibu-shop-main`: ứng dụng giao diện người dùng
- `backend/popfigure`: REST API và xử lý nghiệp vụ

## Yêu cầu môi trường

- Node.js 22+ và npm
- Java 17+
- SQL Server (mặc định cổng `1433`)

## Cấu hình môi trường

### 1) Frontend

File: `frontend/wibu-shop-main/.env`

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### 2) Backend

Backend đang đọc biến môi trường từ file `backend/popfigure/.env` và `application.yml`.

Ví dụ:

```env
DB_USERNAME=sa
DB_PASSWORD=your_password
```

Giá trị datasource mặc định trong `application.yml`:
- `DB_URL=jdbc:sqlserver://localhost:1433;databaseName=PopFigureDB;encrypt=true;trustServerCertificate=true`
- `SERVER_PORT=8080`
- `server.servlet.context-path=/api`

## Chạy dự án

Mở 2 terminal riêng cho frontend và backend.

### 1) Chạy backend

```bash
cd backend/popfigure
./mvnw spring-boot:run
```

API mặc định: `http://localhost:8080/api`

### 2) Chạy frontend

```bash
cd frontend/wibu-shop-main
npm install
npm run dev
```

Frontend mặc định (Vite): `http://localhost:5173`

## Script hữu ích

### Frontend (`frontend/wibu-shop-main`)

- `npm run dev`: chạy môi trường phát triển
- `npm run build`: build production
- `npm run preview`: chạy bản build local
- `npm run lint`: lint và auto-fix
- `npm run type-check`: kiểm tra TypeScript

### Backend (`backend/popfigure`)

- `./mvnw spring-boot:run`: chạy ứng dụng
- `./mvnw test`: chạy test
- `./mvnw clean package`: build file jar

## Một số endpoint chính

Base URL: `/api`

- `POST /auth/login`
- `POST /auth/introspect`
- `POST /auth/logout`
- `POST /auth/refresh`
- `POST /users`
- `GET /products`
- `GET /categories`
- `GET /orders/my`

## Ghi chú

- Frontend gửi JWT qua header `Authorization: Bearer <token>`.
- Cần đảm bảo SQL Server đã tạo database `PopFigureDB` trước khi chạy backend.
