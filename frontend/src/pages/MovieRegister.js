import React, {useState} from "react";
import axios from "../api/axiosInstance";
import MovieSearch from "../components/MovieSearch";

const MovieRegister = () => {
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [message, setMessage] = useState("");

    const handleMovieSelect = (movie) => {
        setSelectedMovie(movie);
    };

    const handleRegister = async () => {
        if (!selectedMovie) return;

        try {
            await axios.post("/movies", {
                title: selectedMovie.title,
                releaseYear: selectedMovie.release_date.split("-")[0],
                tmdbId: selectedMovie.id,
            });

            setMessage(`"${selectedMovie.title}"이(가) 등록되었습니다.`);
            setSelectedMovie(null);
        } catch (error) {
            setMessage("영화 등록에 실패했습니다.");
            console.error("Error registering movie:", error);
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

            {message && <p className="text-yellow-400 mt-3">{message}</p>}
        </div>
    );
};

export default MovieRegister;
