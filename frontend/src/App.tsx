import Router from "./routers/Routers";
import { SnackbarProvider } from "notistack";

function App() {
  return (
    <SnackbarProvider
      anchorOrigin={{
        vertical: "bottom",
        horizontal: "center",
      }}
      autoHideDuration={1000}
    >
      <Router />
    </SnackbarProvider>
  );
}

export default App;
