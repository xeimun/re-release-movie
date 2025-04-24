import {createContext, useState, useEffect, useContext, useCallback} from "react";
import {useNavigate} from "react-router-dom";

export const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    // logout 함수는 매번 새로 만들어지지 않도록 useCallback으로 고정
    const logout = useCallback(() => {
        localStorage.removeItem("token");
        setIsAuthenticated(false);
        navigate("/login");
    }, [navigate]);

    // 앱 시작 시 토큰 유효성 검사
    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token) {
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                const now = Math.floor(Date.now() / 1000);

                if (payload.exp && payload.exp < now) {
                    logout(); // 만료된 토큰이면 로그아웃
                } else {
                    setIsAuthenticated(true);
                }
            } catch (e) {
                console.error("토큰 파싱 오류:", e);
                logout(); // 비정상 토큰일 경우도 로그아웃
            }
        }
    }, [logout]); // useCallback된 logout을 의존성으로 추가

    const login = (token) => {
        localStorage.setItem("token", token);
        setIsAuthenticated(true);
        navigate("/");
    };

    return (
        <AuthContext.Provider value={{isAuthenticated, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
