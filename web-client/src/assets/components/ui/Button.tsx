
interface ButtonProps {
    onClick?: () => void,
    text: string,
    type?: "submit" | "reset" | "button"
}


function Button({onClick, text, type}: ButtonProps) {
  return (
    <button
      className="bg-yellow-theme rounded-2xl min-w-[200px] w-full py-4 cursor-pointer flex justify-center items-center space-x-3 text-white"
       type={type} onClick={onClick}
    >
      <span>{text}</span>
    </button>
  );
}

export default Button;
