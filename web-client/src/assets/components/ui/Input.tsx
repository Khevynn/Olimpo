
function Input({error, ...rest}: any) {
  return (
    <div>
      <input {...rest} className={`w-full py-4 px-3 text-grey-100 placeholder-grey-100 bg-dark-100 rounded-xl focus:bg-dark-100 ${error && 'border border-red-500'}`}/>

      <p className={`text-red-500 text-sm font-thin h-[20px]`}>{error}</p>
    </div>
    
  )
}

export default Input