import {Routes, Route} from "react-router-dom";
import NavBar from "./components/NavBar";
import Signup from "./pages/Signup";
import Login from "./pages/Login";
import Profile from "./pages/Profile";
import MovieRegister from "./pages/MovieRegister";
import AlertManage from "./pages/AlertManage";

const App = () => {
    return (
        <>
            <NavBar/>
            <Routes>
                <Route path="/" element={<h1>메인 페이지</h1>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/me" element={<Profile/>}/>
                <Route path="/movie-register" element={<MovieRegister/>}/>
                <Route path="/alerts/manage" element={<AlertManage/>}/>
            </Routes>
        </>
    );
};

export default App;
