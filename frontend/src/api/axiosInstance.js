import axios from "axios";

// API 기본 URL 설정
const API_URL = "http://localhost:8080";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});

// 요청 인터셉터: 모든 요청에 JWT 자동 포함
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 응답 인터셉터: 만료된 토큰 처리
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            console.error("인증 실패: 로그아웃 처리");
            localStorage.removeItem("token");
            window.location.href = "/login"; // 로그인 페이지로 이동
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;
