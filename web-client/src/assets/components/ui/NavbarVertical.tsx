import { Gamepad2, HomeIcon, House, MessageCircle, Trophy } from "lucide-react";
import { useNavigate } from "react-router-dom";
import {
  goToGames,
  goToHall,
  goToHome,
  goToMessages,
  goToProfile,
} from "../../routes/navigation";

interface NavbarProps {
  active?: string;
}

function NavbarVertical({ active }: NavbarProps) {
  const navigate = useNavigate();

  const tabs = [
    { key: "home", Icon: House, onclick: () => goToHome(navigate) },
    { key: "games", Icon: Gamepad2, onclick: () => goToGames(navigate) },
    { key: "hall", Icon: Trophy, onclick: () => goToHall(navigate) },
  ];

  return (
    <div
      className="bg-dark-100 w-[90vw] h-[70px] lg:h-[90dvh] lg:w-[90px] 
    absolute top-5 lg:left-10 lg:inset-y-0  lg:my-auto rounded-2xl 
    flex flex-row lg:flex-col items-between justify-center lg:items-center py-10 lg:justify-between px-10 lg:px-0"
    >
      <div className="flex flex-row lg:flex-col items-center w-full">
        <img className="w-10 lg:mb-5 mr-10 lg:mr-0" src="/logo2.png" alt="logo" />
        {tabs.map(({ key, Icon, onclick }) => (
          <div
            className={`w-20 lg:w-full flex justify-center items-center h-15 ${
              active === key
                ? "text-yellow-theme border-t-2 lg:border-t-0 lg:border-r-2 border-yellow-theme"
                : "text-white"
            }`}
            onClick={onclick}
          >
            <Icon />
          </div>
        ))}
      </div>
      <div className="flex flex-row lg:flex-col items-center lg:space-y-7">
        <MessageCircle
          className="text-white w-10"
          onClick={() => goToMessages(navigate)}
        />
        <div
          className="rounded-full w-[50px] h-[50px] bg-grey-100"
          onClick={() => goToProfile(navigate)}
        ></div>
      </div>
    </div>
  );
}

export default NavbarVertical;
