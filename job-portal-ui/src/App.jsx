
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import AdminDashboard from './components/AdminDashboard';
import ApplicantDashboard from './components/ApplicantDashboard';
import OAuthTokenHandler from './components/OAuthTokenHandler';

function App() {
  
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/applicant-dashboard" element={<ApplicantDashboard/>} />
          <Route path="/admin-dashboard" element={<AdminDashboard/>} />
          <Route path="/oauth-token" element={<OAuthTokenHandler />} />
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
