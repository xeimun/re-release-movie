import React from "react";

const LoadingMessage = ({message = "로딩 중입니다..."}) => (
    <div className="flex justify-center items-center col-span-full text-gray-500 text-sm">
        {/* Tailwind 기반 회전 스피너 */}
        <svg
            className="w-5 h-5 mr-2 animate-spin"
            fill="none"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
        >
            <circle
                className="opacity-25"
                cx="12"
                cy="12"
                r="10"
                stroke="currentColor"
                strokeWidth="4"
            ></circle>
            <path
                className="opacity-75"
                fill="currentColor"
                d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"
            ></path>
        </svg>
        {message}
    </div>
);

export default LoadingMessage;
