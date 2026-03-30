// import './App.css'
import { Box, Button } from "@mui/material"
import { useContext, useEffect } from "react"
import { AuthContext } from "react-oauth2-code-pkce"
import { useDispatch } from "react-redux";
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router"
import { logout, setCredentials } from "./store/authSlice";
import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetail from "./components/ActivityDetail";

const ActivitiesPage = () => {
  return (
    <Box sx={{ p: 2, border: '1px dashed grey' }}>
      <ActivityForm onActivityAdded = { () => window.location.reload()}/>
      <ActivityList />
    </Box>
  );
}

function App() {
  
  const { token, tokenData, logIn, logOut } 
      = useContext(AuthContext);
  const dispatch = useDispatch();

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({token, user: tokenData}));
    }
  }, [token, tokenData, dispatch]);

  return (
    <Router>
      {!token ? (
        <Box sx={{ p: 4, textAlign: 'center' }}>
          <Button 
            variant="contained" 
            size="large"
            onClick={() => logIn()}
            sx={{ mt: 2 }}
          >
            INGRESAR (LOGIN)
          </Button>
        </Box>
      ) : (
        <Box sx={{ p: 2 }}>
          <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: 2 }}>
            <Button 
              variant="outlined" 
              color="error"
              onClick={() => {
                const logoutUrl = `http://localhost:7080/realms/master/protocol/openid-connect/logout?client_id=oauth2-pkce-client&post_logout_redirect_uri=${window.location.origin}`;
                logOut();
                dispatch(logout());
                window.location.href = logoutUrl;
              }} 
            >
              CERRAR SESIÓN (LOGOUT)
            </Button>
          </Box>
          
          <Box component="section" sx={{ p: 2, border: '1px dashed grey', borderRadius: 2 }}>
            <Routes>
              <Route path="/activities" element={<ActivitiesPage />}/>
              <Route path="/activities/:id" element={<ActivityDetail />}/>
              <Route path="/" element={<Navigate to="/activities" replace/>}/>
            </Routes>
          </Box>
        </Box>
      )}
    </Router>
  )
}

export default App
