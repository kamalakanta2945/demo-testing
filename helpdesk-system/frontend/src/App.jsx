import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/layout/Navbar';
import Sidebar from './components/layout/Sidebar';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import UserDashboard from './pages/UserDashboard';
import AdminDashboard from './pages/AdminDashboard';
import TicketForm from './components/user/TicketForm';
import TicketDetail from './components/user/TicketDetail';
import './App.css';

function App() {
    return (
        <AuthProvider>
            <Router>
                <Navbar />
                <div style={{ display: 'flex' }}>
                    <Sidebar />
                    <main style={{ flexGrow: 1, padding: '20px' }}>
                        <Routes>
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                            <Route path="/dashboard" element={<UserDashboard />} />
                            <Route path="/admin/dashboard" element={<AdminDashboard />} />
                            <Route path="/tickets/new" element={<TicketForm />} />
                            <Route path="/tickets/:id" element={<TicketDetail />} />
                        </Routes>
                    </main>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;
