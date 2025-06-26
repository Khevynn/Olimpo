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
  active: string;
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
      className="bg-dark-100 h-[90dvh] w-[90px] 
    absolute left-10 inset-y-0 my-auto rounded-2xl 
    flex flex-col items-center py-10 justify-between"
    >
      <div className="flex flex-col items-center w-full">
        <img className="w-10 mb-10" src="/logo2.png" alt="logo" />
        {tabs.map(({ key, Icon, onclick }) => (
          <div
            className={`w-full flex justify-center items-center h-15 ${
              active === key
                ? "text-yellow-theme border-r-2 border-yellow-theme"
                : "text-white"
            }`}
            onClick={onclick}
          >
            <Icon />
          </div>
        ))}
      </div>
      <div className="flex flex-col items-center space-y-7">
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
