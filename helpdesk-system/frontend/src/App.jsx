import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { NotificationProvider } from './context/NotificationContext';
import Navbar from './components/layout/Navbar';
import Sidebar from './components/layout/Sidebar';
import Login from './components/auth/Login';
import Register from './components/auth/Register';
import UserDashboard from './pages/UserDashboard';
import AdminDashboard from './pages/AdminDashboard';
import TicketForm from './components/user/TicketForm';
import TicketDetail from './components/user/TicketDetail';
import ProtectedRoute from './components/ProtectedRoute';
import TicketAssignment from './components/admin/TicketAssignment';
import { Box, CssBaseline } from '@mui/material';
import './App.css';

const AppRoutes = () => {
    const { user } = useAuth();

    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/dashboard" element={<ProtectedRoute><UserDashboard /></ProtectedRoute>} />
            <Route path="/admin/dashboard" element={<ProtectedRoute adminOnly={true}><AdminDashboard /></ProtectedRoute>} />
            <Route path="/admin/assignments" element={<ProtectedRoute adminOnly={true}><TicketAssignment /></ProtectedRoute>} />
            <Route path="/tickets/new" element={<ProtectedRoute><TicketForm /></ProtectedRoute>} />
            <Route path="/tickets/:id" element={<ProtectedRoute><TicketDetail /></ProtectedRoute>} />
            <Route path="/" element={user ? (user.role === 'ROLE_ADMIN' ? <Navigate to="/admin/dashboard" /> : <Navigate to="/dashboard" />) : <Navigate to="/login" />} />
        </Routes>
    );
}

function App() {
    return (
        <AuthProvider>
            <NotificationProvider>
                <Router>
                    <Box sx={{ display: 'flex' }}>
                        <CssBaseline />
                        <Navbar />
                        <Sidebar />
                        <Box component="main" sx={{ flexGrow: 1, p: 3, mt: 8 }}>
                            <AppRoutes />
                        </Box>
                    </Box>
                </Router>
            </NotificationProvider>
        </AuthProvider>
    );
}

export default App;
