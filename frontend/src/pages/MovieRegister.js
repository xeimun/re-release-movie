import React, {useState} from "react";
import axios from "../api/axiosInstance";
import MovieSearch from "../components/MovieSearch";

const MovieRegister = () => {
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [message, setMessage] = useState("");
    const [messageType, setMessageType] = useState("");  // 메시지 타입 추가

    const handleMovieSelect = (movie) => {
        setSelectedMovie(movie);
    };

    const handleRegister = async () => {
        if (!selectedMovie) return;

        try {
            const response = await axios.post("/api/alerts/register", {
                title: selectedMovie.title,
                releaseYear: selectedMovie.release_date.split("-")[0],
                tmdbId: selectedMovie.id,
            });

            if (response.status === 200) {  // 성공적으로 등록된 경우
                setMessage(`${selectedMovie.title}이(가) 성공적으로 등록되었습니다.`);
                setMessageType("success");  // 성공 타입 설정
                setSelectedMovie(null);

                setTimeout(() => setMessage(""), 3000);  // 메시지를 3초 후에 초기화
            }
        } catch (error) {
            if (error.response) {
                switch (error.response.status) {
                    case 409: // 이미 등록된 영화
                        setMessage("이미 등록된 영화입니다.");
                        setMessageType("error");
                        break;
                    case 401: // 인증되지 않은 사용자 (로그인 필요)
                        setMessage("로그인이 필요합니다.");
                        setMessageType("error");
                        break;
                    default: // 기타 오류
                        setMessage("영화 등록에 실패했습니다. 다시 시도해주세요.");
                        setMessageType("error");
                        break;
                }
            } else {
                setMessage("서버와의 통신에 문제가 발생했습니다.");
                setMessageType("error");
            }
            console.error("Error registering movie:", error);

            setTimeout(() => setMessage(""), 3000);  // 메시지를 3초 후에 초기화
        }
    };

    return (
        <div className="p-6 bg-gray-900 text-white rounded-lg shadow-lg">
            <MovieSearch onSelectMovie={handleMovieSelect}/>

            {selectedMovie && (
                <div className="mt-4 p-4 bg-gray-800 rounded-lg">
                    <h3 className="text-xl font-semibold">선택한 영화:</h3>
                    <p>{selectedMovie.title} ({selectedMovie.release_date?.split("-")[0]})</p>
                    <button
                        onClick={handleRegister}
                        className="mt-3 bg-green-500 px-4 py-2 text-white rounded-lg hover:bg-green-700"
                    >
                        재개봉 알림 신청
                    </button>
                </div>
            )}

            {message && (
                <p className={`mt-3 ${messageType === "success" ? "text-green-400" : "text-red-400"}`}>
                    {message}
                </p>
            )}
        </div>
    );
};

export default MovieRegister;
