// src/schemas/authSchemas.js
import { z } from "zod";

export const loginSchema = z.object({
  email: z.string().email("Email inválido").nonempty("Email é obrigatório"),
  password: z
    .string()
    .min(3, "Senha obrigatória")
    .max(12, "Senha muito extensa (12 digitos)"),
});

export const profileSchema = z.object({
  user: z
    .string()
    .min(3, "Usuario é obrigatório")
    .max(16, "Usuário muito extenso")
    .regex(
      /^[a-zA-Z0-9_]+$/,
      "Nome de usuário deve conter apenas letras, números ou _"
    ),
  tag: z
    .string()
    .min(4, "Gametag é obrigatório")
    .max(4, "Gametag é obrigatório")
    .regex(
      /^[a-zA-Z0-9]+$/,
      "Gametag deve conter apenas letras ou números"
    ),
    description: z
    .string()
    .max(256, "Descrição muito extensa.")
});

export const registerSchema = z.object({
  user: z
    .string()
    .min(3, "Usuario é obrigatório")
    .max(16, "Usuário muito extenso")
    .regex(
      /^[a-zA-Z0-9_]+$/,
      "Nome de usuário deve conter apenas letras, números ou _"
    ),
  password: z
    .string()
    .min(3, "Senha é obrigatória")
    .max(16, "Usuário muito extenso")
    .regex(/[A-Z]/, "A senha deve conter uma letra maiúscula")
    .regex(/[a-z]/, "A senha deve conter uma letra minúscula")
    .regex(/[0-9]/, "A senha deve conter um número")
    .regex(/[^A-Za-z0-9]/, "A senha deve conter um caractere especial"),
  email: z.string().email("Email inválido").nonempty("Email é obrigatório"),
});

export type LoginSchema = z.infer<typeof loginSchema>
export type RegisterSchema = z.infer<typeof registerSchema>
export type ProfileSchema = z.infer<typeof profileSchema>