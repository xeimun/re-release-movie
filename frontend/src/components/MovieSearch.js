import React, {useState} from "react";
import {searchMovies} from "../api/movieApi";

const MovieSearch = ({onSelectMovie}) => {
    const [query, setQuery] = useState(""); // 검색어 상태
    const [movies, setMovies] = useState([]); // 검색 결과 상태
    const [loading, setLoading] = useState(false); // 로딩 상태

    const handleSearch = async () => {
        if (query.trim() === "") return; // 빈 검색어 방지
        setLoading(true);
        const results = await searchMovies(query); // TMDB API 호출
        setMovies(results);
        setLoading(false);
    };

    return (
        <div className="p-4 bg-gray-900 text-white rounded-lg shadow-md">
            <h2 className="text-xl font-bold mb-3">영화 검색</h2>
            <div className="flex">
                <input
                    type="text"
                    placeholder="영화 제목을 입력하세요..."
                    className="w-full p-2 text-black rounded-l-lg focus:outline-none"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />
                <button
                    onClick={handleSearch}
                    className="bg-blue-500 px-4 py-2 text-white rounded-r-lg hover:bg-blue-700"
                >
                    검색
                </button>
            </div>

            {loading && <p className="text-yellow-400 mt-3">검색 중...</p>}

            <ul className="mt-4 space-y-3">
                {movies.map((movie) => (
                    <li
                        key={movie.id}
                        className="flex items-center bg-gray-800 p-2 rounded-md cursor-pointer hover:bg-gray-700"
                        onClick={() => onSelectMovie(movie)}
                    >
                        <img
                            src={`https://image.tmdb.org/t/p/w200${movie.poster_path}`}
                            alt={movie.title}
                            className="w-12 h-16 mr-3 rounded"
                        />
                        <span className="text-lg font-semibold">
              {movie.title} ({movie.release_date?.split("-")[0]})
            </span>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MovieSearch;
