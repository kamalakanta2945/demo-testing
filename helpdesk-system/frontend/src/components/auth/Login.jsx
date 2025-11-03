import { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { login as loginService } from '../../services/auth';
import { Button, TextField, Typography, Container, Paper } from '@mui/material';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const { login } = useAuth();
    const { showNotification } = useNotification();

    const handleSubmit = (e) => {
        e.preventDefault();
        setError(null);
        loginService({ username, password })
            .then((response) => {
                login(response.data.token);
                showNotification('Logged in successfully!', 'success');
            })
            .catch((err) => {
                setError('Invalid username or password');
                showNotification('Invalid username or password', 'error');
            });
    };

    return (
        <Container component="main" maxWidth="xs">
            <Paper elevation={3} style={{ padding: 20, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Typography component="h1" variant="h5">
                    User Login
                </Typography>
                {error && <Typography color="error">{error}</Typography>}
                <form onSubmit={handleSubmit} style={{ width: '100%', marginTop: 1 }}>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Username"
                        name="username"
                        autoComplete="username"
                        autoFocus
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        style={{ margin: '24px 0px 16px' }}
                    >
                        Sign In
                    </Button>
                </form>
            </Paper>
        </Container>
    );
};

export default Login;
