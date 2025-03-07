import {useState, useContext} from "react";
import axios from "axios";
import {AuthContext} from "../context/AuthContext";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const {login} = useContext(AuthContext); // ✅ 로그인 상태 업데이트

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/auth/login", {email, password});
            login(response.data.token); // ✅ 로그인 후 상태 업데이트 및 페이지 이동
            setMessage("로그인 성공!");
        } catch (error) {
            setMessage(error.response?.data?.message || "로그인 실패");
        }
    };

    return (
        <div>
            <h2>로그인</h2>
            <form onSubmit={handleLogin}>
                <input type="email" placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)}
                       required/>
                <input type="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)}
                       required/>
                <button type="submit">로그인</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default Login;
