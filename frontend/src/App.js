import {Routes, Route} from "react-router-dom";
import NavBar from "./components/NavBar";
import Signup from "./pages/Signup";
import Login from "./pages/Login";
import Profile from "./pages/Profile";
import AlertManage from "./pages/AlertManage";
import Main from "./pages/Main";
import UpcomingPage from "./pages/UpcomingPage";

const App = () => {
    return (
        <>
            <NavBar/>
            <Routes>
                <Route path="/" element={<Main/>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/me" element={<Profile/>}/>
                <Route path="/alerts/manage" element={<AlertManage/>}/>
                <Route path="/upcoming" element={<UpcomingPage/>}/>
            </Routes>
        </>
    );
};

export default App;
