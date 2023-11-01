import React, { useEffect, useState } from 'react';
import {
  BrowserRouter,
  Link,
  Route,
  Routes
} from 'react-router-dom';
import { Home, SignIn, SignUp, Messaging, Logout, Profile } from './pages/index';
import axios from 'axios';
import './index.css';

const App = () => {
  const BASE_URL = 'http://localhost:8080';
  const [loggedIn, setLoggedIn] = useState(false);
  const currUser = JSON.parse(localStorage.getItem('user'));
    useEffect(() => {
      if (currUser) {
        const token = currUser.jwt;
        const headers = {
          Authorization: `Bearer ${token}`
        };
        axios
          .post(`${BASE_URL}/api/auth/validate`, null, {
            headers: headers
          })
          .then(response => {
            if (response.status === 200) {
              localStorage.setItem(
                'user',
                JSON.stringify(response.data)
              );
              setLoggedIn(true);
            }
          })
          .catch(error => {
            if (
              error.response.status === 404 ||
              error.response.status === 403
            ) {
              setLoggedIn(false);
              localStorage.removeItem('user');
            } else {
              localStorage.removeItem('user');
              console.log(error);
            }
          });
      } else {
        setLoggedIn(false);
      }
    }, [currUser]);

  return (
    <BrowserRouter>
      <header className="flex justify-end md:text-lg">
        <Link to="/">
          <h4 className="p-4 absolute left-0 hover:text-zinc-500">
            {' '}
            Private Communication App{' '}
          </h4>
        </Link>
        <Link to={'/messaging'}>
          <h4 className="p-4 hover:text-zinc-500">
            {' '}
            Messaging{' '}
          </h4>
        </Link>
        {loggedIn && (
          <>
            <Link to={'/profile'}>
            <h4 className="p-4 hover:text-zinc-500">
              {' '}
              Profile{' '}
            </h4>
            </Link>
            <Link to={'/logout'}> 
            <h4 className="p-4 hover:text-zinc-500">
              {' '}
              Logout{' '}
            </h4>
          </Link>
        </>
        )}        
      </header>
      <main>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/messaging" element={<Messaging />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/logout" element={<Logout />} />
          <Route path='/profile' element={<Profile />} />
        </Routes>
      </main>
      <footer></footer>
    </BrowserRouter>
  );
};

export default App;
