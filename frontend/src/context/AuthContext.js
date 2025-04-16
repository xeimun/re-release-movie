import {createContext, useState, useEffect, useContext} from "react";
import {useNavigate} from "react-router-dom";

export const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token) {
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                const now = Math.floor(Date.now() / 1000);

                if (payload.exp && payload.exp < now) {
                    logout(); // 토큰이 만료됨
                } else {
                    setIsAuthenticated(true); // 유효한 토큰
                }
            } catch (e) {
                console.error("토큰 파싱 실패:", e);
                logout(); // 잘못된 토큰 → 로그아웃 처리
            }
        } else {
            setIsAuthenticated(false); // 토큰 없음
        }
    }, []);

    const login = (token) => {
        localStorage.setItem("token", token);
        setIsAuthenticated(true);
        navigate("/");
    };

    const logout = () => {
        localStorage.removeItem("token");
        setIsAuthenticated(false);
        navigate("/login");
    };

    return (
        <AuthContext.Provider value={{isAuthenticated, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
