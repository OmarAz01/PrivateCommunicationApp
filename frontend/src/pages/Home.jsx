import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  const currUser = JSON.parse(localStorage.getItem('user'));
  return (
    <>
      <div className="flex justify-center items-center text-center flex-col mt-40 mx-10 md:text-lg">
        <h1 className="text-5xl mb-4">Private Communication App</h1>
        <div className="flex flex-row">
          <button className="bg-zinc-500 hover:bg-zinc-700 text-black font-bold w-fit mr-5 py-2 px-4 rounded-full">
            {currUser ? (
              <Link to="/Profile">Messaging</Link>
            ) : (
              <Link to="/signin">Sign In</Link>
            )}
          </button>
          <button className="bg-zinc-500 hover:bg-zinc-700 text-black font-bold w-fit py-2 px-4 rounded-full">
            {currUser ? (
              <Link to="/Profile">Profile</Link>
            ) : (
              <Link to="/signup">Sign Up</Link>
            )}
          </button>
        </div>
      </div>
    </>
  );
};

export default Home;
