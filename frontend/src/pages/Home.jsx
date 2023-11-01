import React from 'react'
import { Link } from 'react-router-dom'

const Home = () => {
  return (
    <>
      <div className='flex justify-center items-center flex-col mt-40 mx-10 md:text-lg'>
        <h1 className='text-5xl mb-4'>Private Communication App</h1>
        <div className='flex flex-row'>
          <button className='bg-zinc-500 hover:bg-zinc-700 text-black font-bold w-fit mr-5 py-2 px-4 rounded-full'>
            <Link to='/signin'>Sign In</Link>
          </button>
          <button className='bg-zinc-500 hover:bg-zinc-700 text-black font-bold w-fit py-2 px-4 rounded-full'>
            <Link to='/signup'>Sign Up</Link>
          </button>
        </div>
        
      </div>
      
    </>
  )
}

export default Home