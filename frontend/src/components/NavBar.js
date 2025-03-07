import {useContext} from "react";
import {AuthContext} from "../context/AuthContext";
import {Link} from "react-router-dom";

const NavBar = () => {
    const {isAuthenticated, logout} = useContext(AuthContext); // ✅ 로그인 상태 가져오기

    return (
        <nav>
            <h2>🎬 ReRelease Movie</h2>
            <ul>
                {!isAuthenticated ? (
                    <>
                        <li><Link to="/signup">회원가입</Link></li>
                        <li><Link to="/login">로그인</Link></li>
                    </>
                ) : (
                    <>
                        <li><Link to="/me">내 정보</Link></li>
                        <li>
                            <button onClick={logout}>로그아웃</button>
                        </li>
                    </>
                )}
            </ul>
        </nav>
    );
};

export default NavBar;
