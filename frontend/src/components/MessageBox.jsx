import React, { useRef, useEffect, useState } from 'react';
import { Image } from 'react-bootstrap';
import pp1Image from '../assets/pp1.jpg';
import WebSocketService from './WebSocketService';
import axios from 'axios';

const MessageBox = ({ createAlert, recipientId }) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const currUser = JSON.parse(localStorage.getItem('user'));
  const [recipient, setRecipient] = useState({});
  const URL = 'http://localhost:8080';
  const roomIdRef = useRef(0);
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    if (recipientId === 0 || recipientId === undefined) {
      return;
    }
    if (!connected) {
      WebSocketService.connect(currUser.userId, onMessageReceived);
      setConnected(true);
    }
    axios
      .get(`${URL}/api/user/${recipientId}`)
      .then(res => {
        setRecipient(res.data);
        console.log(res.data);
      })
      .catch(err => {
        console.log(err);
      });

    axios
      .get(`${URL}/api/user/${currUser.userId}/${recipientId}`, {
        headers: {
          Authorization: `Bearer ${currUser.jwt}`
        }
      })
      .then(res => {
        roomIdRef.current = res.data;
        console.log(res.data);
        axios
          .get(`${URL}/api/user/${res.data}/messages`, {
            headers: {
              Authorization: `Bearer ${currUser.jwt}`
            }
          })
          .then(res => {
            const messageStream = res.data.reverse();
            setMessages(res.data);
          })
          .catch(err => {
            console.log(err);
          });
      })
      .catch(err => {
        console.log(err);
      });
  }, [recipientId]);

  const onMessageReceived = message => {
    if (message.chatRoomId !== roomIdRef.current) {
      return;
    }
    setMessages(prevMessages => [...prevMessages, message]);
  };

  const sendMessage = () => {
    if (newMessage === '') {
      return;
    } else if (recipientId === 0 || recipientId === undefined) {
      createAlert('Please select a chat!', 'error');
      return;
    } else {
      const message = {
        senderId: currUser.userId,
        recipientId: recipientId,
        content: newMessage,
        chatRoomId: roomIdRef.current
      };
      WebSocketService.sendMessage('/ws/message', message);
      setNewMessage('');
    }
  };

  const deleteChat = () => {
    if (recipientId === 0 || recipientId === undefined) {
      createAlert('Please select a chat!', 'error');
      return;
    }
    const confirm = window.confirm(
      'Are you sure you want to delete this chat?'
    );
    if (confirm) {
      axios
        .delete(`${URL}/api/user/chat/${roomIdRef.current}`, {
          headers: {
            Authorization: `Bearer ${currUser.jwt}`
          }
        })
        .then(res => {
          createAlert('Chat deleted!', 'success');
          setRecipient({});
          setMessages([]);
          setConnected(false);
          WebSocketService.disconnect();
        })
        .catch(err => {
          console.log(err);
        });
    }
  };

  return (
    <div className="flex flex-col justify-between h-full">
      <div className="flex-col h-3/4">
        <div className="flex border-b p-2 w-full">
          {recipient.imageUri ? (
            <Image
              src={recipient.imageUri}
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
          <h1 className="ml-4 mt-6 md:text-lg">
            {recipient.username}
          </h1>
          <button
            onClick={e => deleteChat()}
            className="ml-auto h-fit md:text-base text-sm hover:cursor-pointer hover:bg-red-900 hover:p-3 p-2 mt-6 border rounded-md bg-red-600">
            Delete Chat
          </button>
        </div>
        <div className="mt-5 overflow-y-scroll h-full">
          {messages.map((message, index) => (
            <div
              key={index}
              className={`${
                message.senderId === currUser.userId
                  ? 'text-right'
                  : 'text-left'
              } border-b mt-3 rounded-md p-2 w-full`}>
              <p>{message.content}</p>
            </div>
          ))}
        </div>
      </div>
      <div className="">
        <input
          type="text"
          value={newMessage}
          onChange={e => setNewMessage(e.target.value)}
          onKeyDown={e => {
            if (e.key === 'Enter') {
              sendMessage();
            }
          }}
          className={`border-2 border-black rounded-md w-full max-w-screen-md ml-4 h-12 p-2 text-black`}
        />
        <button
          className="rounded-md px-4 py-2 ml-2 bg-zinc-600 hover:bg-zinc-700 text-white"
          onClick={sendMessage}>
          Send
        </button>
      </div>
    </div>
  );
};

export default MessageBox;
