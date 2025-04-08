import React from "react";

const SearchBar = ({query, setQuery, onSearch}) => {
    const handleKeyDown = (e) => {
        if (e.key === "Enter" && onSearch) {
            onSearch();
        }
    };

    return (
        <div className="flex items-center border-2 border-gray-400 rounded-full px-4 py-2 w-full max-w-md shadow-sm">
            <input
                type="text"
                placeholder="예) 이웃집 토토로"
                className="flex-grow outline-none text-gray-600 placeholder-gray-400"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                onKeyDown={handleKeyDown}
                spellCheck={false}
            />
            <button onClick={onSearch}>
                <svg
                    className="w-5 h-5 text-gray-500"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth="2"
                        d="M21 21l-4.35-4.35m0 0A7.5 7.5 0 104.5 4.5a7.5 7.5 0 0012.15 12.15z"
                    />
                </svg>
            </button>
        </div>
    );
};

export default SearchBar;
