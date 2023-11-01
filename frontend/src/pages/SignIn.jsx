import axios from 'axios';
import React, { useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const SignIn = () => {
  const [userDetails, setUserDetails] = useState({
    email: '',
    password: ''
  });
  const BASE_URL = 'http://localhost:8080';

  const handleLogin = async e => {
    e.preventDefault();

    try {
      const response = await axios.post(
        BASE_URL + '/api/auth/authenticate',
        userDetails
      );
      localStorage.setItem('user', JSON.stringify(response.data));
      window.location.href = '/myaccount';
    } catch (error) {
      if (error.response.status === 403) {
        toast.error('Invalid Credentials');
      } else {
        toast.error('Something went wrong');
        console.log(error);
      }
    }
  };
  return (
    <div className="w-full flex flex-col justify-center">
      <div className="flex flex-col justify-center my-auto pt-8 mt-28 px-8 md:px-24 lg:px-32">
        <p className="text-center text-3xl">Log In</p>
        <form
          className="flex flex-col md:items-center pt-8"
          onSubmit={handleLogin}>
          <div className="flex flex-col items-center min-w-full">
            <label htmlFor="email" className="text-xl">
              Email
            </label>
            <input
              value={userDetails.email}
              onChange={e =>
                setUserDetails(prev => ({
                  ...prev,
                  email: e.target.value
                }))
              }
              type="email"
              id="email"
              placeholder="your@email.com"
              className="shadow appearance-none border rounded w-full md:max-w-screen-sm py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
            />
          </div>

          <div className="flex flex-col pt-4 items-center min-w-full">
            <label htmlFor="password" className="text-xl">
              Password
            </label>
            <input
              value={userDetails.password}
              onChange={e =>
                setUserDetails(prev => ({
                  ...prev,
                  password: e.target.value
                }))
              }
              type="password"
              id="password"
              placeholder="Password"
              className="shadow appearance-none border rounded w-full md:max-w-screen-sm py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
            />
          </div>

          <button
            type="submit"
            className="bg-black w-full rounded-md md:max-w-screen-sm text-white font-bold text-lg hover:bg-gray-600 p-2 mt-8">
            {' '}
            Log In{' '}
          </button>
        </form>
        <div className="text-center pt-12 pb-12">
          <p>
            Don't have an account?{' '}
            <a href="/signup" className="underline font-semibold">
              Register here.
            </a>
          </p>
        </div>
      </div>
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
    </div>
  );
};

export default SignIn;
