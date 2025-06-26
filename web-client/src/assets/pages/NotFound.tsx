
function NotFound() {
  return (
     <div className="relative w-screen h-screen bg-[url(/eye.webp)] bg-cover font-rubik bg-center">
      {/* Dark overlay */}
        <div className="absolute inset-0 bg-black opacity-80 z-0"></div>

        {/* logo */}
        <img src="logo2.png" className="w-10 absolute top-10 left-10" />
          

        {/* TEXT */}
        <div className="flex flex-col items-center justify-center w-screen h-screen space-y-5">
              <h1 className="font-bold text-4xl text-white z-10">Página não encontrada.</h1>
              <h1 className="text-2xl text-white z-10">Parece que a rota inseride está incorreta. Verifique-a e tente novamente.</h1>
        </div>
    </div>
  )
}

export default NotFound