import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';

const Navbar = () => {
    const { user, logout } = useAuth();

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" style={{ flexGrow: 1 }}>
                    <Link to="/" style={{ textDecoration: 'none', color: 'inherit' }}>
                        Help Desk
                    </Link>
                </Typography>
                {user ? (
                    <>
                        <Typography>Welcome, {user.username}</Typography>
                        <Button color="inherit" onClick={logout}>Logout</Button>
                    </>
                ) : (
                    <>
                        <Button color="inherit" component={Link} to="/login">Login</Button>
                        <Button color="inherit" component={Link} to="/register">Register</Button>
                    </>
                )}
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
