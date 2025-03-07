import {useContext, useState} from "react";
import {AuthContext} from "../context/AuthContext";
import {Link} from "react-router-dom";

const NavBar = () => {
    const {isAuthenticated, logout} = useContext(AuthContext);
    const [menuOpen, setMenuOpen] = useState(false);

    return (
        <nav className="bg-gray-800 text-white p-4">
            <div className="container mx-auto flex justify-between items-center">
                <Link to="/" className="text-2xl font-bold hover:text-gray-300">
                    🎬 ReRelease Movie
                </Link>

                {/* 모바일 메뉴 버튼 */}
                <button
                    className="md:hidden text-white focus:outline-none"
                    onClick={() => setMenuOpen(!menuOpen)}
                >
                    ☰
                </button>

                {/* 데스크톱 & 모바일 메뉴 */}
                <ul className={`md:flex space-x-4 ${menuOpen ? "block" : "hidden"} md:block`}>
                    {!isAuthenticated ? (
                        <>
                            <li><Link to="/signup" className="hover:text-gray-300 block md:inline">회원가입</Link></li>
                            <li><Link to="/login" className="hover:text-gray-300 block md:inline">로그인</Link></li>
                        </>
                    ) : (
                        <>
                            <li><Link to="/me" className="hover:text-gray-300 block md:inline">내 정보</Link></li>
                            <li>
                                <button
                                    onClick={logout}
                                    className="hover:text-gray-300 block md:inline"
                                >
                                    로그아웃
                                </button>
                            </li>
                        </>
                    )}
                </ul>
            </div>
        </nav>
    );
};

export default NavBar;
