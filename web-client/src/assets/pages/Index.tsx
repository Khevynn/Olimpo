import { useNavigate } from "react-router-dom";
import { goToLogin, goToRegister } from "../routes/navigation";
import Button from "../components/ui/Button";

function Index() {
  const navigate = useNavigate();

  function onLoginRedirect() {
    goToLogin(navigate);
  }
  function onRegisterRedirect() {
    goToRegister(navigate);
  }

  return (
    <div className="w-screen">
      <div className="w-full h-[900px] bg-dark-300 bg-[url(/wallpaper.png)] bg-cover bg-fixed">


        {/* Navbar */}
        <div
          className="max-w-7xl mx-auto flex justify-between items-center p-3"
          
        >
          <img src="logo2.png" className="w-10" />
          <div className="flex space-x-5 text-white">
            <button className="cursor-pointer" onClick={onRegisterRedirect}>
              Registrar-se
            </button>
            <div className="">
              <Button text="Entrar" onClick={onLoginRedirect} />
            </div>
          </div>
        </div>

        {/* Main Container*/}
        <div className="h-full w-full flex justify-center items-center text-white">
          <div className="max-w-7xl w-full flex flex-col md:flex-row justify-between items-center px-5 space-y-6 ">
            <div
              className="w-full md:w-[500px] space-y-3 text-center md:text-left "
              
            >
              <h1 className="text-3xl font-bold">Crie e Gerencie Torneios</h1>
              <p className="text-2xl">
                Organize torneios de Valorant de forma prática e rápida. Convide
                jogadores, monte chaves e acompanhe resultados em tempo real.
              </p>
            </div>
            <div
              className="w-full md:w-auto flex justify-center md:justify-end"
              
            >
              <Button  
              onClick={onLoginRedirect} 
              text="Entrar" 
              />
            </div>
          </div>
        </div>
      </div>


      
      {/* Section 3*/}
      <div className="w-full bg-dark-300">
        <div className="max-w-7xl mx-auto h-[600px] flex flex-col-reverse md:flex-row justify-center md:justify-between items-center text-white px-5 py-8 space-y-3">
          <div
            className="w-full md:w-auto flex justify-center md:justify-start  "
            
          >
            <Button  
            onClick={onRegisterRedirect} 
            text="Registrar" 
            />
            
          </div>

          <div
            className="w-full md:w-[500px] text-center md:text-end"
            
          >
            <h1 className="text-3xl font-bold">Lobbys Privados para Treinos</h1>
            <p className="text-2xl">
              Monte salas exclusivas para treinos profissionais. Ambiente ideal para times que buscam evolução sem distrações.
            </p>
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="w-full bg-dark-300 bg-[url(/wallpaper.png)] bg-fixed bg-bottom">
        <div className="grid md:grid-cols-2 min-h-[300px] space-y-5 p-3">
          <div
            className="flex justify-center items-center flex-col space-y-3"
            
          >
            <h1 className="text-bold text-white text-2xl">Contate-nos</h1>
            <p className="text-white w-[300px] text-center">
              Telefone:15 99999-1111
            </p>
            <p className="text-white">E-mail: contato@valorant.com</p>
          </div>
          <div
            className="flex justify-center items-center flex-col space-y-3"
            
          >
            <h1 className="text-bold text-white text-2xl">Sobre nós</h1>
            <p className="text-white w-[300px] text-center">
              Somos uma pequena empresa com foco na criação de plataforma de
              jogos.
            </p>
            <p className="text-white">CNPJ: 1321231213312</p>
          </div>
        </div>
      </div>

      {/* Copright */}
      <div className="w-full min-h-[50px] bg-dark-300 flex justify-center items-center">
        <h2 className="text-white text-center p-5">
          Copyright ©2025 Olimpo Desenvolvido por Vitor Miranda e Khevynn Sá
        </h2>
      </div>
    </div>
  );
}

export default Index;