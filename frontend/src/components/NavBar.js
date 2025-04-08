// 📄 src/components/NavBar.js
import {useContext} from "react";
import {Link, useNavigate} from "react-router-dom";
import {AuthContext} from "../context/AuthContext";

const NavBar = () => {
    const {isAuthenticated, logout} = useContext(AuthContext);
    const navigate = useNavigate();

    const goHome = () => {
        navigate("/", {replace: true});
    };

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    const renderMenu = () => {
        if (!isAuthenticated) {
            return (
                <>
                    <Link to="/login" className="hover:text-blue-500">로그인</Link>
                    <Link to="/signup" className="hover:text-blue-500">회원가입</Link>
                </>
            );
        }
        return (
            <>
                <Link to="/me" className="hover:text-blue-500">내 정보</Link>
                <Link to="/alerts/manage" className="hover:text-blue-500">알림 관리</Link>
                <button onClick={handleLogout} className="hover:text-blue-500">로그아웃</button>
            </>
        );
    };

    return (
        <nav className="bg-white text-gray-800 py-3 shadow-sm">
            <div className="max-w-7xl mx-auto px-6 flex flex-col sm:flex-row justify-between items-center">
                {/* 좌측 로고 */}
                <button
                    onClick={goHome}
                    className="text-lg font-bold mb-2 sm:mb-0 hover:text-black"
                >
                    ReScreen
                </button>
                {/* 우측 메뉴 */}
                <div className="flex space-x-4 text-sm font-semibold">
                    {renderMenu()}
                </div>
            </div>
        </nav>
    );
};

export default NavBar;
