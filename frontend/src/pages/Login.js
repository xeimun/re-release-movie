import {useState, useContext} from "react";
import axios from "axios";
import {AuthContext} from "../context/AuthContext";

const Login = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const {login} = useContext(AuthContext);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/auth/login", {email, password});
            login(response.data.token);
            setMessage("로그인 성공:)");
        } catch (error) {
            setMessage(error.response?.data?.message || "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
    };

    return (
        <div className="flex justify-center items-center min-h-screen bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-md w-96">
                <h2 className="text-2xl font-bold mb-4 text-center">로그인</h2>
                <form onSubmit={handleLogin} className="flex flex-col space-y-4">
                    <input
                        type="email"
                        placeholder="이메일"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        className="border p-2 rounded w-full"
                    />
                    <input
                        type="password"
                        placeholder="비밀번호"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className="border p-2 rounded w-full"
                    />
                    <button
                        type="submit"
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700 w-full"
                    >
                        로그인
                    </button>
                </form>
                {message && <p className="text-red-500 mt-2 text-center">{message}</p>}
            </div>
        </div>
    );
};

export default Login;
