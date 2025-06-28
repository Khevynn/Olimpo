import Button from "../ui/Button";
import { ReactElement } from "react";

interface DataProps{
    title : string,
    text: string,
    button: string,
    icon?: ReactElement
}

function EmptyData({title, text, button, icon} : DataProps) {
  return (
    <div className="w-full bg-dark-100 h-[270px] rounded-4xl flex flex-col items-center justify-center text-center text-white">
        {icon}
        <h1 className="font-bold">{title}</h1>
        <h2 className="">{text}</h2>
        <div className="w-[300px] mt-3"><Button text={button}/></div>
    </div>
  );
}

export default EmptyData;
