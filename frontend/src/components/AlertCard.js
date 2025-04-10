import React from "react";

const AlertCard = ({alert, isSelected, onSelect, onDelete}) => {
    const handleCardClick = (e) => {
        // 버튼 클릭 시 카드 선택 해제 방지
        if (e.target.closest("button")) return;

        if (isSelected) {
            onSelect(null); // 선택 해제
        } else {
            onSelect(alert); // 새로 선택
        }
    };

    return (
        <div
            className="relative rounded-lg overflow-hidden shadow hover:shadow-lg transform transition cursor-pointer"
            onClick={handleCardClick}
        >
            {/* 포스터 이미지 */}
            {alert.posterPath ? (
                <img
                    src={`https://image.tmdb.org/t/p/w500${alert.posterPath}`}
                    alt={alert.movieTitle}
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

            {/* 텍스트 정보 */}
            <div className="p-3 text-left bg-white">
                <h3
                    className="font-semibold text-base truncate"
                    title={alert.movieTitle}
                >
                    {alert.movieTitle}
                </h3>
                <p className="text-sm text-gray-500">
                    등록일: {new Date(alert.registeredAt).toLocaleDateString()}
                </p>
            </div>

            {/* 선택된 경우: 삭제 버튼 */}
            {isSelected && (
                <div className="absolute inset-0 flex flex-col items-center justify-center bg-black bg-opacity-40 px-2">
                    <button
                        onClick={() => onDelete(alert.userMovieAlertId)}
                        className="px-6 py-2 bg-red-500 hover:bg-red-600 text-white font-semibold rounded-full shadow-md transition"
                    >
                        🗑️ 삭제
                    </button>
                </div>
            )}
        </div>
    );
};

export default AlertCard;
