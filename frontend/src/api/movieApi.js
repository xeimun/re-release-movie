import axios from "./axiosInstance";

/**
 * TMDB 영화 검색 API 호출 (페이지 포함)
 * @param {string} query 검색할 영화 제목
 * @param {number} page 페이지 번호 (1부터 시작)
 * @returns {Promise<{results: Array, total_pages: number}>}
 */
export const searchMovies = async (query, page = 1) => {
    try {
        const response = await axios.get("/api/tmdb/search", {
            params: {query, page}
        });
        return response.data;
    } catch (error) {
        console.error("영화 검색 오류", error);
        return {results: [], total_pages: 1};
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
