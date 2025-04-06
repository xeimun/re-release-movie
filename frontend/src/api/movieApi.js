import axios from "./axiosInstance";

/**
 * TMDB 영화 검색 API 호출
 * @param {string} query 검색할 영화 제목
 * @returns {Promise<Array>} 검색된 영화 목록 (배열)
 */
export const searchMovies = async (query) => {
    try {
        const response = await axios.get("/api/tmdb/search", {params: {query}});
        return response.data.results; // TMDB API의 `results` 배열 반환
    } catch (error) {
        console.error("영화 검색 오류", error);
        return []; // 오류 발생 시 빈 배열 반환
    }
};

export const getUserAlerts = async () => {
    const response = await axios.get("/api/alerts/my-alerts");
    return response.data;
};

export const deleteUserAlert = async (alertId) => {
    const response = await axios.delete(`/api/alerts/${alertId}`);
    return response.data;
};
