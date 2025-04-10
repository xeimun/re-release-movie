import React, {forwardRef} from "react";

const UpcomingCard = forwardRef(({movie, onClick}, ref) => {
    const {
        title,
        posterPath,
        releaseDate,
        voteAverage,
    } = movie;

    const formattedRating = voteAverage ? voteAverage.toFixed(1) : "0.0";
    const formattedDate = releaseDate || "미정";

    return (
        <div
            ref={ref}
            onClick={onClick}
            className="bg-white rounded-lg overflow-hidden shadow hover:shadow-lg cursor-pointer transition"
        >
            {/* 포스터 이미지 */}
            {posterPath ? (
                <img
                    src={`https://image.tmdb.org/t/p/w500${posterPath}`}
                    alt={`${title} 포스터`}
                    className="w-full h-72 object-cover"
                />
            ) : (
                <div
                    className="w-full h-72 flex items-center justify-center bg-gray-200 text-gray-600 font-bold text-lg">
                    NO IMAGE
                </div>
            )}

            {/* 텍스트 정보 */}
            <div className="p-3 text-left">
                <h3
                    className="font-semibold text-base truncate"
                    title={title}
                >
                    {title}
                </h3>
                <p className="text-sm text-gray-500">개봉일: {formattedDate}</p>
                <p className="text-sm text-yellow-600 font-semibold">
                    ⭐ {formattedRating}
                </p>
            </div>
        </div>
    );
});

UpcomingCard.displayName = "UpcomingCard";
export default UpcomingCard;
