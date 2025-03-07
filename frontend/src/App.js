import {Routes, Route} from "react-router-dom";
import NavBar from "./components/NavBar";
import Signup from "./pages/Signup";
import Login from "./pages/Login";
import Profile from "./pages/Profile";

const App = () => {
    return (
        <>
            <NavBar/>
            <Routes>
                <Route path="/" element={<h1>메인 페이지</h1>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/me" element={<Profile/>}/>
            </Routes>
        </>
    );
};

export default App;
