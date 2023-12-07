import React, { useEffect, useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';
import Image from 'react-bootstrap/Image';
import pp1Image from '../assets/pp1.jpg';
import { useNavigate } from 'react-router-dom';

const Profile = () => {
  const [user, setUser] = useState({});
  const currUser = JSON.parse(localStorage.getItem('user'));
  const BASE_URL = 'http://localhost:8080';
  const [prompt, setPrompt] = useState('');
  const [promptImg, setPromptImg] = useState('');
  const navigate = useNavigate();
  const [emailChange, setEmailChange] = useState({
    email: user.email,
    password: ''
  });
  const [passwordChange, setPasswordChange] = useState({
    oldPassword: '',
    newPassword: '',
    newPasswordConfirm: ''
  });

  const handlePromptGenerate = e => {
    e.preventDefault();

    const promptTemp = prompt.split(' ').join('+');
    axios
      .get(`https://api.multiavatar.com/${promptTemp}.svg`)
      .then(res => {
        setPromptImg(res.config.url);
      })
      .catch(err => {
        console.log(err);
      });
  };

  const handlePromptSubmit = e => {
    e.preventDefault();
    const confirmed = window.confirm(
      'Once submitted, you cannot change your profile picture. Are you sure?'
    );
    if (confirmed) {
      axios
        .put(
          `${BASE_URL}/api/user/${currUser.userId}/image`,
          promptImg,
          {
            headers: {
              Authorization: `Bearer ${currUser.jwt}`,
              'Content-Type': 'text/plain'
            }
          }
        )
        .then(res => {
          if (res.status === 200) {
            setUser({ ...user, imageUri: promptImg });
            createAlert('Profile picture set!', 'success');
          }
        })
        .catch(err => {
          console.log(err);
        });
    }
  };

  useEffect(() => {
    axios
      .get(`${BASE_URL}/api/user/${currUser.userId}`)
      .then(response => {
        setUser(response.data);
      })
      .catch(error => {
        console.log(error);
      });
  }, []);

  const handleEmailChange = e => {
    // const passwordRegex =
    //   /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,30}$/;

    // if (!passwordRegex.test(emailChange.password)) {
    //   createAlert(
    //     'Password must contain an uppercase letter, a symbol, and a number',
    //     'error'
    //   );
    //   return;
    // }

    axios
      .post(
        `${BASE_URL}/api/user/${user.userId}/changeEmail`,
        emailChange,
        {
          headers: {
            Authorization: `Bearer ${currUser.jwt}`,
            'Content-Type': 'application/json'
          }
        }
      )
      .then(res => {
        createAlert('Email updated!', 'success');
        user.email = emailChange.email;
        setEmailChange({ ...emailChange, password: '' });
      })
      .catch(err => {
        if (err.response.status === 400) {
          if (err.response.data === 'Password is incorrect') {
            createAlert('Password is incorrect', 'error');
          } else if (err.response.data === 'Email already exists') {
            createAlert('Email already exists', 'error');
          }
        } else {
          createAlert('Something went wrong', 'error');
          console.log(err);
        }
      });
  };

  const handlePasswordChange = e => {
    // const passwordRegex =
    //   /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,30}$/;

    // if (
    //   !passwordRegex.test(passwordChange.password) ||
    //   !passwordRegex.test(passwordChange.newPassword) ||
    //   !passwordRegex.test(passwordChange.newPasswordConfirm)
    // ) {
    //   createAlert(
    //     'Password must contain an uppercase letter, a symbol, and a number',
    //     'error'
    //   );
    //   return;
    // }
    if (
      passwordChange.newPassword !==
      passwordChange.newPasswordConfirm
    ) {
      createAlert('Passwords do not match', 'error');
      return;
    } else if (
      passwordChange.oldPassword === passwordChange.newPassword
    ) {
      createAlert(
        'New password cannot be the same as old password',
        'error'
      );
      return;
    } else {
      const passwordChangeTemp = {
        oldPassword: passwordChange.password,
        newPassword: passwordChange.newPassword
      };

      axios
        .post(
          `${BASE_URL}/api/user/${user.userId}/changePassword`,
          passwordChangeTemp,
          {
            headers: {
              Authorization: `Bearer ${currUser.jwt}`,
              'Content-Type': 'application/json'
            }
          }
        )
        .then(res => {
          createAlert('Password updated!', 'success');
          setPasswordChange({
            password: '',
            newPassword: '',
            newPasswordConfirm: ''
          });
        })
        .catch(err => {
          if (err.response.status === 400) {
            if (err.response.data === 'Old password is incorrect') {
              createAlert('Old password is incorrect', 'error');
            }
          } else {
            createAlert('Something went wrong', 'error');
            console.log(err);
          }
        });
    }
  };

  const createAlert = (message, type) => {
    if (type === 'success') {
      toast.success(message);
    } else if (type === 'error') {
      toast.error(message);
    }
  };

  return (
    <div className="flex flex-col justify-center items-center p-5">
      {!user.imageUri ? (
        <>
          {promptImg ? (
            <Image
              src={promptImg}
              alt="Image"
              className="md:w-64 md:h-64 w-44 h-44 mx-4 mb-2 mt-12 rounded-full"
            />
          ) : (
            <Image
              src={pp1Image}
              alt="Image"
              className="md:w-64 md:h-64 w-44 h-44 mx-4 mb-2 mt-12 rounded-full"
            />
          )}
          <form
            className="flex flex-col justify-center items-center"
            onSubmit={handlePromptGenerate}>
            <input
              type="text"
              value={prompt}
              onChange={e => setPrompt(e.target.value)}
              className="flex w-72 p-2 italic bg-neutral-900 text-m leading-tight mt-4 focus:outline-none focus:shadow-outline rounded"
              placeholder="Type a prompt to create an avatar..."
            />
            <div className="flex flex-row justify-center items-center">
              <button
                type="button"
                className="bg-zinc-900 mt-4 py-1
                px-4 hover:bg-zinc-600 rounded-md w-24 border-black border shadow-sm md:text-base text-sm"
                onClick={handlePromptGenerate}>
                Generate{' '}
              </button>
              <button
                className="bg-zinc-900 mt-4 py-1
                  px-4 hover:bg-green-600 rounded-md w-24 border-black border shadow-sm md:text-base text-sm"
                type="button"
                onClick={handlePromptSubmit}>
                Submit
              </button>
            </div>
          </form>
        </>
      ) : (
        <Image
          src={user.imageUri}
          alt="Image"
          className="md:w-64 md:h-64 w-44 h-44 mx-4 mb-2 mt-12 rounded-full"
        />
      )}

      <div className="flex flex-row items-center mt-4">
        <h4 className="md:text-2xl text-xl font-bold text-neutral-100">
          {'@'}
          {user.username}
        </h4>
      </div>
      <div className="w-full flex text-center flex-col p-4 rounded-md mt-10 border max-w-screen-xl border-zinc-600">
        <h1 className="text-2xl font-bold text-neutral-100 mb-14">
          Security
        </h1>
        <div className="flex flex-col md:flex-row text-left md:items-start items-center justify-center md:justify-between">
          <div className="flex flex-col text-left items-left md:w-1/2 max-w-xs w-4/5">
            <h4 className="pl-1 md:text-xl text-base py-2 text-left w-max">
              {' '}
              Change Your Password
            </h4>
            <form className="flex flex-col text-left w-full items-left">
              <input
                type="password"
                value={passwordChange.password}
                onChange={e =>
                  setPasswordChange({
                    ...passwordChange,
                    password: e.target.value
                  })
                }
                required
                className="flex md:w-72 w-full p-2 italic bg-neutral-900 text-m leading-tight mt-4 focus:outline-none focus:shadow-outline rounded"
                placeholder="Type in your old password..."
              />
              <input
                type="password"
                value={passwordChange.newPassword}
                onChange={e =>
                  setPasswordChange({
                    ...passwordChange,
                    newPassword: e.target.value
                  })
                }
                required
                minLength={10}
                maxLength={100}
                className="flex md:w-72 w-full p-2 italic bg-neutral-900 text-m leading-tight mt-4 focus:outline-none focus:shadow-outline rounded"
                placeholder="Type in your new password..."
              />
              <input
                type="password"
                value={passwordChange.newPasswordConfirm}
                onChange={e =>
                  setPasswordChange({
                    ...passwordChange,
                    newPasswordConfirm: e.target.value
                  })
                }
                required
                minLength={10}
                maxLength={100}
                className="flex md:w-72 w-full p-2 italic bg-neutral-900 text-m leading-tight mt-4 focus:outline-none focus:shadow-outline rounded"
                placeholder="Re-Type in your old password..."
              />
              <button
                type="button"
                onClick={handlePasswordChange}
                className="bg-zinc-900 mt-4 py-1
                px-4 hover:bg-red-600 rounded-md md:w-72 w-full border-black border shadow-sm md:text-base text-sm">
                Save New Password{' '}
              </button>
            </form>
          </div>
          <div className="flex flex-col text-left items-left md:w-1/2 max-w-xs w-4/5">
            <h4 className="pl-1 md:text-xl text-base py-2 md:mt-0 mt-8 text-left w-max">
              Change Your Email
            </h4>
            <h4 className="pl-1 text py-2 md:mt-0 mt-8 text-left w-max">
              Current Email: {user.email}
            </h4>
            <form className="flex flex-col text-left w-full">
              <input
                type="email"
                onChange={e =>
                  setEmailChange({
                    ...emailChange,
                    email: e.target.value
                  })
                }
                id="emailChange"
                value={emailChange.email}
                required
                minLength={10}
                maxLength={100}
                className="flex md:w-72 w-full p-2 italic bg-neutral-900 text-base leading-tight mt-4 focus:outline-none focus:shadow-outline rounded"
                placeholder="Type in your new email..."
              />
              <input
                type="password"
                id="passwordEmail"
                required
                minLength={8}
                maxLength={240}
                value={emailChange.password}
                onChange={e =>
                  setEmailChange({
                    ...emailChange,
                    password: e.target.value
                  })
                }
                className="flex md:w-72 w-full p-2 italic bg-neutral-900 text-m leading-tight mt-4 focus:outline-none focus:shadow-outline rounded"
                placeholder="Type in your password..."
              />
              <button
                type="button"
                onClick={handleEmailChange}
                className="bg-zinc-900 mt-4 py-1
                px-4 hover:bg-red-600 rounded-md md:w-72 w-full border-black border shadow-sm md:text-base text-sm">
                Save New Email{' '}
              </button>
            </form>
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
    </div>
  );
};

export default Profile;
