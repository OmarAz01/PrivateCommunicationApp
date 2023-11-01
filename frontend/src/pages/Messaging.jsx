import React from 'react'

const Messaging = () => {
  return (
    <>
        <div className='flex flex-row p-4 mt-40 justify-center items-center'>
            <div className='border rounded-md w-full max-w-screen-lg h-screen flex justify-center'>
                <h1 className='p-4 text-lg'>Messages</h1>
            </div>
            <div className='border rounded-md ml-4 w-1/3 scree max-w-sm h-screen flex justify-center'>
                <h1 className='p-4 text-lg'>Requests</h1>
            </div>
        </div>
    </>
  )
}

export default Messaging