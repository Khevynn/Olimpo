import { Laugh, NotebookPen, Sticker } from "lucide-react";
import EmptyData from "../components/layouts/EmptyData";
import NavbarVertical from "../components/ui/NavbarVertical";

function Home() {
  return (
    <div className="w-screen h-screen bg-dark-300 flex items-center justify-center">
      <NavbarVertical active="home" />
      <div className="w-[700px] h-[90dvh] py-20 flex flex-col items-center text-white space-y-10">
        <div className="text-start space-y-2 w-full">
          <h1 className="text-3xl font-bold">
            Olá. Seja bem-vindo.
          </h1>
          <h1 className="text-2xl font-thin">
            Que comecem os jogos.
          </h1>
        </div>

        <div className="w-full space-y-5">
            <h1 className="text-2xl">Últimas partidas</h1>
            <EmptyData icon={<Sticker />} title="Você não possui partidas." text="Participe de algum jogo para vê-las." button="Procurar jogos"/>
        </div>
        <div className="w-full space-y-5">
            <h1 className="text-2xl">Jogadores ativos</h1>
            <EmptyData icon={<Laugh />} title="Você não possui amigos." text="Adicione  jogadores para jogar junto." button="Procurar amigos"/>
        </div>
      </div>
    </div>
  );
}

export default Home;
