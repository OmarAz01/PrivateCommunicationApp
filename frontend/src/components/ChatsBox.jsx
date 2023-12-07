import React, { useEffect, useState } from 'react';
import { Image } from 'react-bootstrap';
import pp1Image from '../assets/pp1.jpg';
import axios from 'axios';

const ChatsBox = ({ setRecipient }) => {
  const [prevChats, setprevChats] = useState([]);
  const currUser = JSON.parse(localStorage.getItem('user'));
  const URL = 'http://localhost:8080';

  useEffect(() => {
    axios
      .get(`${URL}/api/user/${currUser.userId}/prevChats`, {
        headers: {
          Authorization: `Bearer ${currUser.jwt}`
        }
      })
      .then(res => {
        setprevChats(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }, [currUser.userId]);

  const handleClick = id => {
    setRecipient(id);
  };

  return prevChats.map(
    prevChat => (
      console.log(prevChat),
      (
        <div
          key={prevChat.recipientId}
          className="w-full bg-zinc-700 h-fit p-2 border flex items-center border-black rounded mt-2 hover:bg-zinc-600 hover:cursor-pointer"
          onClick={e => handleClick(prevChat.recipientId)}
          id={prevChat.recipientId}>
          {prevChat.imageUri ? (
            <Image
              src={prevChat.imageUri}
              alt="Image"
              className="w-10 h-10 lg:w-20 lg:h-20 rounded-full mr-2"
            />
          ) : (
            <Image
              src={pp1Image}
              alt="Image"
              className="w-10 h-10 lg:w-20 lg:h-20 rounded-full mr-2"
            />
          )}
          <h1 className="text-sm md:text-base">
            {prevChat.recipientUsername} -{' '}
          </h1>
          <h1 className="text-sm md:text-base ml-2">
            {prevChat.lastMessage}
          </h1>
        </div>
      )
    )
  );
};

export default ChatsBox;
