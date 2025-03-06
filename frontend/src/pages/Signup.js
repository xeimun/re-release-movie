import {useState} from "react";
import axios from "axios";

const Signup = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [nickname, setNickname] = useState("");
    const [message, setMessage] = useState("");

    const handleSignup = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/api/auth/signup", {
                email,
                password,
                nickname,
            });
            setMessage(response.data.message);
        } catch (error) {
            setMessage(error.response?.data?.message || "회원가입 실패");
        }
    };

    return (
        <div>
            <h2>회원가입</h2>
            <form onSubmit={handleSignup}>
                <input type="email" placeholder="이메일" value={email} onChange={(e) => setEmail(e.target.value)}
                       required/>
                <input type="password" placeholder="비밀번호" value={password} onChange={(e) => setPassword(e.target.value)}
                       required/>
                <input type="text" placeholder="닉네임" value={nickname} onChange={(e) => setNickname(e.target.value)}
                       required/>
                <button type="submit">회원가입</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default Signup;
