import axios from 'axios';
import React, { useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const SignUp = () => {
  const BASE_URL = 'http://localhost:8080';
  const [register, setRegister] = useState({
    username: '',
    email: '',
    password: '',
    passwordConfirmation: ''
  });

  const handleRegister = e => {
    e.preventDefault();

    // const passwordRegex =
    //   /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,30}$/;

    // if (!passwordRegex.test(register.password)) {
    //   toast.error(
    //     'Password must contain an uppercase letter, a symbol, and a number'
    //   );
    //   return;
    // }

    if (register.password === register.passwordConfirmation) {
      axios
        .post(`${BASE_URL}/api/auth/register`, register)
        .then(response => {
          if (response.data.error === 'Username already exists') {
            toast.error('Username already exists');
            return;
          } else if (
            response.data.error === 'Email already exists'
          ) {
            toast.error('Email already exists');
            return;
          }
          localStorage.setItem(
            'user',
            JSON.stringify(response.data)
          );
          toast.success('Registered successfully redirecting...');
          setTimeout(() => {
            window.location.href = '/myaccount';
          }, 5000);
        })
        .catch(error => {
          console.log(error);
        });
    } else {
      toast.error('Passwords do not match');
    }
  };

  return (
    <div className="w-full flex flex-col justify-center">
      <div className="flex flex-col justify-center my-auto pt-8 mt-28 px-8 lg:px-32">
        <p className="text-center text-3xl">Register</p>
        <form
          className="flex flex-col pt-8 md:items-center"
          onSubmit={handleRegister}>
          <div className="flex items-center flex-col min-w-full">
            <input
              type="text"
              id="name"
              placeholder="Username"
              value={register.username}
              required
              minLength={4}
              maxLength={10}
              onChange={e =>
                setRegister({
                  ...register,
                  username: e.target.value
                })
              }
              className="shadow appearance-none border rounded w-full md:max-w-screen-sm py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
            />
          </div>

          <div className="flex items-center flex-col pt-4 min-w-full">
            <input
              type="email"
              id="email"
              placeholder="your@email.com"
              value={register.email}
              required
              minLength={10}
              maxLength={100}
              onChange={e =>
                setRegister({ ...register, email: e.target.value })
              }
              className="shadow appearance-none border rounded w-full md:max-w-screen-sm py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
            />
          </div>

          <div className="flex items-center flex-col pt-4 min-w-full">
            <input
              type="password"
              id="password"
              placeholder="Password"
              required
              minLength={8}
              maxLength={240}
              value={register.password}
              onChange={e =>
                setRegister({
                  ...register,
                  password: e.target.value
                })
              }
              className="shadow appearance-none border rounded w-full md:max-w-screen-sm py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
            />
          </div>

          <div className="flex items-center flex-col pt-4 min-w-full">
            <input
              type="password"
              id="confirm-password"
              placeholder="Retype password"
              required
              minLength={8}
              maxLength={240}
              value={register.passwordConfirmation}
              onChange={e =>
                setRegister({
                  ...register,
                  passwordConfirmation: e.target.value
                })
              }
              className="shadow appearance-none border rounded w-full md:max-w-screen-sm py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
            />
          </div>
          <button
            type="submit"
            className="bg-black items-center w-full rounded-md md:max-w-screen-sm text-white font-bold text-lg hover:bg-gray-600 p-2 mt-8">
            Register
          </button>
        </form>
        <div className="text-center pt-12 pb-12">
          <p>
            Already have an account?{' '}
            <a href="/signin" className="underline font-semibold">
              Log in here.
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

export default SignUp;
