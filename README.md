# 📌 Event Management System

## 📖 Giới thiệu  
**Event Management System** là một ứng dụng web backend được xây dựng bằng **Spring Boot** giúp quản lý sự kiện, hỗ trợ người dùng đăng ký, quản lý và tham gia sự kiện.  
Hệ thống có phân quyền **Admin** và **User**, tích hợp **Spring Security + JWT** để xác thực và bảo mật API. Ngoài ra, dự án cũng tích hợp **Swagger UI** để cung cấp tài liệu API trực quan.  

---

## 🚀 Công nghệ sử dụng  
- **Java 17**  
- **Spring Boot 3.x**  
  - Spring Web  
  - Spring Security (JWT Authentication & Authorization)  
  - Spring Data JPA (Hibernate)  
- **MySQL** (database)  
- **Swagger (Springdoc OpenAPI)** – Tài liệu API  
- **Maven** (quản lý dependency)  

---

## ⚙️ Các tính năng chính  
- 👤 **Authentication & Authorization**  
  - Đăng ký / Đăng nhập người dùng  
  - Xác thực bằng **JWT**  
  - Phân quyền: **Admin** & **User**  

- 🎉 **Quản lý sự kiện**  
  - Tạo sự kiện (Admin)  
  - Chỉnh sửa / Xóa sự kiện (Admin)  
  - Xem danh sách sự kiện (User & Admin)  
  - Đăng ký tham gia sự kiện (User)  

- 📑 **API Documentation**  
  - Swagger UI tại: `http://localhost:8080/swagger-ui.html`  

---

