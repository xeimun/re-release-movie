import React, {useEffect, useRef, useState} from "react";
import {useLocation} from "react-router-dom";
import SearchBar from "../components/SearchBar";
import MovieCard from "../components/MovieCard";
import LoadingMessage from "../components/LoadingMessage";
import {searchMovies} from "../api/movieApi";

const Main = () => {
    const [query, setQuery] = useState("");
    const [movies, setMovies] = useState([]);
    const [hasSearched, setHasSearched] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [loading, setLoading] = useState(false);
    const location = useLocation();
    const cardAreaRef = useRef(null);

    // 검색 실행
    const handleSearch = async () => {
        if (!query.trim()) return;

        setHasSearched(true);
        setSelectedMovie(null);
        setLoading(true);

        try {
            const results = await searchMovies(query);
            setMovies(results || []);
        } catch (e) {
            console.error("영화 검색 실패:", e);
            setMovies([]);
        } finally {
            setLoading(false);
        }
    };

    // 홈 이동 시 상태 초기화
    useEffect(() => {
        if (location.pathname === "/") {
            setQuery("");
            setMovies([]);
            setHasSearched(false);
            setSelectedMovie(null);
            setLoading(false);
        }
    }, [location.pathname]);

    // 카드 외부 클릭 시 선택 해제
    useEffect(() => {
        const handleClickOutside = (e) => {
            if (
                cardAreaRef.current &&
                !cardAreaRef.current.contains(e.target)
            ) {
                setSelectedMovie(null);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    return (
        <main className="px-4 py-16 text-center bg-white">
            <h1 className="text-4xl font-bold mb-8">영화 (재)개봉 알림</h1>

            <div className="flex flex-col items-center">
                <SearchBar query={query} setQuery={setQuery} onSearch={handleSearch}/>

                <div
                    ref={cardAreaRef}
                    className="mt-10 w-full max-w-5xl grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 px-2"
                >
                    {/* 검색 후 로딩 중일 때 */}
                    {hasSearched && loading && <LoadingMessage message="검색 중입니다..."/>}

                    {/* 검색 결과 없음 */}
                    {hasSearched && !loading && movies.length === 0 && (
                        <p className="text-gray-500 text-sm col-span-full">
                            검색 결과가 없습니다.
                        </p>
                    )}

                    {/* 검색 결과 표시 */}
                    {movies.map((movie) => (
                        <MovieCard
                            key={movie.id}
                            movie={movie}
                            isSelected={selectedMovie?.id === movie.id}
                            onSelect={(selected) => setSelectedMovie(selected)}
                        />
                    ))}
                </div>
            </div>
        </main>
    );
};

export default Main;
