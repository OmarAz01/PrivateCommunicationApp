import React, { useEffect, useState } from 'react';
import RequestsBox from '../components/RequestsBox';
import MessageBox from '../components/MessageBox';
import ChatsBox from '../components/ChatsBox';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const Messaging = () => {
  const [currView, setCurrView] = useState('requests');
  const [recipientId, setRecipientId] = useState(0);
  const [search, setSearch] = useState('');
  const [displayUsers, setDisplayUsers] = useState([]);
  const URL = 'http://localhost:8080';
  const currUser = JSON.parse(localStorage.getItem('user'));

  const setRecipient = id => {
    setTimeout(() => {
      setRecipientId(id);
      console.log('id is ' + id);
    }, 1000);
  };

  const createAlert = (title, variant) => {
    if (variant === 'success') {
      return toast.success(title);
    } else {
      return toast.error(title);
    }
  };

  useEffect(() => {
    if (search === '') {
      setDisplayUsers([]);
    } else {
      axios
        .get(`${URL}/api/user/username/${search}`, {
          headers: {
            Authorization: `Bearer ${currUser.jwt}`
          }
        })
        .then(res => {
          setDisplayUsers(res.data);
        })
        .catch(err => {
          console.log(err);
        });
    }
  }, [search]);

  const sendRequest = id => {
    axios
      .post(
        `${URL}/api/user/request/${currUser.userId}/${id}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${currUser.jwt}`
          }
        }
      )
      .then(res => {
        toast.success('Request sent!');
        setSearch('');
      })
      .catch(err => {
        toast.error('Request failed!');
        setSearch('');
        console.log(err);
      });
  };

  return (
    <>
      <div className="flex flex-col justify-center items-center rounded-md">
        <input
          type="text"
          value={search}
          onChange={e => setSearch(e.target.value)}
          placeholder="Search for a user..."
          className="border-2 text-black border-black rounded-md w-full max-w-screen-md ml-4 h-12 p-2 mt-40"
        />
        {displayUsers.length > 0 ? (
          <div className="flex flex-col bg-zinc-900 rounded-md w-full max-w-screen-md ml-4 h-fit p-2 ">
            {displayUsers.map((user, index) => (
              <div
                key={index}
                className="flex flex-row justify-between items-center border-b py-2">
                <h1>{user.username}</h1>
                <button
                  onClick={e => sendRequest(user.userId)}
                  className="border-2 border-black rounded-md p-2 bg-zinc-800 hover:bg-zinc-700">
                  Send Request
                </button>
              </div>
            ))}
          </div>
        ) : null}
      </div>

      <div className="flex flex-row p-4 justify-center items-center">
        <div className="border-2 border-black rounded-md w-full max-h-[60rem] h-screen max-w-screen-lg flex justify-center">
          <div className="flex flex-col w-full h-full p-4">
            <MessageBox
              createAlert={createAlert}
              recipientId={recipientId}
            />
          </div>
        </div>
        <div className="border-black border-2 rounded-md ml-4 w-1/3 max-w-sm h-screen max-h-[60rem] flex-row justify-center">
          <div className="flex flex-row justify-center items-center w-full h-12 border-b-2 border-black">
            <button
              className=" rounded-md p-2 w-1/2 text-sm md:text-base hover:bg-zinc-700"
              onClick={e => setCurrView('requests')}>
              Requests
            </button>
            <button
              className="rounded-md p-2 w-1/2 text-sm md:text-base hover:bg-zinc-700"
              onClick={e => setCurrView('chats')}>
              Chats
            </button>
          </div>
          <div className="p-2">
            {currView === 'requests' ? (
              <RequestsBox createAlert={createAlert} />
            ) : (
              <ChatsBox setRecipient={setRecipient} />
            )}
          </div>
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
    </>
  );
};

export default Messaging;
