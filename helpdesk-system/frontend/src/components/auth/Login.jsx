import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { login as loginService } from '../../services/auth';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const { login } = useAuth();

    const handleSubmit = (e) => {
        e.preventDefault();
        loginService({ username, password }).then((response) => {
            login(response.data.token);
        });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>User Login</h2>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button type="submit">Login</button>
        </form>
    );
};

export default Login;
