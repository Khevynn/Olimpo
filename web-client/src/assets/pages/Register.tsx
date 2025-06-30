import { useNavigate } from "react-router-dom";
import AuthForm from "../components/layouts/AuthForm";
import AuthImage from "../components/layouts/AuthImage";
import Button from "../components/ui/Button";
import Input from "../components/ui/Input";
import { goToLogin } from "../routes/navigation";
import { UseAuth } from "../hooks/useAuth";
import { registerSchema, RegisterSchema } from "../schemas/schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import Loading from "../components/ui/Loading";
import { useEffect } from "react";

function Register() {
  const navigate = useNavigate();
  const onRedirectLogin = () => {
    goToLogin(navigate);
  };

  //@ts-ignore auth fetch
  const { status, mutate, isPending} = UseAuth(import.meta.env.VITE_API_REGISTER);

  const HandleRegister = (data: RegisterSchema) => {
    mutate(data);
  };

  //react hook form
  const {
    register,
    handleSubmit,
    formState: { errors },
     reset 
  } = useForm<RegisterSchema>({
    resolver: zodResolver(registerSchema),
  });

  //Reset form
    useEffect(() => {
      if (status.success){
        reset()
      }
    }, [status])

  return (
    <div className="bg-dark-300 w-screen h-screen flex items-center justify-center space-x-3">
      <AuthForm
        title="Registrar"
        subtitle="Crie uma conta para começar."
        onSubmit={handleSubmit(HandleRegister)}
      >
        <Input placeholder="E-mail" type="email" {...register("email")} error={errors.email?.message}/>
        <Input placeholder="Usuario" type="user" {...register("user")} error={errors.user?.message}/>
        <Input placeholder="Senha" type="password" {...register("password")} error={errors.password?.message}/>

        <div className="text-white text-sm font-thin h-[50px] text-center flex justify-center items-center">
          {isPending ? <Loading /> : status.message}
        </div>
        <Button text="Registrar" type="submit" />
        <p className="text-sm text-grey-100 text-center">
          Já tem uma conta?
          <span
            className="text-yellow-theme cursor-pointer px-1"
            onClick={onRedirectLogin}
          >
            Entre
          </span>
        </p>
      </AuthForm>
      <AuthImage />
    </div>
  );
}

export default Register;
