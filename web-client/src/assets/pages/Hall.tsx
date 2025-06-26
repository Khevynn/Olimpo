import NavbarHorizontal from "../components/ui/NavbarHorizontal";
import NavbarVertical from "../components/ui/NavbarVertical";

function Hall() {
  return (
    <div className="w-screen h-screen bg-dark-300">
      {/* NAVBAR*/}
      <NavbarHorizontal />
      <NavbarVertical active="hall" />
    </div>
  );
}

export default Hall;
