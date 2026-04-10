import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { selectUser } from "../store/userSelectors";
import { setUserDetails } from "../store/userActions";

const Navbar = () => {
    const user = useSelector(selectUser);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.clear();
        let initialUser = {
            username: "",
            role: ""
        };
       
        dispatch(setUserDetails, initialUser);
        navigate("/");
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container">
                <Link className="navbar-brand" to="/">
                    <strong>Job Portal</strong>
                </Link>

                <div className="d-flex ms-auto align-items-center">
                    <span className="nav-link mb-0">
                        Welcome {user.username}
                    </span>

                    <button
                        className="btn btn-danger ms-3"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;