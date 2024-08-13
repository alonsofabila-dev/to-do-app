import { ToDosPage } from "./pages/ToDosPage.jsx";
import { Toaster } from "react-hot-toast";
import { TaskListProvider } from "./components/TaskListContext.jsx";


function App() {

  return (
      <div className="container mx-auto">
          <TaskListProvider>
              <ToDosPage />
              <Toaster />
          </TaskListProvider>
      </div>
  )
}

export default App
