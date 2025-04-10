import React from "react";

const MovieModal = ({isOpen, onClose, movie}) => {
    if (!isOpen || !movie) return null;

    return (
        <div
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
            onClick={onClose}
        >
            <div
                className="bg-white rounded-lg shadow-lg p-6 max-w-md w-11/12 text-left relative"
                onClick={(e) => e.stopPropagation()} // 내부 클릭 시 닫힘 방지
            >
                {/* 닫기 버튼 */}
                <button
                    onClick={onClose}
                    className="absolute top-2 right-2 text-gray-500 hover:text-black text-lg"
                >
                    ✖
                </button>

                <h2 className="text-xl font-bold mb-4">{movie.title}</h2>
                <p className="text-gray-700 text-sm whitespace-pre-line">
                    {movie.overview || "줄거리 정보가 없습니다."}
                </p>
            </div>
        </div>
    );
};

export default MovieModal;
