import { useNavigate } from "react-router-dom";
import AuthForm from "../components/layouts/AuthForm";
import AuthImage from "../components/layouts/AuthImage";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";
import { goToHome, goToRegister } from "../routes/navigation";
import { UseAuth } from "../hooks/useAuth";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { LoginSchema, loginSchema } from "../schemas/schemas";
import Loading from "../components/ui/Loading";
import { useEffect } from "react";

function Login() {
  
  //Redirect function
  const navigate = useNavigate();
  const onRedirectRegister = () => {
    goToRegister(navigate);
  };

  //@ts-ignore auth fetch
  const { status, mutate, isPending, data } = UseAuth(import.meta.env.VITE_API_AUTH);

  const HandleLogin = (data: LoginSchema) => {
    mutate(data);
  };

  //react hook form
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginSchema>({
    resolver: zodResolver(loginSchema),
  });
  
  //Go to home
  useEffect(() => {
    if (status.success){
      try {
        localStorage.setItem("token", data.token)
        goToHome(navigate);
      } catch (error) {
        
      }
    }
  }, [status])

  return (
    <div className="bg-dark-300 w-screen h-screen flex items-center justify-center lg:space-x-3">
      <AuthForm
        title="Entrar"
        subtitle="Insira seus dados para continuar"
        onSubmit={handleSubmit(HandleLogin)}
      >
        {/* EMAIL */}
        <Input
          placeholder="E-mail"
          type="email"
          error={errors.email?.message}
          {...register("email")}
        />

        {/* PASSWORD */}
        <Input
          placeholder="Senha"
          type="password"
          error={errors.password?.message}
          {...register("password")}
        />

        <div className="text-white text-sm font-thin h-[50px] text-center flex justify-center items-center">
          {isPending ? <Loading /> : status.message}
        </div>

        {/* BUTTON */}
        <Button text="Entrar" type="submit"/>

        {/* CREATE ACCOUNT */}
        <p className="text-sm text-grey-100 text-center">
          Ainda não tem uma conta?
          <span
            className="text-yellow-theme cursor-pointer px-1"
            onClick={onRedirectRegister}
          >
            Registre-se
          </span>
        </p>
      </AuthForm>
      <AuthImage />
    </div>
  );
}

export default Login;
