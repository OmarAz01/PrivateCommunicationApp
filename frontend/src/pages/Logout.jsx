import axios from 'axios';
import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Logout = () => {
    
    const BASE_URL = 'http://localhost:8080';
    const currUser = JSON.parse(localStorage.getItem('user'));
    const navigate = useNavigate();
  
    useEffect(() => {
      if (currUser) {
        axios
          .get(`${BASE_URL}/api/auth/logout`, {
            headers: {
              Authorization: `Bearer ${currUser.jwt}`
            }
          })
          .then(response => {
            if (response.status === 200) {
              toast.success(
                'Successfully logged out, redirecting to sign in page'
              );
              localStorage.removeItem('user');
              setTimeout(() => {
                navigate('/signin');
              }, 3000);
            }
          })
          .catch(error => {
            toast.error('Something went wrong');
          });
      } else {
        navigate('/signin');
      }
    }, []);
  
    return (
      <>
        <ToastContainer
          position="top-center"
          autoClose={5000}
          hideProgressBar={true}
          newestOnTop={true}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="dark"
        />
        ;
      </>
    );
  };

export default Logout