import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Sidebar = () => {
    const { user } = useAuth();

    if (!user) {
        return null;
    }

    return (
        <aside>
            {user.role === 'ROLE_ADMIN' ? (
                <ul>
                    <li>
                        <Link to="/admin/dashboard">Dashboard</Link>
                    </li>
                    <li>
                        <Link to="/admin/assignments">Assignments</Link>
                    </li>
                </ul>
            ) : (
                <ul>
                    <li>
                        <Link to="/dashboard">My Tickets</Link>
                    </li>
                    <li>
                        <Link to="/tickets/new">Create Ticket</Link>
                    </li>
                </ul>
            )}
        </aside>
    );
};

export default Sidebar;
