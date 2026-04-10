import {useEffect} from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setUserDetails} from "../store/userActions";

const OAuthTokenHandler = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');

        if(!token){
            alert("No token found in URL");
            navigate('/');
            return;
        }

        localStorage.setItem('token', token);
        axios.get('http://localhost:8080/api/auth/details', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
        .then(response => {
            const user = {
                role: response.data.roles[0],
                username: response.data.username
            };
            dispatch(setUserDetails(user));

            switch(user.role){
                case 'ROLE_APPLICANT':
                    navigate('/applicant-dashboard');
                    break;
                case 'ROLE_ADMIN':
                    navigate('/admin-dashboard');
                    break;
                default:
                    navigate('/');
            }
        })
        .catch(error => { 
            console.error('Error fetching user details:', error);
            alert('Google login failed. Please try again.');
            navigate('/');
        });                                                                             
    },[dispatch, navigate]);

   return <h2>Logging in with Google...</h2>  
};

export default OAuthTokenHandler