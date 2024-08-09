import { ToDosPage } from "./pages/ToDosPage.jsx";
import { Toaster } from "react-hot-toast";


function App() {

  return (
      <div className="container mx-auto">
          <ToDosPage />
          <Toaster />
      </div>
  )
}

export default App
