import { createContext, useContext, useState, useEffect } from 'react';
import { getProfile } from '../services/auth';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            getProfile().then((response) => {
                setUser(response.data);
            });
        }
    }, []);

    const login = (token) => {
        localStorage.setItem('token', token);
        return getProfile().then((response) => {
            setUser(response.data);
        });
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
    };

    const value = {
        user,
        login,
        logout,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    return useContext(AuthContext);
};
