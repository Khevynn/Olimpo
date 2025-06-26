import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import Register from "./assets/pages/Register";
import Login from "./assets/pages/Login";
import Index from "./assets/pages/Index";
import Home from "./assets/pages/Home";
import Hall from "./assets/pages/Hall";
import Games from "./assets/pages/Games";
import Profile from "./assets/pages/Profile";
import Messages from "./assets/pages/Messages";
import NotFound from "./assets/pages/NotFound";

let router = createBrowserRouter([
  {
    path: "/",
    element: <Index />,
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/home",
    element: <Home />,
  },
  {
    path: "/hall",
    element: <Hall />,
  },
  {
    path: "/games",
    element: <Games />,
  },
  {
    path: "/profile",
    element: <Profile />,
  },
  {
    path: "/messages",
    element: <Messages />,
  },
  {
  path: "*",
  element: <NotFound />,
}
]);

const queryClient = new QueryClient();

const container = document.getElementById("root");

if (container) {
  const root = createRoot(container);
  root.render(
    <StrictMode>
      <QueryClientProvider client={queryClient}>
        <RouterProvider router={router} />
      </QueryClientProvider>
    </StrictMode>
  );
} else {
  console.error("Elemento 'root' não encontrado no DOM.");
}
