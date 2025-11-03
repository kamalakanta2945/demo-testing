import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Drawer, List, ListItem, ListItemText } from '@mui/material';

const Sidebar = () => {
    const { user } = useAuth();

    if (!user) {
        return null;
    }

    const drawerWidth = 240;

    return (
        <Drawer
            variant="permanent"
            sx={{
                width: drawerWidth,
                flexShrink: 0,
                [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
            }}
        >
            <div style={{ height: 64 }} />
            {user.role === 'ROLE_ADMIN' ? (
                <List>
                    <ListItem button component={Link} to="/admin/dashboard">
                        <ListItemText primary="Dashboard" />
                    </ListItem>
                    <ListItem button component={Link} to="/admin/assignments">
                        <ListItemText primary="Assignments" />
                    </ListItem>
                </List>
            ) : (
                <List>
                    <ListItem button component={Link} to="/dashboard">
                        <ListItemText primary="My Tickets" />
                    </ListItem>
                    <ListItem button component={Link} to="/tickets/new">
                        <ListItemText primary="Create Ticket" />
                    </ListItem>
                </List>
            )}
        </Drawer>
    );
};

export default Sidebar;
