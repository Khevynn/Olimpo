import NavbarHorizontal from "../components/ui/NavbarHorizontal";
import NavbarVertical from "../components/ui/NavbarVertical";

function Home() {
    return (
        <div className="w-screen h-screen bg-dark-300">
            {/* NAVBAR*/}
            <NavbarHorizontal />
            <NavbarVertical active="home"/>
        </div>
    )
}

export default Home;