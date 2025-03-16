import axios from "./axiosInstance";

/**
 * TMDB 영화 검색 API 호출
 * @param {string} query 검색할 영화 제목
 * @returns {Promise<Array>} 검색된 영화 목록 (배열)
 */
export const searchMovies = async (query) => {
    try {
        // 백엔드 API에 검색 요청을 보냄
        const response = await axios.get("/api/tmdb/search", {params: {query}});

        // TMDB API의 검색 결과 리스트 반환
        return response.data.results; // TMDB API의 `results` 배열 반환
    } catch (error) {
        console.error("영화 검색 오류", error);
        return []; // 오류 발생 시 빈 배열 반환
    }
};
