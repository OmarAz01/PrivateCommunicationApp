import React, { useEffect, useState } from 'react';
import { Image } from 'react-bootstrap';
import pp1Image from '../assets/pp1.jpg';
import axios from 'axios';

const RequestsBox = ({ createAlert }) => {
  const [requests, setRequests] = useState([]);
  const currUser = JSON.parse(localStorage.getItem('user'));
  const URL = 'http://localhost:8080';

  useEffect(() => {
    axios
      .get(`${URL}/api/user/${currUser.userId}/requests`, {
        headers: {
          Authorization: `Bearer ${currUser.jwt}`
        }
      })
      .then(res => {
        setRequests(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }, [currUser.userId]);

  const handleAccept = requestId => {
    const request = {
      requestId: requestId,
      status: 'Accepted'
    };
    axios
      .post(`${URL}/api/user/requestResponse`, request, {
        headers: {
          Authorization: `Bearer ${currUser.jwt}`,
          'Content-Type': 'application/json'
        }
      })
      .then(res => {
        createAlert('Request Accepted!', 'success');
        console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  };

  const handleReject = requestId => {
    const request = {
      requestId: requestId,
      status: 'Rejected'
    };
    axios
      .post(`${URL}/api/user/requestResponse`, request, {
        headers: {
          Authorization: `Bearer ${currUser.jwt}`
        }
      })
      .then(res => {
        createAlert('Request Rejected!', 'danger');
        setRequests(
          requests.filter(request => request.requestId !== requestId)
        );
      })
      .catch(err => {
        console.log(err);
      });
  };

  return requests.map(request => (
    <div
      className="w-full bg-zinc-700 h-fit p-2 border flex border-black rounded"
      key={request.id}>
      {request.imageUri ? (
        <Image
          src={request.imageUri}
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

      <div className="flex flex-col w-full">
        <h1 className="text-sm md:text-base mt-3">
          {request.senderUsername} wants to chat{' '}
        </h1>
        <div className="flex flex-col md:flex-row justify-start w-full pt-2">
          <button
            onClick={e => handleAccept(request.requestId)}
            className="border-2 border-black rounded-md w-fit px-2 py-1 mr-4 hover:bg-green-500">
            Accept
          </button>
          <button
            onClick={e => handleReject(request.requestId)}
            className="border-2 border-black rounded-md p-1 w-fit px-2 py-1 hover:bg-red-500">
            Reject
          </button>
        </div>
      </div>
    </div>
  ));
};

export default RequestsBox;
