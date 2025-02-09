import React, { useEffect, useState } from 'react';
import axios from 'axios';

const MovieList = () => {
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        axios.get('http://localhost:8080/api/movies') // Spring Boot API 호출
            .then(response => {
                setMovies(response.data); // 가져온 데이터를 상태에 저장
            })
            .catch(error => {
                console.error("Error fetching data:", error);
            });
    }, []);

    return (
        <div>
            <h2>🎬 재개봉 영화 목록</h2>
            <ul>
                {movies.map(movie => (
                    <li key={movie.id}>{movie.title} ({movie.year})</li>
                ))}
            </ul>
        </div>
    );
};

export default MovieList;