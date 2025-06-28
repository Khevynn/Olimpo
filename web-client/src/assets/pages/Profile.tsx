import { useForm } from "react-hook-form";
import Input from "../components/ui/Input";
import NavbarVertical from "../components/ui/NavbarVertical";
import { profileSchema, ProfileSchema } from "../schemas/schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import TextArea from "../components/ui/TextArea";
import Button from "../components/ui/Button";

function Profile() {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ProfileSchema>({
    resolver: zodResolver(profileSchema),
  });

  return (
    <div className="w-screen h-screen bg-dark-300 flex items-center justify-center">
      <NavbarVertical />
      <div className="w-[700px] h-[90dvh] py-20 flex flex-col items-center text-white space-y-10">
        <div className="text-start space-y-2 w-full">
          <h1 className="text-3xl font-bold">Configurações de usuário.</h1>
          <h1 className="text-2xl font-thin">Detalhes sobre sua conta.</h1>
        </div>
        <div className="w-full flex justify-center">
          <div className="w-[70%] pr-5">
            <label htmlFor="" className="block text-2xl mb-3">
              Nome de Usuário
            </label>
            <Input
              placeholder="Usuário"
              type="text"
              {...register("user")}
              error={errors.user?.message}
            />
          </div>
          <div className="w-[30%]">
            <label htmlFor="" className="block text-2xl mb-3">
              GameTag
            </label>
            <Input
              placeholder="Gametag"
              maxlength="4"
              type="text"
              max="4"
              {...register("tag")}
              error={errors.tag?.message}
            />
          </div>
        </div>
        <div className="w-full">
          <label htmlFor="" className="block text-2xl mb-3">
            Sobre mim
          </label>
          <TextArea placeholder="Descrição" />
        </div>
        <Button text="Salvar"/>
      </div>
    </div>
  );
}

export default Profile;
