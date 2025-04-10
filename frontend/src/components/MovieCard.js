import React, {useContext, useState} from "react";
import {AuthContext} from "../context/AuthContext";
import {useNavigate} from "react-router-dom";
import axios from "../api/axiosInstance";

const MovieCard = ({movie, isSelected, onSelect}) => {
    const {isAuthenticated} = useContext(AuthContext);
    const navigate = useNavigate();

    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const handleCardClick = (e) => {
        if (e.target.closest("button")) return;

        if (isSelected) {
            onSelect(null);
        } else {
            onSelect(movie);
        }
    };

    const handleRegister = async (e) => {
        e.stopPropagation();

        if (!isAuthenticated) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        try {
            const response = await axios.post("/api/alerts/register", {
                tmdbId: movie.id,
                title: movie.title,
                posterPath: movie.poster_path,
                releaseYear: movie.release_date.split("-")[0],
            });

            if (response.status === 200) {
                setSuccessMessage("✅ 등록 완료되었습니다.");
                setErrorMessage("");
                setTimeout(() => setSuccessMessage(""), 3000);
            }
        } catch (error) {
            setSuccessMessage(""); // 성공 메시지 초기화
            if (error.response) {
                switch (error.response.status) {
                    case 409:
                        setErrorMessage("⚠️ 이미 등록된 영화입니다.");
                        break;
                    case 401:
                        setErrorMessage("⚠️ 로그인이 필요합니다.");
                        break;
                    default:
                        setErrorMessage("⚠️ 등록 실패. 다시 시도해주세요.");
                }
            } else {
                setErrorMessage("⚠️ 서버 연결 오류입니다.");
            }
            setTimeout(() => setErrorMessage(""), 3000);
        }
    };

    return (
        <div
            className="relative rounded-lg overflow-hidden shadow hover:shadow-lg transform transition cursor-pointer"
            onClick={handleCardClick}
        >
            {/* 포스터 이미지 또는 대체 이미지 */}
            {movie.poster_path ? (
                <img
                    src={`https://image.tmdb.org/t/p/w500${movie.poster_path}`}
                    alt={movie.title}
                    className={`w-full h-72 object-cover transition ${
                        isSelected ? "brightness-75" : ""
                    }`}
                />
            ) : (
                <div
                    className={`w-full h-72 flex items-center justify-center bg-gray-200 text-gray-600 font-bold text-lg tracking-wide transition ${
                        isSelected ? "brightness-75" : ""
                    }`}
                >
                    NO IMAGE
                </div>
            )}

            {/* 영화 정보 텍스트 */}
            <div className="p-3 text-left bg-white">
                <h3 className="font-semibold text-base truncate" title={movie.title}>
                    {movie.title}
                </h3>
                <p className="text-sm text-gray-500">
                    개봉일: {movie.release_date?.split("-")[0]}
                </p>
            </div>

            {/* 알림 등록 버튼 및 메시지 */}
            {isSelected && (
                <div className="absolute inset-0 flex flex-col items-center justify-center bg-black bg-opacity-40 px-2">
                    <button
                        onClick={handleRegister}
                        className="px-6 py-2 bg-amber-400 hover:bg-amber-500 text-white font-semibold rounded-full shadow-md transition"
                    >
                        🔔 알림 등록
                    </button>

                    {successMessage && (
                        <div
                            className="mt-3 px-4 py-2 rounded text-sm font-semibold shadow-md bg-green-100 text-green-800">
                            {successMessage}
                        </div>
                    )}

                    {errorMessage && (
                        <div className="mt-3 px-4 py-2 rounded text-sm font-semibold shadow-md bg-red-100 text-red-800">
                            {errorMessage}
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default MovieCard;
