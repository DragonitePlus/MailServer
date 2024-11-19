package com.example.mailserver.entity;

import lombok.Getter;
import lombok.Setter;

// 辅助类，用于封装响应数据
@Setter
@Getter
public class Response {
        private String message;
        private String token;

        public Response(String message) {
            this.message = message;
        }

        public Response(String message, String token) {
            this.message = message;
            this.token = token;
        }

}