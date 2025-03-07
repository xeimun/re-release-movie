import React from "react";
import ReactDOM from "react-dom/client";
import {BrowserRouter} from "react-router-dom"; // ✅ BrowserRouter 추가
import App from "./App";
import {AuthProvider} from "./context/AuthContext";

ReactDOM.createRoot(document.getElementById("root")).render(
    <BrowserRouter> {/* ✅ BrowserRouter는 index.js에서 한 번만 감쌈 */}
        <AuthProvider>
            <App/>
        </AuthProvider>
    </BrowserRouter>
);
